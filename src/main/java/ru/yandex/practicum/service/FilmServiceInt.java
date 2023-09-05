package ru.yandex.practicum.service;


import ru.yandex.practicum.model.Film;

import java.util.List;

public interface FilmServiceInt {
    List<Film> getPopularFilm(int count);

    void deleteLike(long filmId, int userId);

    void putLike(long filmId, int userId);
}
