package ru.yandex.practicum;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {


    @Autowired
    private TestRestTemplate restTemplate;
    private final String path = "/users";

    @Test
    void postUser() {
        final User user = new User(1, "ar@yandex.ru", "Armen Osipyan", "sdfasdfas",
                LocalDate.of(1969, 12, 1), new ArrayList<>());
        final HttpEntity<User> request = new HttpEntity<>(user);
        final ResponseEntity<User> response = restTemplate.postForEntity(path, request, User.class);
        final User newUser = response.getBody();
        System.out.println(user.toString());
        user.setId(1);
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);
        assertEquals(getResponse.getBody().size(), 1, "произошла ошибка");
        assertEquals(newUser, user);
    }


    @Test
    void postUserNullEmail() {
        final User user = new User(0, null, "Armen Osipyan", "sdfasdfas",
                LocalDate.of(1969, 12, 1), new ArrayList<>());
        final HttpEntity<User> request = new HttpEntity<>(user);
        final ResponseEntity<User> response = restTemplate.postForEntity(path, request, User.class);
        final int statusCode = response.getStatusCodeValue();
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);
        assertEquals(getResponse.getBody().size(), 0, "Добавление Юзера с неподходящим Email");
        assertEquals(statusCode, 400);
    }

    @Test
    void postUserFailEmail() {
        final User user = new User(0, "dhdfnfnf", "Armen Osipyan", "sdfasdfas",
                LocalDate.of(1969, 12, 1), new ArrayList<>());
        final HttpEntity<User> request = new HttpEntity<>(user);
        final ResponseEntity<User> response = restTemplate.postForEntity(path, request, User.class);
        final int statusCode = response.getStatusCodeValue();
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);
        assertEquals(getResponse.getBody().size(), 0, "Добавление Юзера с неподходящим Email");
        assertEquals(statusCode, 400);
    }

    @Test
    void postUserNoLogin() {
        final User user = new User(0, "ar@yandex.ru", "Armen Osipyan", " ",
                LocalDate.of(1969, 12, 1), new ArrayList<>());
        final HttpEntity<User> request = new HttpEntity<>(user);
        final ResponseEntity<User> response = restTemplate.postForEntity(path, request, User.class);
        final int statusCode = response.getStatusCodeValue();
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);
        assertEquals(getResponse.getBody().size(), 0, "Добавление Юзера с неподходящим Login");
        assertEquals(statusCode, 400);
    }


    @Test
    void postUserFailBirthDayFuture() {
        final User user = new User(0, "ar@yandex.ru", "Armen Osipyan", "sdfasdfas",
                LocalDate.of(2856, 12, 1), new ArrayList<>());
        final HttpEntity<User> request = new HttpEntity<>(user);
        final ResponseEntity<User> response = restTemplate.postForEntity(path, request, User.class);
        final int statusCode = response.getStatusCodeValue();
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);
        assertEquals(getResponse.getBody().size(), 0, "Добавление Юзера с неподходящим Birthday");
        assertEquals(statusCode, 400);
    }


    @Test
    void postUserBoudaryCasePast() throws IOException, InterruptedException {
        final User user = new User(0, "ar@yandex.ru", "sdfasdfas", "sdfasdfas",
                LocalDate.of(1900, 1, 2), new ArrayList<>());
        final HttpEntity<User> request = new HttpEntity<>(user);
        final ResponseEntity<User> response = restTemplate.postForEntity(path, request, User.class);
        final User newUser = response.getBody();
        user.setId(1);
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);
        assertEquals(getResponse.getBody().size(), 1, "произошла ошибка");
        assertEquals(newUser, user, "Неверное добавление пользователя");
    }

    @Test
    void postUserNoEmail() throws IOException, InterruptedException {
        final User user = new User(0, "", "Armen Osipyan", "sdfasdfas",
                LocalDate.of(1969, 12, 1), new ArrayList<>());
        final HttpEntity<User> request = new HttpEntity<>(user);
        final ResponseEntity<User> response = restTemplate.postForEntity(path, request, User.class);
        final int statusCode = response.getStatusCodeValue();
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);
        assertEquals(getResponse.getBody().size(), 0, "Добавление Юзера с неподходящим Email");
        assertEquals(statusCode, 400);
    }

    @Test
    void postUserBoudaryCasePresent() throws IOException, InterruptedException {
        final User user = new User(0, "ar@yandex.ru", "sdfasdfas", "sdfasdfas",
                LocalDate.now().minusDays(1), new ArrayList<>());
        final HttpEntity<User> request = new HttpEntity<>(user);
        final ResponseEntity<User> response = restTemplate.postForEntity(path, request, User.class);
        final User newUser = response.getBody();
        user.setId(1);
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);
        assertEquals(getResponse.getBody().size(), 1, "произошла ошибка");
        assertEquals(newUser, user, "Неверное добавление пользователя");
    }

    @Test
    void postUserNullName() {
        final User user = new User(0, "ar@yandex.ru", null, "sdfasdfas",
                LocalDate.of(1969, 12, 1), new ArrayList<>());
        final HttpEntity<User> request = new HttpEntity<>(user);
        final ResponseEntity<User> response = restTemplate.postForEntity(path, request, User.class);
        final User newUser = response.getBody();
        user.setId(1);
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);
        assertEquals(getResponse.getBody().size(), 1, "произошла ошибка");

    }

    @Test
    void postUserNoName() {
        final User user = new User(0, "ar@yandex.ru", " ", "sdfasdfas",
                LocalDate.of(1969, 12, 1), new ArrayList<>());
        final HttpEntity<User> request = new HttpEntity<>(user);
        final ResponseEntity<User> response = restTemplate.postForEntity(path, request, User.class);
        final User newUser = response.getBody();
        user.setId(1);
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);
        assertEquals(getResponse.getBody().size(), 1, "произошла ошибка");

    }

    @Test
    void putUserNormal() {
        final User user = new User(0, "ar@yandex.ru", "Armen Osipyan", "sdfasdfas",
                LocalDate.of(1969, 12, 1), new ArrayList<>());
        final HttpEntity<User> request = new HttpEntity<>(user);
        final ResponseEntity<User> response = restTemplate.postForEntity(path, request, User.class);
        final User putUser = new User(1, "ar@yandex.ru", "Alexandro Designer", "Qwerty",
                LocalDate.of(1976, 12, 1), new ArrayList<>());
        final HttpEntity<User> newRequest = new HttpEntity<>(putUser);
        final ResponseEntity<User> newResponse = restTemplate.exchange(path, HttpMethod.PUT, newRequest, User.class);
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);
        assertEquals(getResponse.getBody().size(), 1, "произошла ошибка");
        assertEquals(newResponse.getBody(), putUser, "произошла ошибка");
    }


    @Test
    public void postUserNull() {
        final User user = null;
        final HttpEntity<User> request = new HttpEntity<>(user);
        final ResponseEntity<User> response = restTemplate.postForEntity(path, request, User.class);
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);
        assertEquals(getResponse.getBody().size(), 0, "произошла ошибка");
        assertEquals(response.getStatusCodeValue(), 415, "Неверный ответ сервера");
    }
}
