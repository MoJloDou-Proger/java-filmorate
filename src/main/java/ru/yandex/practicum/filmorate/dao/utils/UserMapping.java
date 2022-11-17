package ru.yandex.practicum.filmorate.dao.utils;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.constants.Constants.DATE_FORMAT;

@Component
@NoArgsConstructor
public class UserMapping {

    public User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(resultSet.getInt("user_id"), resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                LocalDate.parse(resultSet.getString("birthday"), DATE_FORMAT));
    }
}
