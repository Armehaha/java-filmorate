package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

    User getUserById(int userId);

    void updateUserFromId(int userId, User user);
}
