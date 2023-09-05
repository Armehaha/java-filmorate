package ru.yandex.practicum.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.FilmGenre;
import ru.yandex.practicum.storage.GenreDao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
@Primary
public class GenreImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<FilmGenre> getAllGenre() {
        String sqlQuery = "SELECT * FROM genre";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs));
    }

    private FilmGenre makeGenre(ResultSet rs) throws SQLException {
        FilmGenre filmGenre = new FilmGenre();
        filmGenre.setId(rs.getInt("genre_id"));
        filmGenre.setName(rs.getString("name"));
        return filmGenre;
    }

    @Override
    public FilmGenre getGenreById(long genreId) {
        String sqlQuery = "SELECT * FROM genre WHERE genre_id = ?";
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet(sqlQuery, genreId);

        if (genreRow.next()) {
            FilmGenre filmGenre = new FilmGenre();
            filmGenre.setId(genreRow.getInt("genre_id"));
            filmGenre.setName(genreRow.getString("name"));
            return filmGenre;
        } else {
            throw new NotFoundException("Жанра нет");
        }
    }
}
