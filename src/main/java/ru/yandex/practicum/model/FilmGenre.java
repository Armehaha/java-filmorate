package ru.yandex.practicum.model;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilmGenre {
    private int id;
    private String name;

    public FilmGenre(int id) {
        this.id = id;
    }

}