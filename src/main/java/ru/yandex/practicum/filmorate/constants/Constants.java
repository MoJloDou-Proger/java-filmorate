package ru.yandex.practicum.filmorate.constants;

import java.time.LocalDate;

public class Constants {
    public static final LocalDate MIN_ALLOWED_DATE = LocalDate.of(1895,12,28);
    public static final LocalDate DATE_BEFORE_MIN_ALLOWED = LocalDate.of(1895,12,27);
    public static final String FILM_DB_STORAGE = "FilmDbStorage";
    public static final String USER_DB_STORAGE = "UserDbStorage";
    public static final String IN_MEMORY_USER_STORAGE = "InMemoryUserStorage";
    public static final String IN_MEMORY_FILM_STORAGE = "InMemoryFilmStorage";

    public static final String CREATE_USER_QUERY =
            "INSERT INTO users (email, login, name, birthday) " +
                    "VALUES (?, ?, ?, ?)";

}
