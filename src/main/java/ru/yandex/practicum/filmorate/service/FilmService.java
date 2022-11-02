package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.constants.Constants.FILM_DB_STORAGE;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier(FILM_DB_STORAGE) FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public String putLike(Integer id, Integer userId) {
        return filmStorage.putLike(id, userId);
    }

    public String deleteLike(Integer id, Integer userId) {
        return filmStorage.deleteLike(id, userId);
    }

    public List<Film> getTopFilms(Integer count) {
        return filmStorage.getTopFilms(count);
    }

    public List<Film> getFilms(){
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film){
        return filmStorage.createFilm(film);
    }

    public Film findFilm(int id){
        return filmStorage.findFilm(id);
    }

    public Film updateFilm(Film film){
        return filmStorage.updateFilm(film);
    }

    public String deleteFilm(int id) {
        return filmStorage.deleteFilm(id);
    }

}
