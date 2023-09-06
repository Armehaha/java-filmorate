package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.FilmMPA;
import ru.yandex.practicum.storage.db.MpaDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MPAService {
    private final MpaDbStorage mpaDbStorage;

    public List<FilmMPA> getAllMPA() {
        return mpaDbStorage.getAllMPA();
    }

    public FilmMPA getMPAById(long mpaId) {
        return mpaDbStorage.getMPAById(mpaId);
    }
}
