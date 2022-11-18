package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.utils.UserMapping;
import ru.yandex.practicum.filmorate.exception.FindUserException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.constants.Constants.CREATE_USER_QUERY;
import static ru.yandex.practicum.filmorate.validation.Validation.validateUser;

@Slf4j
@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapping userMapping;

    private static final String ALL_USERS = "SELECT * FROM users";
    private static final String UPDATE_USER_BY_ID =
            "UPDATE users SET " +
                         "email = ?, " +
                         "login = ?, " +
                         "name = ?, " +
                         "birthday = ? " +
            "WHERE user_id = ?";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE user_id = ?";
    private static final String FIND_USER_BY_ID = "SELECT * FROM users WHERE user_id = ?";
    private static final String ADD_FRIEND = "INSERT INTO friends(user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_FRIEND = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String RECEIVE_FRIENDS =
            "SELECT * " +
            "FROM users AS u " +
            "WHERE u.user_id IN (" +
                "SELECT f.friend_id " +
                "FROM friends AS f " +
                "WHERE f.user_id = ?" +
            ")";
    private static final String RECEIVE_COMMON_FRIENDS =
            "SELECT * " +
            "FROM users AS u " +
            "WHERE u.user_id IN (" +
                "SELECT f1.friend_id " +
                "FROM friends AS f1 " +
                "JOIN friends AS f2 " +
                    "ON f1.friend_id = f2.friend_id AND f1.user_id <> f2.user_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?" +
            ")";
    public UserDbStorage(JdbcTemplate jdbcTemplate, UserMapping userMapping){
        this.jdbcTemplate=jdbcTemplate;
        this.userMapping = userMapping;
    }

    @Override
    public List<User> allUsers() {
        return jdbcTemplate.query(ALL_USERS, userMapping::mapRowToUser);
    }

    @Override
    public User createUser(User user) {
        validateUser(user);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(CREATE_USER_QUERY, new String[]{"user_id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        user.setId(id);
        log.debug("Created user: {} with id: {}", user.getName(), id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        validateUser(user);
        int updateId = user.getId();
        int upd = jdbcTemplate.update(UPDATE_USER_BY_ID
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , updateId);
        if (upd > 0){
            log.info("Пользователь c id={} обновлён", updateId);
            return findUser(updateId);
        } else throw new IdNotFoundException("Пользователь с id=" + updateId + " не найден");
    }

    @Override
    public String deleteUser(int id) {
        int del = jdbcTemplate.update(DELETE_USER_BY_ID, id);
        if (del > 0){
            return String.format("Пользователь c id=%s удалён", id);
        } else throw new IdNotFoundException("Пользователь с id=" + id + " не найден");
    }

    @Override
    public User findUser(int id) {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_ID, userMapping::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e){
            throw new IdNotFoundException("Пользователь с id=" + id + " не найден");
        }
    }


    @Override
    public String addFriend(Integer id, Integer friendId) {
        if (isUsersNotNull(id, friendId)){
            jdbcTemplate.update(ADD_FRIEND, id, friendId);
            return String.format("Пользователь с id=%s добавлен в список друзей.", friendId);
        }
        throw new FindUserException("Среди указанных пользователей null");
    }

    @Override
    public String deleteFriend(Integer id, Integer friendId) {
        if (isUsersNotNull(id, friendId)){
            jdbcTemplate.update(DELETE_FRIEND, id, friendId);
            jdbcTemplate.update(DELETE_FRIEND, friendId, id);
            return String.format("Пользователь с id=%s удалён из списка друзей.", friendId);
        }
        throw new FindUserException("Среди указанных пользователей null");
    }

    @Override
    public List<User> receiveFriends(Integer id) {
        if (findUser(id) != null){
            return jdbcTemplate.query(RECEIVE_FRIENDS, userMapping::mapRowToUser,id);
        }
        throw new FindUserException("Среди указанных пользователей null");
    }

    @Override
    public List<User> receiveCommonFriends(Integer id, Integer otherId) {
        if (isUsersNotNull(id, otherId)){
            return jdbcTemplate.query(RECEIVE_COMMON_FRIENDS, userMapping::mapRowToUser, id, otherId);
        }
        throw new FindUserException("Среди указанных пользователей null");
    }

    @Override
    public boolean isUsersNotNull(Integer id, Integer otherId) {
        return findUser(id) != null && findUser(otherId) != null;
    }
}
