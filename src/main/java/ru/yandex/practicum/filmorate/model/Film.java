package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    private long rate;
    private MpaRating mpa;
    private List<Genre> genres;
    private List<Integer> likes = new ArrayList<>();

    public Film(int id, String name, String description, LocalDate releaseDate, long duration, MpaRating mpa,
                long rate, List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
    }
}
