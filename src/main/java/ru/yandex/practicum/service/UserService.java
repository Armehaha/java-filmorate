package ru.yandex.practicum.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Getter
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;


    public User getUserById(int userId) {
        if (userStorage.getUserById(userId) != null) {
            return userStorage.getUserById(userId);
        } else {
            throw new NoSuchElementException();
        }
    }

    public void putFriend(int userId, int friendId) {
        if (userStorage.getUserById(userId) == null) {
            throw new NoSuchElementException();
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new NoSuchElementException();
        }
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        if (user.getFriends().contains(friendId)) {
            throw new IllegalArgumentException();
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);


        userStorage.updateUserFromId(userId, user);
        userStorage.updateUserFromId(friendId, friend);
    }

    public void deleteFriend(int userId, int friendId) {
        if (userStorage.getUserById(userId) == null) {
            throw new NoSuchElementException();
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new NoSuchElementException();
        }
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

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
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(userId).getFriends() == null) {
            throw new NoSuchElementException();
        }
        if (userStorage.getUserById(otherId) == null || userStorage.getUserById(otherId).getFriends() == null) {
            throw new NoSuchElementException();
        }
        final List<User> mutualFriends = new ArrayList<>();
        final User user = userStorage.getUserById(userId);
        final User other = userStorage.getUserById(otherId);

        for (Integer friend : user.getFriends()) {
            if (other.getFriends().contains(friend)) {
                mutualFriends.add(userStorage.getUserById(friend));
            }
        }
        return mutualFriends;
    }

    public List<User> getFriends(int userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException();
        }
        final List<User> friends = new ArrayList<>();
        final User user = userStorage.getUserById(userId);

        for (Integer friend : user.getFriends()) {
            friends.add(userStorage.getUserById(friend));
        }
        return friends;
    }
}
