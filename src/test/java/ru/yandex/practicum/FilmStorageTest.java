package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
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
import ru.yandex.practicum.service.db.FilmServiceDb;
import ru.yandex.practicum.storage.FilmStorage;
import ru.yandex.practicum.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmStorageTest {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final FilmServiceDb filmService;
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
    public void addFilmNormal() {
        film = filmStorage.addFilm(film);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getById(film.getId()));

        assertThat(filmOptional)
                .hasValueSatisfying(f ->
                        assertThat(f)
                                .hasFieldOrPropertyWithValue("id", film.getId())
                                .hasFieldOrPropertyWithValue("name", "sdfsf")
                                .hasFieldOrPropertyWithValue("description", "sfsadfasdf")
                                .hasFieldOrPropertyWithValue("releaseDate",
                                        LocalDate.of(2017, 12, 5))
                                .hasFieldOrPropertyWithValue("duration",
                                        100)
                                .hasFieldOrPropertyWithValue("mpa.id", 3));
    }


    @Test
    public void getListFilms() {
        film = filmStorage.addFilm(film);
        film2 = filmStorage.addFilm(film2);
        film3 = filmStorage.addFilm(film3);
        List<Film> listFilms = filmStorage.getAllFilms();
        System.out.println("dsfsfsdfsdf"+ listFilms);
        assertThat(listFilms).asList().hasSize(3);
        assertThat(listFilms).asList().contains(filmStorage.getById(film.getId()));
        assertThat(listFilms).asList().contains(filmStorage.getById(film2.getId()));
        assertThat(listFilms).asList().contains(filmStorage.getById(film3.getId()));
        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "sdfsf"));
        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "sdfsf"));
        assertThat(Optional.of(listFilms.get(2)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "sdfsf"));
    }

    @Test
    public void getListFilmsEmpty() {
        List<Film> listFilms = filmStorage.getAllFilms();

        assertThat(listFilms).asList().hasSize(0);
        assertThat(listFilms).asList().isEmpty();
    }

    @Test
    public void addLikeNormal() {
        user = userStorage.addUser(user);
        film = filmStorage.addFilm(film);

        filmService.putLike(film.getId(), (int) user.getId());
        film = filmStorage.getById(film.getId());
        Set<Integer> like = film.getUserLike();

        assertThat(like).isEqualTo(Set.of(1));
    }

    @Test
    public void deleteLikeNormal() {
        user2 = userStorage.addUser(user2);
        film2 = filmStorage.addFilm(film2);

        filmService.putLike(film2.getId(), (int) user2.getId());
        filmService.deleteLike(film2.getId(), (int) user2.getId());
        film2 = filmStorage.getById(film2.getId());
        Set<Integer> like = film.getUserLike();

        assertThat(like).isEqualTo(Set.of());
    }

    @Test
    public void getPopularFilms() {
        user = userStorage.addUser(user);
        user2 = userStorage.addUser(user2);
        film = filmStorage.addFilm(film);
        film2 = filmStorage.addFilm(film2);

        filmService.putLike(film.getId(), (int) user.getId());
        filmService.putLike(film2.getId(), (int) user2.getId());
        filmService.putLike(film2.getId(), (int) film.getId());
        List<Film> listPopular = filmService.getPopularFilm(10);

        assertThat(listPopular).asList().hasSize(2);
        assertThat(listPopular).asList().startsWith(filmStorage.getById(film2.getId()));
        assertThat(listPopular).asList().contains(filmStorage.getById(film.getId()));
        assertThat(Optional.of(listPopular.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "sdfsf"));
        assertThat(Optional.of(listPopular.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "sdfsf"));
    }

    @Test
    public void getPopularFilm() {
        user = userStorage.addUser(user);
        user2 = userStorage.addUser(user2);
        film = filmStorage.addFilm(film);
        film2 = filmStorage.addFilm(film2);

        filmService.putLike(film.getId(), (int) user.getId());
        filmService.putLike(film2.getId(), (int) user2.getId());
        filmService.putLike(film2.getId(), (int) film.getId());
        List<Film> listPopular = filmService.getPopularFilm(1);

        assertThat(listPopular).asList().hasSize(1);
        assertThat(listPopular).asList().startsWith(filmStorage.getById(film2.getId()));
        assertThat(Optional.of(listPopular.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "sdfsf"));
    }

}
