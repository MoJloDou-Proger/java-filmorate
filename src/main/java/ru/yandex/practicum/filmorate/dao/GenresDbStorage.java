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

    @Autowired
    public GenresDbStorage(JdbcTemplate jdbcTemplate, GenresMapping genresMapping) {
        this.jdbcTemplate = jdbcTemplate;
        this.genresMapping = genresMapping;
    }

    public List<Genre> getAllGenres(){
        String sqlQuery = "SELECT * FROM genre";
        return jdbcTemplate.query(sqlQuery, genresMapping::mapRowToGenre);
    }

    public Genre getGenre(Integer id){
        String sqlQuery = "SELECT * FROM genre WHERE genre_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, genresMapping::mapRowToGenre, id);
    }
}