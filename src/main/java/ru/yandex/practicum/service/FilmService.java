package ru.yandex.practicum.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.FilmStorage;
import ru.yandex.practicum.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Getter
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;

    }

    public Film getOneFilm(int filmId) {
        if (filmStorage.getOneFilm(filmId) != null) {
            return filmStorage.getOneFilm(filmId);
        } else {
            throw new NoSuchElementException();
        }
    }

    public void putLike(int filmId, int userId) {
        if (userStorage.getOneUser(userId) == null) {
            throw new NoSuchElementException();
        }
        if (filmStorage.getOneFilm(filmId) == null) {
            throw new NoSuchElementException();
        }
        Film film = filmStorage.getOneFilm(filmId);

        if (film.getUserLike().contains(userId)) {
            throw new IllegalArgumentException("Пользователь уже поставил фильму лайк");
        }
        film.setLikes(film.getLikes() + 1);
        film.getUserLike().add(userId);
        filmStorage.updateFilmFromId(filmId, film);
    }

    public void deleteLike(int filmId, int userId) {
        if (userStorage.getOneUser(userId) == null) {
            throw new NoSuchElementException("userId");
        }
        if (filmStorage.getOneFilm(filmId) == null) {
            throw new NoSuchElementException("filmId");
        }
        Film film = filmStorage.getOneFilm(filmId);

        if (!film.getUserLike().contains(userId)) {
            throw new IllegalArgumentException("Пользователь еще не поставил фильму лайк");
        }
        List<Integer> filmLike = film.getUserLike()
                .stream()
                .filter(integer -> integer != userId)
                .collect(Collectors.toList());
        film.getUserLike().clear();
        film.setLikes(film.getLikes() - 1);
        filmLike.forEach(integer -> film.getUserLike().add(integer));

        filmStorage.updateFilmFromId(filmId, film);
    }

    public List<Film> getPopularFilm(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing(Film::getLikes, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(count)
                .collect(Collectors.toList());
    }
}
