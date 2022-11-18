package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
public interface FilmStorage {
    List<Film> getFilms();
    Film createFilm(Film film);
    Film updateFilm(Film film);
    String deleteFilm(int id);
    Film findFilm(int id);
    String putLike(Integer id, Integer userId);
    String deleteLike(Integer id, Integer userId);
    List<Film> getTopFilms(Integer count);
}
