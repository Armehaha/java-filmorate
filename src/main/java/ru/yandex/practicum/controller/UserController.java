package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> integerUserMap = new LinkedHashMap<>();
    private int idUser = 1;

    @GetMapping
    public List<User> getUserList() {
        log.info("получение пользователей");
        return new ArrayList<>(integerUserMap.values());
    }

    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(idUser++);
        integerUserMap.put(user.getId(), user);
        log.info("добавление пользователя");

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        integerUserMap.put(user.getId(), user);
        log.info("изменение пользователя");
        return user;

    }
}
