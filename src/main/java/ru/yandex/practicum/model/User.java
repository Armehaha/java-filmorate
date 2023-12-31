package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class User {
    private int id;
    @Email
    @NotBlank
    private String email;
    private String name;
    @NotBlank
    private String login;
    @Past
    private LocalDate birthday;
    private final List<Integer> friends = new ArrayList<>();

}
