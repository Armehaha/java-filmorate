package ru.yandex.practicum.service.db;

import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmServiceInt;
import ru.yandex.practicum.storage.FilmStorage;


import java.util.List;

@Service
public class FilmServiceDb implements FilmServiceInt {
    private final JdbcTemplate jdbcTemplate;
    @Getter
    private final FilmStorage filmStorage;

    public FilmServiceDb(JdbcTemplate jdbcTemplate, FilmStorage filmStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
    }

    @Override
    public void putLike(long filmId, int userId) {
        if (validationUserLike(filmId, userId)) {
            throw new NotFoundException("Данный пользователь уже поставил лайк этому фильму");
        }
        String sqlUserLike = "MERGE INTO users_like (film_id, user_id) VALUES (?, ?)";
        String sqlLikes = "UPDATE films SET likes = ? WHERE film_id = ?";
        Film film = filmStorage.getById(filmId);

        jdbcTemplate.update(sqlUserLike, filmId, userId);
        jdbcTemplate.update(sqlLikes, (film.getLikes() + 1), filmId);
    }

    @Override
    public void deleteLike(long filmId, int userId) {
        if (!validationUserLike(filmId, userId)) {
            throw new NotFoundException("Данный пользователь не ставил лайк этому фильму");
        }
        String sqlUserLike = "DELETE FROM users_like WHERE film_id = ? AND user_id = ?";
        String sqlLikes = "UPDATE films SET likes = ? WHERE film_id = ?";
        Film film = filmStorage.getById(filmId);

        jdbcTemplate.update(sqlUserLike, filmId, userId);
        jdbcTemplate.update(sqlLikes, (film.getLikes() - 1), filmId);
    }

    @Override
    public List<Film> getPopularFilm(int count) {
        String sqlQuery = "SELECT * FROM films ORDER BY likes DESC LIMIT ?;";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> filmStorage.getById(rs.getInt("film_id")),
                count);
    }

    private boolean validationUserLike(long filmId, int userId) {
        String sqlQuery = "SELECT * FROM users_like WHERE film_id =? AND user_id = ?";
        SqlRowSet userLike = jdbcTemplate.queryForRowSet(sqlQuery, filmId, userId);

        if (userLike.next()) {
            return true;
        } else {
            return false;
        }
    }
}
