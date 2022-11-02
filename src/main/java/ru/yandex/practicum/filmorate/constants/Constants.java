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

    public static final String CREATE_FILM_QUERY =
            "INSERT INTO films (name, description, release_date, duration, rate, rating_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?);";

    public static final String CREATE_FILM_GENRES_QUERY =
            "INSERT INTO film_genre (genre_id, film_id) " +
                    "SELECT ?, " +
                    "? " +
                    "WHERE NOT EXISTS (" +
                        "SELECT genre_id, " +
                               "film_id " +
                        "FROM film_genre " +
                        "WHERE genre_id = ? " +
                          "AND film_id = ?" +
                    ");";

    public static final String UPDATE_FILM_QUERY =
            "UPDATE films SET name = ?, " +
                             "description = ?, " +
                             "release_date = ?, " +
                             "duration = ?, " +
                             "rating_id = ? " +
            "WHERE film_id = ?";

    public static final String FILM_INFORMATION = "SELECT f.film_id AS f_id, " +
                                                        "f.name AS film_name, " +
                                                        "f.description, " +
                                                        "f.release_date, " +
                                                        "f.duration, " +
                                                        "f.rate, " +
                                                        "f.rating_id AS r_id, " +
                                                        "mp.name AS mpa_name, " +
                                                        "ARRAY_AGG(fg.genre_id) FILTER (WHERE fg.genre_id IS NOT NULL) AS gen_id, " +
                                                        "ARRAY_AGG(g.name) FILTER (WHERE g.name IS NOT NULL) AS genre_name, "  +
                                                        "COUNT(l.film_id) AS likes " +
                                                  "FROM films AS f " +
                                                  "LEFT JOIN mpa_rating AS mp " +
                                                        "ON f.rating_id = mp.rating_id " +
                                                  "LEFT JOIN film_genre AS fg " +
                                                        "ON f.film_id = fg.film_id " +
                                                  "LEFT JOIN genre AS g " +
                                                        "ON fg.genre_id = g.genre_id " +
                                                  "LEFT JOIN likes AS l " +
                                                        "ON f.film_id = l.film_id ";

}
