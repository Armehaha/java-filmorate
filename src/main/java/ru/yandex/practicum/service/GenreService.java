package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.FilmGenre;
import ru.yandex.practicum.storage.db.GenreDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public List<FilmGenre> getAllGenre() {
        return genreDbStorage.getAllGenre();
    }

    public FilmGenre getGenreById(long genreId) {
        return genreDbStorage.getGenreById(genreId);
    }

}
