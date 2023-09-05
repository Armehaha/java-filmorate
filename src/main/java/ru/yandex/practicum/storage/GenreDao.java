package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.FilmGenre;

import java.util.List;

public interface GenreDao {
    List<FilmGenre> getAllGenre();

    FilmGenre getGenreById(long genreId);
}
