package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.FilmStorage;
import ru.yandex.practicum.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService implements FilmServiceInt {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int filmId) {
        Film film = filmStorage.getById(filmId);
        if (film == null) {
            throw new NotFoundException();
        }
        return film;
    }


    public void putLike(long filmId, int userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException();
        }
        if (filmStorage.getById(filmId) == null) {
            throw new NotFoundException();
        }
        Film film = filmStorage.getById(filmId);

        if (film.getUserLike().contains(userId)) {
            throw new IllegalArgumentException("Пользователь уже поставил фильму лайк");
        }
        film.setLikes(film.getLikes() + 1);
        film.getUserLike().add(userId);
        filmStorage.updateFilmFromId(filmId, film);
    }

    public void deleteLike(long filmId, int userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("userId");
        }
        if (filmStorage.getById(filmId) == null) {
            throw new NotFoundException("filmId");
        }
        Film film = filmStorage.getById(filmId);

        if (!film.getUserLike().contains(userId)) {
            throw new IllegalArgumentException("Пользователь еще не поставил фильму лайк");
        }
        film.getUserLike().remove(userId);
        film.setLikes(film.getLikes() - 1);
        filmStorage.updateFilmFromId(filmId, film);
    }

    public List<Film> getPopularFilm(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing(Film::getLikes, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(count)
                .collect(Collectors.toList());
    }
}
