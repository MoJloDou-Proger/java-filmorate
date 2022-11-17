package ru.yandex.practicum.filmorate.dao.utils;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constants.Constants.DATE_FORMAT;

@Component
@NoArgsConstructor
public class FilmMapping {
    public Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Array genreIdArray = resultSet.getArray("gen_id");
        Array genreNameArray = resultSet.getArray("genre_name");
        List<Genre> filmGenreList = new ArrayList<>();

        if (genreIdArray != null && genreNameArray != null) {
            List<Integer> genreIds = Arrays
                    .stream((Object[]) genreIdArray.getArray())
                    .map(Object::toString)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            List<String> genreNames = Arrays
                    .stream((Object[]) genreNameArray.getArray())
                    .map(Object::toString)
                    .collect(Collectors.toList());

            filmGenreList = genreIds.stream()
                    .map(k -> new Genre(k, genreNames.get(genreIds.indexOf(k))))
                    .collect(Collectors.toList());
        }

        return new Film(resultSet.getInt("f_id"), resultSet.getString("film_name"),
                resultSet.getString("description"),
                LocalDate.parse(resultSet.getString("release_date"), DATE_FORMAT),
                resultSet.getLong("duration"),
                new MpaRating(resultSet.getInt("r_id"), resultSet.getString("mpa_name")),
                resultSet.getLong("rate"), filmGenreList);
    }
}
