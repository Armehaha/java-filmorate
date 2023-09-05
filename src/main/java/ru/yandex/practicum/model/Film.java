package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Film {
    private long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    @NonNull
    private int duration;
    private Set<Integer> userLike = new HashSet<>();
    private int likes;
    private Set<FilmGenre> genres;
    private FilmMPA mpa;

    public Film(String name, String description, LocalDate releaseDate, @NonNull int duration, int likes, Set<FilmGenre> genres, FilmMPA mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.genres = genres;
        this.mpa = mpa;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("description", description);
        user.put("release_date", releaseDate);
        user.put("duration", duration);
        user.put("likes", likes);
        user.put("mpa_id", mpa.getId());
        return user;
    }

}