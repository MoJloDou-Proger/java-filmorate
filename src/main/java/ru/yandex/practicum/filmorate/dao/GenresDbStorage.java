package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.utils.GenresMapping;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public class GenresDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenresMapping genresMapping;

    private static final String ALL_GENRES = "SELECT * FROM genre";
    private static final String GENRES_BY_ID = "SELECT * FROM genre WHERE genre_id = ?";

    @Autowired
    public GenresDbStorage(JdbcTemplate jdbcTemplate, GenresMapping genresMapping) {
        this.jdbcTemplate = jdbcTemplate;
        this.genresMapping = genresMapping;
    }

    public List<Genre> getAllGenres(){
        return jdbcTemplate.query(ALL_GENRES, genresMapping::mapRowToGenre);
    }

    public Genre getGenre(Integer id){
        return jdbcTemplate.queryForObject(GENRES_BY_ID, genresMapping::mapRowToGenre, id);
    }
}