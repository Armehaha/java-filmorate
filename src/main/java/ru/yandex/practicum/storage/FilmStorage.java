package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getById(int filmId);

    void updateFilmFromId(int filmId, Film film);
}
