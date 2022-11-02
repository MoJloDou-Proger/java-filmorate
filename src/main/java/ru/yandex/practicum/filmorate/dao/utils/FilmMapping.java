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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class FilmMapping {
    public Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Array genreIdArray = resultSet.getArray("gen_id");
        Array genreNameArray = resultSet.getArray("genre_name");
        List<Genre> filmGenreSet = new ArrayList<>();

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
            for (int i = 0; i < genreIds.size(); i++){
                filmGenreSet.add(new Genre(genreIds.get(i), genreNames.get(i)));
            }
        }

        return new Film(resultSet.getInt("f_id"), resultSet.getString("film_name"),
                resultSet.getString("description"),
                LocalDate.parse(resultSet.getString("release_date"), format),
                resultSet.getLong("duration"),
                new MpaRating(resultSet.getInt("r_id"), resultSet.getString("mpa_name")),
                resultSet.getLong("rate"), filmGenreSet);
    }
}
