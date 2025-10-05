package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

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
public class MpaRatingStorage {

    private final JdbcTemplate jdbcTemplate;

<<<<<<< HEAD
    private final RowMapper<MpaRating> mpaRowMapper = (rs, rowNum) -> {
        MpaRating rating = new MpaRating();
        rating.setId(rs.getInt("id"));
        rating.setCode(rs.getString("code"));
        rating.setDescription(rs.getString("description"));
        return rating;
=======
    private final RowMapper<MpaRating> mpaRatingRowMapper = (ResultSet rs, int rowNum) -> {
        MpaRating mpaRating = new MpaRating();
        mpaRating.setId(rs.getInt("id"));
        mpaRating.setCode(rs.getString("code"));
        mpaRating.setDescription(rs.getString("description"));
        return mpaRating;
>>>>>>> 10448c9be0cb0a5825a0fa45bc444ab72bbe323e
    };

    public List<MpaRating> getAllMpaRatings() {
        String sql = "SELECT * FROM mpa_ratings ORDER BY id";
<<<<<<< HEAD
        return jdbcTemplate.query(sql, mpaRowMapper);
=======
        return jdbcTemplate.query(sql, mpaRatingRowMapper);
>>>>>>> 10448c9be0cb0a5825a0fa45bc444ab72bbe323e
    }

    public Optional<MpaRating> getMpaRatingById(int id) {
        String sql = "SELECT * FROM mpa_ratings WHERE id = ?";
<<<<<<< HEAD
        List<MpaRating> ratings = jdbcTemplate.query(sql, mpaRowMapper, id);
=======
        List<MpaRating> ratings = jdbcTemplate.query(sql, mpaRatingRowMapper, id);
>>>>>>> 10448c9be0cb0a5825a0fa45bc444ab72bbe323e
        return ratings.isEmpty() ? Optional.empty() : Optional.of(ratings.get(0));
    }
}
