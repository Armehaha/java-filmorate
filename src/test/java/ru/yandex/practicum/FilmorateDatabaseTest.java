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
import ru.yandex.practicum.service.db.UserServiceDb;
import ru.yandex.practicum.storage.FilmStorage;
import ru.yandex.practicum.storage.GenreDao;
import ru.yandex.practicum.storage.MPADao;
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
public class FilmorateDatabaseTest {

    private final UserStorage userStorage;
    private final UserServiceDb userService;
    private final FilmStorage filmStorage;
    private final FilmServiceDb filmService;
    private final MPADao mpaService;
    private final GenreDao genreService;
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
    public void addUserNormal() {
        user = userStorage.addUser(user);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(user.getId()));

        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", user.getId())
                                .hasFieldOrPropertyWithValue("email", "ar@yandex.ru")
                                .hasFieldOrPropertyWithValue("name", "Armen Osipyan")
                                .hasFieldOrPropertyWithValue("login", "sdfasdfas")
                                .hasFieldOrPropertyWithValue("birthday",
                                        LocalDate.of(1995, 12, 1)));
    }


    @Test
    public void updateUserNormal() {
        user3 = userStorage.addUser(user3);
        user2 = userStorage.updateUser(user3);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(user2.getId()));
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", user.getId())
                                .hasFieldOrPropertyWithValue("email", "ar3213123@yandex.ru")
                                .hasFieldOrPropertyWithValue("name", "Armen Osipyan")
                                .hasFieldOrPropertyWithValue("login", "sdfasdfasdsfsfd")
                                .hasFieldOrPropertyWithValue("birthday",
                                        LocalDate.of(2007, 12, 1)));
    }


    @Test
    public void putFriendNormal() {
        user = userStorage.addUser(user);
        user2 = userStorage.addUser(user2);
        userService.putFriend((int) (user.getId()), (int) user2.getId());

        List<User> listFriends = userService.getFriends(user.getId());

        assertThat(listFriends).asList().hasSize(1);
        assertThat(listFriends).asList().contains(userStorage.getUserById(user2.getId()));
        assertThat(Optional.of(listFriends.get(0)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("email", "ar14314@yandex.ru"));
    }


    @Test
    public void getCommonFriendsNormal() {
        user = userStorage.addUser(user);
        user2 = userStorage.addUser(user2);
        user3 = userStorage.addUser(user3);

        userService.putFriend((int) user.getId(), (int) user3.getId());
        userService.putFriend((int) user2.getId(), (int) user3.getId());
        List<User> mutualFriends = userService.getMutualFriends((int) user.getId(), (int) user2.getId());

        assertThat(mutualFriends).asList().hasSize(1);
        assertThat(mutualFriends).asList().contains(userStorage.getUserById(user3.getId()));
        assertThat(Optional.of(mutualFriends.get(0)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("name", "Armen Osipyan"));
    }

    @Test
    public void getListUsers() {
        user = userStorage.addUser(user);
        user2 = userStorage.addUser(user2);
        user3 = userStorage.addUser(user3);
        List<User> listUsers = userStorage.getAllUsers();

        assertThat(listUsers).asList().hasSize(3);
        assertThat(listUsers).asList().contains(userStorage.getUserById(user.getId()));
        assertThat(listUsers).asList().contains(userStorage.getUserById(user2.getId()));
        assertThat(listUsers).asList().contains(userStorage.getUserById(user3.getId()));
        assertThat(Optional.of(listUsers.get(0)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("login", "sdfasdfas"));
        assertThat(Optional.of(listUsers.get(1)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("login", "sdfasddsfsdffas"));
        assertThat(Optional.of(listUsers.get(2)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("login", "sdfasdfasdsfsfd"));
    }

    @Test
    public void getListUsersBlank() {
        List<User> listUsers = userStorage.getAllUsers();

        assertThat(listUsers).asList().hasSize(0);
        assertThat(listUsers).asList().isEmpty();
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