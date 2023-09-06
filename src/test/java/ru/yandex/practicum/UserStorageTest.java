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
import ru.yandex.practicum.service.db.UserServiceDb;
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
public class UserStorageTest {
    private final UserStorage userStorage;
    private final UserServiceDb userService;
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
}
