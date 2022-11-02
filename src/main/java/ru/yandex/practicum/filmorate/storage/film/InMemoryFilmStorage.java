package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constants.Constants.IN_MEMORY_FILM_STORAGE;
import static ru.yandex.practicum.filmorate.validation.Validation.validateFilm;

@Slf4j
@Component
@Qualifier(IN_MEMORY_FILM_STORAGE)
public class InMemoryFilmStorage implements FilmStorage {
    private int filmId = 1;
    private final List<Film> films;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public InMemoryFilmStorage(List<Film> films, InMemoryUserStorage userStorage) {
        this.films = films;
        this.userStorage = userStorage;
    }

    @Override
    public Film createFilm(Film film){
        log.info("Получен POST-запрос с объектом Film: {}", film);
        film.setId(filmId);
        validateFilm(film);
        films.add(film);
        log.info("Фильм {} c id={} добавлен, объект: {}", film.getName(), filmId, film);
        increaseFilmId();
        return film;
    }

    @Override
    public Film updateFilm(Film film){
        log.info("Получен PUT-запрос с объектом Film: {}", film);
        validateFilm(film);
        int filmId = film.getId();
        for (Film u : films) {
            if (u.getId() == filmId) {
                films.set(films.indexOf(u), film);
                log.info("Фильм c id={} обновлён", filmId);
                return film;
            }
        }
        throw new IdNotFoundException("Фильм с id=" + filmId + " не найден");
    }

    @Override
    public String deleteFilm(int id) {
        for (Film u : films) {
            if (u.getId() == id){
                films.remove(u);
                log.info("Фильм c id={} удалён", id);
                return String.format("Фильм c id=%s удалён", id);
            }
        }
        throw new IdNotFoundException("Фильм с id=" + id + " не найден");
    }

    @Override
    public Film findFilm(int id){
        for (Film f : films){
            if (f.getId() == id){
                return f;
            }
        }
        throw new IdNotFoundException("Фильм с id=" + id + " не найден");
    }

    @Override
    public List<Film> getFilms(){
        return new ArrayList<>(films);
    }

    @Override
    public String putLike(Integer id, Integer userId) {
        if (findFilm(id) != null && userStorage.findUser(userId) != null){
            findFilm(id).getLikes().add(userId);
            return String.format("Фильму с id=%s поставлен лайк пользователем с userId=%s.", id, userId);
        }
        throw new IdNotFoundException("Указанные id неверны.");
    }

    @Override
    public String deleteLike(Integer id, Integer userId) {
        if (findFilm(id) != null && userStorage.findUser(userId) != null){
            findFilm(id).getLikes().remove(userId);
            return String.format("У фильма с id=%s удалён лайк пользователем с userId=%s.", id, userId);
        }
        throw new IdNotFoundException("Указанные id неверны.");
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        List<Film> filmList = getFilms();
        return filmList.stream().sorted((po, p1) -> {
            if (po.getLikes().size() > p1.getLikes().size()){
                return -1;
            } else if (po.getLikes().size() < p1.getLikes().size()){
                return 1;
            } return 0;
        }).limit(count).collect(Collectors.toList());
    }

    private void increaseFilmId(){
        filmId++;
    }
}
