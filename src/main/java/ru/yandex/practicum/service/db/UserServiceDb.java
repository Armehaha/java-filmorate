package ru.yandex.practicum.service.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.IUserService;
import ru.yandex.practicum.storage.UserStorage;


import java.util.*;

@Service
@RequiredArgsConstructor
@Primary
public class UserServiceDb implements IUserService {
    private final JdbcTemplate jdbc;
    private final UserStorage userStorage;

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User addUser(User user) {
        validationName(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        validationName(user);
        return userStorage.updateUser(user);
    }

    public User getUserById(int userId) {
        if (userStorage.getUserById(userId) != null) {
            return userStorage.getUserById(userId);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public List<User> getFriends(long userId) {
        validationIdUser(userId);

        String sqlQuery = "SELECT friend_id FROM user_friends WHERE user_id = ?";
        List<User> friends = jdbc.query(sqlQuery, (rs, rowNum) ->
                userStorage.getUserById(rs.getInt("friend_id")), userId);
        return friends;
    }

    @Override
    public void putFriend(int userId, int friendId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(friendId) == null) {
            throw new NotFoundException();
        }
        validationIdUser(userId);
        validationIdUser(friendId);
        if (isFriends(userId, friendId)) {
            String sqlQuery = "MERGE INTO user_friends (user_id, friend_id) VALUES (?, ?)";
            jdbc.update(sqlQuery, userId, friendId);
        } else {
            throw new IllegalArgumentException("Данный пользователь уже добавлен в друзья");
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        validationIdUser(userId);
        validationIdUser(friendId);
        if (!isFriends(userId, friendId)) {
            String sqlQuery = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";
            jdbc.update(sqlQuery, userId, friendId);
        } else {
            throw new IllegalArgumentException("Данный пользователь не ваш друг");
        }
    }

    private boolean isFriends(long userId, long friendId) {
        String sqlQuery = "SELECT * FROM user_friends WHERE user_id = ? AND friend_id = ?";
        SqlRowSet userFriends = jdbc.queryForRowSet(sqlQuery, userId, friendId);

        if (!userFriends.next()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<User> getMutualFriends(int userId, int otherId) {
        validationIdUser(userId);
        validationIdUser(otherId);

        String sqlQuery = "SELECT friend_id FROM user_friends WHERE user_id = ? AND  friend_id IN " +
                "(SELECT friend_id FROM user_friends WHERE user_id = ?)";
        Set<User> mutualFriends = new HashSet<>();
        SqlRowSet friends = jdbc.queryForRowSet(sqlQuery, userId, otherId);

        if (friends.next()) {
            mutualFriends.add(userStorage.getUserById(friends.getLong("friend_id")));
        }
        return new ArrayList<>(mutualFriends);
    }

    private void validationIdUser(long userId) {
        SqlRowSet sqlUser = jdbc.queryForRowSet("SELECT * FROM users WHERE id = ?", userId);

        if (!sqlUser.next()) {
            throw new NoSuchElementException("Пользователь с таким айди не найден");
        }
    }

    private User validationName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return user;
    }
}
