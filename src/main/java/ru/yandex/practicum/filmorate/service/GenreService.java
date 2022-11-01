package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenresDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public class GenreService {
    private final GenresDbStorage genresDbStorage;

    @Autowired
    public GenreService(GenresDbStorage genresDbStorage) {
        this.genresDbStorage = genresDbStorage;
    }

    public List<Genre> getAllGenres(){
        return genresDbStorage.getAllGenres();
    }

    public Genre getGenre(Integer id){
        return genresDbStorage.getGenre(id);
    }
}
