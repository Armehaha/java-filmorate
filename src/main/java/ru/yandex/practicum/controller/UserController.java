package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> userMap = new LinkedHashMap<>();
    private int idUser = 1;

    @GetMapping
    public List<User> getUserList() {
        log.info("получение пользователей");
        return List.of((User) userMap.values());
    }

    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(idUser++);
        userMap.put(user.getId(), user);
        log.info("добавление пользователя");

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            log.info("изменение пользователя");

            return user;
        } else {
            throw new ValidationException();
        }

    }
}
