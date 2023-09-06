package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.FilmGenre;
import ru.yandex.practicum.model.FilmMPA;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.MPAStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MPAStorageTest {
    private final MPAStorage mpaService;
    User user;
    User user2;
    User user3;

    Film film;
    Film film2;
    Film film3;
    FilmMPA mpaFilm = new FilmMPA();
    Set<FilmGenre> genresFilm;


    @BeforeEach
    public void createObjects() {
        user = new User(0, "ar@yandex.ru", "Armen Osipyan", "sdfasdfas",
                LocalDate.of(1995, 12, 1));
        user2 = new User(0, "ar14314@yandex.ru", "Armen Osipyan", "sdfasddsfsdffas",
                LocalDate.of(2005, 12, 1));
        user3 = new User(0, "ar3213123@yandex.ru", "Armen Osipyan", "sdfasdfasdsfsfd",
                LocalDate.of(2007, 12, 1));
        mpaFilm.setId(3);

        genresFilm = Set.of(new FilmGenre(3), new FilmGenre(5));
        genresFilm = Set.of(new FilmGenre(1), new FilmGenre(5));
        film = new Film("sdfsf", "sfsadfasdf",
                LocalDate.of(2017, 12, 5), 100, 0, genresFilm, mpaFilm);

        film2 = new Film("sdfsf", "sfsadfasdf",
                LocalDate.of(1905, 12, 5), 100, 0, genresFilm, mpaFilm);

        film3 = new Film("sdfsf", "sfsadfasdf",
                LocalDate.of(2000, 12, 5), 100, 0, genresFilm, mpaFilm);
    }


    @Test
    public void getAllMPA() {
        List<FilmMPA> all = mpaService.getAllMPA();
        assertThat(all).asList().hasSize(5);
        final int first = 1;
        final int second = 2;

        assertThat(all).asList().startsWith(mpaService.getMPAById(first));
        assertThat(all).asList().contains(mpaService.getMPAById(second));
    }

    @Test
    public void getMPAById() {
        final int third = 3;
        final String name = "PG-13";

        assertThat(mpaService.getMPAById(third))
                .hasFieldOrPropertyWithValue("id", 3)
                .hasFieldOrPropertyWithValue("name", name);
    }
}
