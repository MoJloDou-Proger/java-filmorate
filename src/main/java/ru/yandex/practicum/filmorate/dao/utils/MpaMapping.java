package ru.yandex.practicum.filmorate.dao.utils;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@NoArgsConstructor
public class MpaMapping {

    public MpaRating mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new MpaRating(resultSet.getInt("rating_id"), resultSet.getString("name"));
    }
}
