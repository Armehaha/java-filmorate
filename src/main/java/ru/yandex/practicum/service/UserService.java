package ru.yandex.practicum.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Getter
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public User getUserById(int userId) {
        if (userStorage.getOneUser(userId) != null) {
            return userStorage.getOneUser(userId);
        } else {
            throw new NoSuchElementException();
        }
    }

    public void putFriend(int userId, int friendId) {
        if (userStorage.getOneUser(userId) == null) {
            throw new NoSuchElementException();
        }
        if (userStorage.getOneUser(friendId) == null) {
            throw new NoSuchElementException();
        }
        User user = userStorage.getOneUser(userId);
        User friend = userStorage.getOneUser(friendId);

        if (user.getFriends().contains(friendId)) {
            throw new IllegalArgumentException();
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);


        userStorage.updateUserFromId(userId, user);
        userStorage.updateUserFromId(friendId, friend);
    }

    public void deleteFriend(int userId, int friendId) {
        if (userStorage.getOneUser(userId) == null) {
            throw new NoSuchElementException();
        }
        if (userStorage.getOneUser(friendId) == null) {
            throw new NoSuchElementException();
        }
        User user = userStorage.getOneUser(userId);
        User friend = userStorage.getOneUser(friendId);

        if (!user.getFriends().contains(friendId)) {
            throw new IllegalArgumentException();
        }
        final List<Integer> userList = user.getFriends().stream()
                .filter(integer -> !integer.equals(friendId))
                .collect(Collectors.toList());
        final List<Integer> friendList = friend.getFriends().stream()
                .filter(integer -> !integer.equals(userId))
                .collect(Collectors.toList());
        user.getFriends().clear();
        friend.getFriends().clear();
        userList.forEach(integer -> user.getFriends().add(integer));
        friendList.forEach(integer -> friend.getFriends().add(integer));

        userStorage.updateUserFromId(userId, user);
        userStorage.updateUserFromId(friendId, friend);
    }

    public List<User> getMutualFriends(int userId, int otherId) {
        if (userStorage.getOneUser(userId) == null || userStorage.getOneUser(userId).getFriends() == null) {
            throw new NoSuchElementException();
        }
        if (userStorage.getOneUser(otherId) == null || userStorage.getOneUser(otherId).getFriends() == null) {
            throw new NoSuchElementException();
        }
        final List<User> mutualFriends = new ArrayList<>();
        final User user = userStorage.getOneUser(userId);
        final User other = userStorage.getOneUser(otherId);

        for (Integer friend : user.getFriends()) {
            if (other.getFriends().contains(friend)) {
                mutualFriends.add(userStorage.getOneUser(friend));
            }
        }
        return mutualFriends;
    }

    public List<User> getFriends(int userId) {
        if (userStorage.getOneUser(userId) == null) {
            throw new NoSuchElementException();
        }
        final List<User> friends = new ArrayList<>();
        final User user = userStorage.getOneUser(userId);

        for (Integer friend : user.getFriends()) {
            friends.add(userStorage.getOneUser(friend));
        }
        return friends;
    }
}
