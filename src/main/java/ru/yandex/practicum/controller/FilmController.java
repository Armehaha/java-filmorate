package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.ErrorResponse;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("получение всех фильмов");
        return filmService.getFilmStorage().getAllFilms();

    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        log.info("добавление фильма");
        return filmService.getFilmStorage().addFilm(film);

    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        log.info("изменение фильма");
        return filmService.getFilmStorage().updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getUserDyId(@PathVariable int id) {
        return filmService.getOneFilm(id);
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
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final NoSuchElementException e) {
        return new ErrorResponse(
                "Такого элемента нет", e.getMessage()
        );
    }
}
