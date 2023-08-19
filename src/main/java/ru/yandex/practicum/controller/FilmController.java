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
    private final Map<Integer, Film> filmMap = new LinkedHashMap<>();
    private int id = 1;

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("получение всех фильмов");
        return new ArrayList<>(filmMap.values());

    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        if (!filmMap.containsKey(film.getId()) || !film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            film.setId(id++);
            filmMap.put(film.getId(), film);
            log.info("добавление фильма");
        } else {
            throw new ValidationException();
        }
        return film;

    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        filmMap.put(film.getId(), film);
        log.info("изменение фильма");
        return film;

    }
}
