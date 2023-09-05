package ru.yandex.practicum.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> filmMap = new HashMap<>();
    private int id = 1;

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(id++);
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
            return film;
        } else
            throw new NotFoundException();
    }

    @Override
    public Film getById(int filmId) {
        return filmMap.get(filmId);
    }

    @Override
    public void updateFilmFromId(int filmId, Film film) {
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(filmId, film);
        } else
            throw new NotFoundException();
    }
}
