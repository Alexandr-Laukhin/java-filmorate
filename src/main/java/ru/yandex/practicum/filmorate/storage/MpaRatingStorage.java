package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MpaRatingStorage {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<MpaRating> mpaRatingRowMapper = (ResultSet rs, int rowNum) -> {
        MpaRating mpaRating = new MpaRating();
        mpaRating.setId(rs.getInt("id"));
        mpaRating.setCode(rs.getString("code"));
        mpaRating.setDescription(rs.getString("description"));
        return mpaRating;
    };

    public List<MpaRating> getAllMpaRatings() {
        String sql = "SELECT * FROM mpa_ratings ORDER BY id";
        return jdbcTemplate.query(sql, mpaRatingRowMapper);
    }

    public Optional<MpaRating> getMpaRatingById(int id) {
        String sql = "SELECT * FROM mpa_ratings WHERE id = ?";
        List<MpaRating> ratings = jdbcTemplate.query(sql, mpaRatingRowMapper, id);
        return ratings.isEmpty() ? Optional.empty() : Optional.of(ratings.get(0));
    }
}
