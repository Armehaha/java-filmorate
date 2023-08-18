package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> integerFilmMap = new LinkedHashMap<>();
    private int id = 1;

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("получение всех фильмов");
        return new ArrayList<>(integerFilmMap.values());

    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.parse("1985-12-28"))) {
            log.error("ошибка валидации даты");
            throw new ValidationException();
        }
        if (integerFilmMap.containsKey(film.getId())) {
            throw new ValidationException();
        }
        film.setId(id++);
        integerFilmMap.put(film.getId(), film);
        log.info("добавление фильма");
        return film;
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {

        if (integerFilmMap.containsKey(film.getId())) {
            integerFilmMap.put(film.getId(), film);
            log.info("изменение фильма");
            return film;
        } else
            throw new ValidationException();

    }
}
