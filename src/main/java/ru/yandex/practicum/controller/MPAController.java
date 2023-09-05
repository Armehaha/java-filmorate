package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.FilmMPA;
import ru.yandex.practicum.storage.db.MPAImpl;

import java.util.List;


@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MPAController {

    private final MPAImpl mpa;

    @GetMapping
    public List<FilmMPA> getAllMPA() {
        return mpa.getAllMPA();
    }

    @GetMapping("/{id}")
    public FilmMPA getMPAById(@PathVariable int id) {
        return mpa.getMPAById(id);
    }
}
