package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.utils.FilmMapping;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.constants.Constants.*;
import static ru.yandex.practicum.filmorate.validation.Validation.validateFilm;

@Slf4j
@Qualifier("FilmDbStorage")
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapping filmMapping;
    private final UserDbStorage userDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmMapping filmMapping, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmMapping = filmMapping;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public List<Film> getFilms() {
        return jdbcTemplate.query(FILM_INFORMATION + "GROUP BY f.film_id", filmMapping::mapRowToFilm);
    }

    @Override
    public Film createFilm(Film film) {
        validateFilm(film);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(CREATE_FILM_QUERY, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setLong(5, film.getRate());
            ps.setObject(6, film.getMpa().getId());
            return ps;
        }, keyHolder);

        int filmId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(filmId);

        filmGenres(filmId, film.getGenres());

        log.debug("Создан фильм: {} с id: {}", film.getName(), filmId);
        return film;
    }

    private void filmGenres(int filmId, List<Genre> filmGenres){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (filmGenres != null) {
            for (Genre g : filmGenres) {
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection
                            .prepareStatement(CREATE_FILM_GENRES_QUERY, new String[]{"film_genre_id"});
                    ps.setInt(1, g.getId());
                    ps.setInt(2, filmId);
                    ps.setInt(3, g.getId());
                    ps.setInt(4, filmId);
                    return ps;
                }, keyHolder);
            }
        }
    }

    @Override
    public Film updateFilm(Film film) {
        validateFilm(film);

        int updateId = film.getId();

        String sqlQuery = UPDATE_FILM_QUERY;
        int upd = jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , updateId);

        sqlQuery = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, updateId);

        filmGenres(updateId, film.getGenres());

        if (upd > 0){
            log.info("Фильм c id={} обновлён", updateId);
            return findFilm(updateId);
        } else throw new IdNotFoundException("Фильм с id=" + updateId + " не найден");
    }

    @Override
    public String deleteFilm(int id) {
        String sqlQuery = "DELETE FROM films WHERE film_id = ?";
        int del = jdbcTemplate.update(sqlQuery, id);
        if (del > 0){
            return String.format("Фильм c id=%s удалён", id);
        } else throw new IdNotFoundException("Фильм с id=" + id + " не найден");
    }

    @Override
    public Film findFilm(int filmId) {
        String sqlQuery = FILM_INFORMATION + "WHERE f.film_id = ? GROUP BY f.film_id";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, filmMapping::mapRowToFilm, filmId);
        } catch (EmptyResultDataAccessException e){
            throw new IdNotFoundException("Фильм с id=" + filmId + " не найден");
        }
    }

    @Override
    public String putLike(Integer filmId, Integer userId) {
        if (findFilm(filmId) != null && userDbStorage.findUser(userId) != null){
            String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery, filmId, userId);
            return String.format("Фильму с id=%s поставлен лайк пользователем с userId=%s.", filmId, userId);
        }
        throw new IdNotFoundException("Указанные id неверны.");
    }

    @Override
    public String deleteLike(Integer filmId, Integer userId) {
        if (findFilm(filmId) != null && userDbStorage.findUser(userId) != null){
            String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
            jdbcTemplate.update(sqlQuery, filmId, userId);
            return String.format("У фильма с id=%s удалён лайк пользователем с userId=%s.", filmId, userId);
        }
        throw new IdNotFoundException("Указанные id неверны.");
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        String sqlQuery = FILM_INFORMATION + "GROUP BY f.film_id ORDER BY COUNT(l.film_id) DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, filmMapping::mapRowToFilm, count);
    }
}

