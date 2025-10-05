package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component("filmDbStorage")
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Film> filmRowMapper = (ResultSet rs, int rowNum) -> {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        Date releaseDate = rs.getDate("release_date");
        if (releaseDate != null) {
            film.setReleaseDate(releaseDate.toLocalDate());
        }
        film.setDuration(rs.getInt("duration"));
        
        int mpaId = rs.getInt("mpa_id");
        if (!rs.wasNull() && mpaId != 0) {
            MpaRating mpa = new MpaRating();
            mpa.setId(mpaId);
            film.setMpa(mpa);
        }
        
        return film;
    };

    @Override
    public Film createFilm(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, film.getReleaseDate() != null ? Date.valueOf(film.getReleaseDate()) : null);
            ps.setInt(4, film.getDuration());
            ps.setObject(5, film.getMpa() != null ? film.getMpa().getId() : null);
            return ps;
        }, keyHolder);

        int id = keyHolder.getKey().intValue();
        film.setId(id);
        
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            addGenresToFilm(film.getId(), film.getGenres());
        }
        
        log.info("Создан фильм с id: {}", id);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
        int rowsUpdated = jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                film.getReleaseDate() != null ? Date.valueOf(film.getReleaseDate()) : null,
                film.getDuration(), film.getMpa() != null ? film.getMpa().getId() : null, film.getId());

        if (rowsUpdated == 0) {
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }

        if (film.getGenres() != null) {
            removeGenresFromFilm(film.getId());
            if (!film.getGenres().isEmpty()) {
                addGenresToFilm(film.getId(), film.getGenres());
            }
        }

        log.info("Обновлен фильм с id: {}", film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT f.*, mr.code as mpa_code, mr.description as mpa_description " +
                "FROM films f LEFT JOIN mpa_ratings mr ON f.mpa_id = mr.id ORDER BY f.id";
        return jdbcTemplate.query(sql, this::mapFilmWithMpa);
    }

    @Override
    public Optional<Film> findFilmById(int id) {
        String sql = "SELECT f.*, mr.code as mpa_code, mr.description as mpa_description " +
                "FROM films f LEFT JOIN mpa_ratings mr ON f.mpa_id = mr.id WHERE f.id = ?";
        List<Film> films = jdbcTemplate.query(sql, this::mapFilmWithMpa, id);
        if (films.isEmpty()) {
            return Optional.empty();
        }
        
        Film film = films.get(0);
        film.setGenres(getGenresForFilm(id));
        return Optional.of(film);
    }

    @Override
    public void deleteFilm(int id) {
        String sql = "DELETE FROM films WHERE id = ?";
        int rowsDeleted = jdbcTemplate.update(sql, id);
        if (rowsDeleted == 0) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
        log.info("Удален фильм с id: {}", id);
    }

    private Film mapFilmWithMpa(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        Date releaseDate = rs.getDate("release_date");
        if (releaseDate != null) {
            film.setReleaseDate(releaseDate.toLocalDate());
        }
        film.setDuration(rs.getInt("duration"));
        
        String mpaCode = rs.getString("mpa_code");
        if (mpaCode != null) {
            MpaRating mpa = new MpaRating();
            mpa.setId(rs.getInt("mpa_id"));
            mpa.setCode(mpaCode);
            mpa.setDescription(rs.getString("mpa_description"));
            film.setMpa(mpa);
        }
        
        return film;
    }

    private void addGenresToFilm(int filmId, Set<Genre> genres) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : genres) {
            jdbcTemplate.update(sql, filmId, genre.getId());
        }
    }

    private void removeGenresFromFilm(int filmId) {
        String sql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    private Set<Genre> getGenresForFilm(int filmId) {
        String sql = "SELECT g.id, g.name FROM genres g " +
                "JOIN film_genres fg ON g.id = fg.genre_id WHERE fg.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("name"));
            return genre;
        }, filmId);
        return new HashSet<>(genres);
    }
}
