package ru.yandex.practicum.service;


import ru.yandex.practicum.model.User;

import java.util.List;

public interface UserServiceInt {
    List<User> getFriends(long userId);

    void putFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<User> getMutualFriends(int userId, int otherId);

    List<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

    User getUserById(int id);
}
