package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private final String path = "/films";

    @Test
    public void postFilm() {
        final Film film = new Film(0, "sdfsf", "sfsadfasdf",
                LocalDate.of(2017, 12, 5), 100);
        final HttpEntity<Film> request = new HttpEntity<>(film);
        final ResponseEntity<Film> response = restTemplate.postForEntity(path, request, Film.class);
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);
        assertEquals(getResponse.getBody().size(), 1, "Произошла ошибка");
        film.setId(1);
        assertEquals(response.getBody(), film, "неверный id");
    }

    @Test
    public void putFilmUnknown() {
        final Film film = new Film(0, "adsfasdfasdfe", "adsfafdasdf",
                LocalDate.of(2017, 12, 5), 100);
        final HttpEntity<Film> request = new HttpEntity<>(film);
        final ResponseEntity<Film> response = restTemplate.postForEntity(path, request, Film.class);
        final Film putFilm = new Film(23523623, "adfewqrqwer", "авпвапывапвыапe",
                LocalDate.of(2000, 12, 5), 150);
        final HttpEntity<Film> newRequest = new HttpEntity<>(putFilm);
        final ResponseEntity<Film> newResponse = restTemplate.exchange(path, HttpMethod.PUT, newRequest, Film.class);
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);

        assertEquals(getResponse.getBody().size(), 1, "Произошла ошибка");
        assertEquals(newResponse.getStatusCodeValue(), 500, "Произошла ошибка");
    }

    @Test
    public void postFilmNull() {
        final Film film = new Film(0, null, "adsfafdasdf",
                LocalDate.of(2017, 12, 5), 100);
        final HttpEntity<Film> request = new HttpEntity<>(film);
        final ResponseEntity<Film> response = restTemplate.postForEntity(path, request, Film.class);
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);

        assertEquals(getResponse.getBody().size(), 0, "Произошла ошибка");
        assertEquals(response.getStatusCodeValue(), 400, "ошибка сервера");
    }


    @Test
    public void postFilmDate() {
        final Film film = new Film(0, "sdfadsfs", "adsfafdasdf",
                LocalDate.of(1893, 12, 28), 100);
        final HttpEntity<Film> request = new HttpEntity<>(film);
        final ResponseEntity<Film> response = restTemplate.postForEntity(path, request, Film.class);
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);

        assertEquals(getResponse.getBody().size(), 0, "Произошла ошибка");

    }

    @Test
    public void postFilmDateRelease() {
        final Film film = new Film(0, "sdfadsfs", "adsfafdasdf",
                LocalDate.of(3015, 12, 5), 100);
        final HttpEntity<Film> request = new HttpEntity<>(film);
        final ResponseEntity<Film> response = restTemplate.postForEntity(path, request, Film.class);
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);

        assertEquals(getResponse.getBody().size(), 1, "Произошла ошибка");
        film.setId(1);
        assertEquals(response.getBody(), film, "неверный id");
    }

    @Test
    public void postFilmFailDurationNegative() {
        final Film film = new Film(0, "sdfadsfs", "adsfafdasdf",
                LocalDate.of(1995, 12, 28), -100);
        final HttpEntity<Film> request = new HttpEntity<>(film);
        final ResponseEntity<Film> response = restTemplate.postForEntity(path, request, Film.class);
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);

        assertEquals(getResponse.getBody().size(), 0, "Произошла ошибка");
        assertEquals(response.getStatusCodeValue(), 400, "ошибка сервера");
    }


    @Test
    public void putFilmNormal() {
        final Film film = new Film(0, "sdfadsfs", "adsfafdasdf",
                LocalDate.of(2017, 12, 5), 100);
        final HttpEntity<Film> request = new HttpEntity<>(film);
        final ResponseEntity<Film> response = restTemplate.postForEntity(path, request, Film.class);
        final Film putFilm = new Film(1, "7 element", "Esche kruche",
                LocalDate.of(2000, 12, 5), 150);
        final HttpEntity<Film> newRequest = new HttpEntity<>(putFilm);
        final ResponseEntity<Film> newResponse = restTemplate.exchange(path, HttpMethod.PUT, newRequest, Film.class);
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);

        assertEquals(getResponse.getBody().size(), 1, "Произошла ошибка");
        assertEquals(newResponse.getBody(), putFilm, "Произошла ошибка");
    }


    @Test
    public void postFilmDurationZero() {
        final Film film = new Film(0, "sdfadsfs", "adsfafdasdf",
                LocalDate.of(1995, 12, 28), 0);
        final HttpEntity<Film> request = new HttpEntity<>(film);
        final ResponseEntity<Film> response = restTemplate.postForEntity(path, request, Film.class);
        final ResponseEntity<ArrayList> getResponse = restTemplate.getForEntity(path, ArrayList.class);

        assertEquals(getResponse.getBody().size(), 0, "Произошла ошибка");
        assertEquals(response.getStatusCodeValue(), 400, "ошибка сервера");
    }

}