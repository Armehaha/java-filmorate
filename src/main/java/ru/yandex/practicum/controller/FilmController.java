package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;


    @GetMapping
    public List<Film> getAllFilms() {
        log.info("получение всех фильмов");
        return filmService.getAllFilms();

    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isAfter(LocalDate.parse("1895-12-28"))) {
            log.info("добавление фильма");
            return filmService.addFilm(film);
        } else {
            throw new ValidationException();
        }
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        log.info("изменение фильма");
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getUserDyId(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable int id, @PathVariable int userId) {
        filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilm(count);
    }

}
