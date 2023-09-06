package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.model.FilmGenre;
import ru.yandex.practicum.storage.GenreStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GenreStorageTest {
    private final GenreStorage genreService;

    @Test
    public void shouldListGenres() {
        List<FilmGenre> listGenres = genreService.getAllGenre();

        assertThat(listGenres).asList().hasSize(6);
        final int first = 1;
        final int second = 2;

        assertThat(listGenres).asList().startsWith(genreService.getGenreById(first));
        assertThat(listGenres).asList().contains(genreService.getGenreById(second));
    }

    @Test
    public void getGenreById() {
        final int id = 3;
        final String name = "Мультфильм";

        assertThat(genreService.getGenreById(id))
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("name", name);
    }
}