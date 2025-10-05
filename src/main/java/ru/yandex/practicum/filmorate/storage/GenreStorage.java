package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

<<<<<<< HEAD
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
=======
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
>>>>>>> 10448c9be0cb0a5825a0fa45bc444ab72bbe323e
public class GenreStorage {

    private final JdbcTemplate jdbcTemplate;

<<<<<<< HEAD
    private final RowMapper<Genre> genreRowMapper = (rs, rowNum) -> {
=======
    private final RowMapper<Genre> genreRowMapper = (ResultSet rs, int rowNum) -> {
>>>>>>> 10448c9be0cb0a5825a0fa45bc444ab72bbe323e
        Genre genre = new Genre();
        genre.setId(rs.getInt("id"));
        genre.setName(rs.getString("name"));
        return genre;
    };

    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM genres ORDER BY id";
        return jdbcTemplate.query(sql, genreRowMapper);
    }

    public Optional<Genre> getGenreById(int id) {
        String sql = "SELECT * FROM genres WHERE id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, genreRowMapper, id);
        return genres.isEmpty() ? Optional.empty() : Optional.of(genres.get(0));
    }
}
