package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.datasource.url", havingValue = "jdbc:h2:file:./db/filmorate")
public class MpaRatingStorage {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<MpaRating> mpaRowMapper = (rs, rowNum) -> {
        MpaRating rating = new MpaRating();
        rating.setId(rs.getInt("id"));
        rating.setName(rs.getString("name"));
        rating.setDescription(rs.getString("description"));
        return rating;
    };

    public List<MpaRating> getAllMpaRatings() {
        String sql = "SELECT * FROM mpa_ratings ORDER BY id";
        return jdbcTemplate.query(sql, mpaRowMapper);
    }

    public Optional<MpaRating> getMpaRatingById(int id) {
        String sql = "SELECT * FROM mpa_ratings WHERE id = ?";
        List<MpaRating> ratings = jdbcTemplate.query(sql, mpaRowMapper, id);
        return ratings.isEmpty() ? Optional.empty() : Optional.of(ratings.get(0));
    }
}
