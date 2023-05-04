package ru.yandex.practicum.filmorate.storage.film.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


public class ValuesExtractor<T> implements ResultSetExtractor<Map<Long, Set<T>>> {

    private final RowMapper<T> rowMapper;

    public ValuesExtractor(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }


    @Override
    public Map<Long, Set<T>> extractData(ResultSet rs) throws SQLException, DataAccessException {

        Map<Long, Set<T>> valuesByFilmId = new HashMap<>();
        while (rs.next()) {
            var value = rowMapper.mapRow(rs, 1);
            var filmId = rs.getLong(1);
            if (valuesByFilmId.containsKey(filmId))
                valuesByFilmId.get(filmId).add(value);
            else {
                valuesByFilmId.put(filmId, new LinkedHashSet<>() {
                });
                if (value != null) {
                    valuesByFilmId.get(filmId).add(value);
                }
            }
        }
        return valuesByFilmId;
    }
}
