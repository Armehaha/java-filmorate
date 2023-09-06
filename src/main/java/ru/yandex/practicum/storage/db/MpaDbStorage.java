package ru.yandex.practicum.storage.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.FilmMPA;
import ru.yandex.practicum.storage.MPAStorage;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
@Primary
public class MpaDbStorage implements MPAStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<FilmMPA> getAllMPA() {
        String sqlQuery = "SELECT * FROM mpa";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMpa(rs));
    }

    private FilmMPA makeMpa(ResultSet rs) throws SQLException {
        FilmMPA filmMPA = new FilmMPA();
        filmMPA.setId(rs.getInt("mpa_id"));
        filmMPA.setName(rs.getString("title"));
        return filmMPA;
    }

    @Override
    public FilmMPA getMPAById(long mpaId) {
        String sqlQuery = "SELECT * FROM mpa WHERE mpa_id = ?";
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sqlQuery, mpaId);

        if (mpaRow.next()) {
            FilmMPA filmMPA = new FilmMPA();
            filmMPA.setId(mpaRow.getInt("mpa_id"));
            filmMPA.setName(mpaRow.getString("title"));
            return filmMPA;

        } else {
            throw new NotFoundException("МРА нет");
        }
    }
}
