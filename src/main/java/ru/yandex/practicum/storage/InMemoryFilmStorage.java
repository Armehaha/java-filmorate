package ru.yandex.practicum.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (film.getReleaseDate().isAfter(LocalDate.parse("1895-12-28"))) {
            film.setId(id++);
            filmMap.put(film.getId(), film);

        } else {
            throw new ValidationException();
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);

            return film;
        } else
            throw new ValidationException();

    }

    @Override
    public Film getOneFilm(int filmId) {
        return filmMap.get(filmId);
    }

    @Override
    public void updateFilmFromId(int filmId, Film film) {
        filmMap.put(filmId, film);
    }
}
