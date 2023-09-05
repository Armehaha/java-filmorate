package ru.yandex.practicum.service;


import ru.yandex.practicum.model.User;

import java.util.List;

public interface UserServiceInt {
    List<User> getFriends(long userId);

    void putFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<User> getMutualFriends(int userId, int otherId);
}
