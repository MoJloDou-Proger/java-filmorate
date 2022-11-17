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

    private static final String ALL_MPA_RATINGS = "SELECT * FROM mpa_rating";
    private static final String MPA_RATING_BY_ID = "SELECT * FROM mpa_rating WHERE rating_id = ?";

    public MpaDbStorage(JdbcTemplate jdbcTemplate, MpaMapping mpaMapping) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaMapping = mpaMapping;
    }

    public List<MpaRating> getAllMpa(){
        return jdbcTemplate.query(ALL_MPA_RATINGS, mpaMapping::mapRowToMpa);
    }

    public MpaRating getMpa(Integer id){
        return jdbcTemplate.queryForObject(MPA_RATING_BY_ID, mpaMapping::mapRowToMpa, id);
    }
}
