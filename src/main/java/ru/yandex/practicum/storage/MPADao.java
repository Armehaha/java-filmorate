package ru.yandex.practicum.storage;


import ru.yandex.practicum.model.FilmMPA;

import java.util.List;

public interface MPADao {
    List<FilmMPA> getAllMPA();

    FilmMPA getMPAById(long mpaId);
}
