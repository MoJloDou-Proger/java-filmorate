package ru.yandex.practicum.filmorate.dao.utils;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@NoArgsConstructor
public class UserMapping {

    public User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new User(resultSet.getInt("user_id"), resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                LocalDate.parse(resultSet.getString("birthday"), format));
    }
}
