package ru.yandex.practicum.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserStorage;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> userMap = new HashMap<>();
    private int idUser = 1;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User addUser(User user) {
        user.setId(idUser++);
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            return user;
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public User getUserById(long userId) {
        return userMap.get(userId);
    }

    @Override
    public void updateUserFromId(long userId, User user) {
        if (userMap.containsKey(user.getId())) {
            userMap.put(userId, user);
        } else {
            throw new NotFoundException();
        }
    }
}
