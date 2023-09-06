package ru.yandex.practicum.storage;


import ru.yandex.practicum.model.FilmMPA;

import java.util.List;

public interface MPAStorage {
    List<FilmMPA> getAllMPA();

    FilmMPA getMPAById(long mpaId);
}
