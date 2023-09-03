package ru.yandex.practicum.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> userMap = new HashMap<>();
    private int idUser = 1;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
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
    public User getUserById(int userId) {
        return userMap.get(userId);
    }

    @Override
    public void updateUserFromId(int userId, User user) {
        if (userMap.containsKey(user.getId())) {
            userMap.put(userId, user);
        } else {
            throw new NotFoundException();
        }
    }
}
