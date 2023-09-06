package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    @Email
    @NotBlank
    private String email;
    private String name;
    @NotBlank
    private String login;
    @Past
    private LocalDate birthday;
    private List<Integer> friends = new ArrayList<>();

    public User(long id, String email, String name, String login, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.login = login;
        this.birthday = birthday;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        if (name == null || name.isBlank()) {
            user.put("name", login);
        } else {
            user.put("name", name);
        }
        user.put("login", login);
        user.put("birthday", birthday);
        return user;
    }
}
