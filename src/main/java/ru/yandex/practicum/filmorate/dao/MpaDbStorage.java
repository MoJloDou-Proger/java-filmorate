package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.utils.MpaMapping;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

@Component
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaMapping mpaMapping;

    public MpaDbStorage(JdbcTemplate jdbcTemplate, MpaMapping mpaMapping) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaMapping = mpaMapping;
    }

    public List<MpaRating> getAllMpa(){
        String sqlQuery = "SELECT * FROM mpa_rating";
        return jdbcTemplate.query(sqlQuery, mpaMapping::mapRowToMpa);
    }

    public MpaRating getMpa(Integer id){
        String sqlQuery = "SELECT * FROM mpa_rating WHERE rating_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, mpaMapping::mapRowToMpa, id);
    }
}
