package ru.yandex.practicum.storage.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserStorage;

import java.util.List;


@Component
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        user = validationName(user);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        long key = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();
        User newUser = getUserById(key);

        return newUser;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET email = ?, name = ?, login = ?, birthday = ? WHERE id = ?";
        user = validationName(user);

        validationIdUser(user.getId());
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        User newUser = getUserById(user.getId());

        return newUser;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM users";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> getUserById(rs.getLong("id")));
    }

    @Override
    public User getUserById(long userId) {
        User user = new User();
        String sqlQuery = "SELECT * FROM users WHERE id = ?";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sqlQuery, userId);

        if (userRow.next()) {
            user.setId(userRow.getLong("id"));
            user.setEmail(userRow.getString("email"));
            user.setName(userRow.getString("name"));
            user.setLogin(userRow.getString("login"));
            user.setBirthday(userRow.getDate("birthday").toLocalDate());

        } else {
            throw new NotFoundException("Пользователь не найден");
        }

        return user;
    }

    @Override
    public void updateUserFromId(long userId, User user) {

    }

    private void validationIdUser(long userId) {
        SqlRowSet sqlUser = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?", userId);

        if (!sqlUser.next()) {
            throw new NotFoundException("Пользователь с таким айди не найден");
        }
    }

    private User validationName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return user;
    }
}