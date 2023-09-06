package ru.yandex.practicum.storage.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.FilmGenre;
import ru.yandex.practicum.storage.FilmStorage;
import ru.yandex.practicum.storage.MPAStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genre;
    private final MPAStorage mpa;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genre, MPAStorage mpa) {
        this.jdbcTemplate = jdbcTemplate;
        this.genre = genre;
        this.mpa = mpa;
    }

    private void validationFilm(long filmId) {
        String sqlQuery = "SELECT * FROM films WHERE film_id = ?";
        SqlRowSet film = jdbcTemplate.queryForRowSet(sqlQuery, filmId);

        if (!film.next()) {
            throw new NotFoundException("Такого фильма нет");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT film_id FROM films ORDER BY film_id";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> getById(rs.getInt("film_id")));
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        int key = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();

        if (film.getGenres() != null) {
            addFilmGenres(film, key);
        }
        return getById(key);
    }

    @Override
    public Film updateFilm(Film film) {
        validationFilm(film.getId());

        String sqlQuery = "UPDATE films SET name = ?, likes = ?, description = ?,  duration = ?, release_date = ?, " +
                "mpa_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getLikes(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        if (film.getGenres() != null) {
            updateGenre(film);
        } else {
            deleteGenre(film);
        }
        return getById(film.getId());
    }

    @Override
    public Film getById(long filmId) {
        validationFilm(filmId);

        String sqlFilm = "SELECT * FROM films WHERE film_id = ?";
        String sqlGenre = "SELECT genre_id FROM films_genre WHERE film_id = ?";
        String sqlUsersLike = "SELECT * FROM users_like WHERE film_id = ?";
        List<FilmGenre> genresFilm = jdbcTemplate.query(sqlGenre, (rs, rowNum) -> makeGenre(rs), filmId);
        List<Integer> usersLike = jdbcTemplate.query(sqlUsersLike, (rs, rowNum) -> makeUsersLike(rs), filmId);
        SqlRowSet sqlQuery = jdbcTemplate.queryForRowSet(sqlFilm, filmId);

        Film film = new Film();
        if (sqlQuery.next()) {
            film.setId(filmId);
            film.setName(sqlQuery.getString("name"));
            film.setLikes(sqlQuery.getInt("likes"));
            film.setDescription(sqlQuery.getString("description"));
            film.setDuration(sqlQuery.getInt("duration"));
            film.setReleaseDate(sqlQuery.getDate("release_date").toLocalDate());
            film.setMpa(mpa.getMPAById(sqlQuery.getInt("mpa_id")));
            film.setGenres(new HashSet<>(genresFilm));
            film.setUserLike(new HashSet<>(usersLike));
        }
        return film;
    }

    @Override
    public void updateFilmFromId(long filmId, Film film) {

        String sqlQuery = "UPDATE films SET name = ?, likes = ?, description = ?,  duration = ?, release_date = ?, " +
                "mpa_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getLikes(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                filmId);
    }

    private FilmGenre makeGenre(ResultSet rs) throws SQLException {
        return genre.getGenreById(rs.getInt("genre_id"));
    }

    private Integer makeUsersLike(ResultSet rs) throws SQLException {
        return rs.getInt("user_id");
    }

    private void updateGenre(Film film) {
        deleteGenre(film);
        addFilmGenres(film, film.getId());
    }

    private void deleteGenre(Film film) {
        String sqlDelete = "DELETE FROM films_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlDelete, film.getId());
    }

    private void addFilmGenres(Film film, long key) {
        Set<FilmGenre> filmGenres = new HashSet<>(film.getGenres());

        for (FilmGenre genre : filmGenres) {
            String sqlFilmGenres = "MERGE INTO films_genre (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlFilmGenres, key, genre.getId());
        }
    }
}
