package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.FilmGenre;
import ru.yandex.practicum.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genre;

    @GetMapping
    public List<FilmGenre> getAllGenres() {
        return genre.getAllGenre();
    }

    @GetMapping("/{id}")
    public FilmGenre getGenreById(@PathVariable int id) {
        return genre.getGenreById(id);
    }
}
