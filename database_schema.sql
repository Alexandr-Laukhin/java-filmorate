CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255),
    birthday DATE
);

CREATE TABLE mpa_ratings (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(10) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE genres (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE films (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL CHECK (duration > 0),
    mpa_id INTEGER,
    FOREIGN KEY (mpa_id) REFERENCES mpa_ratings(id)
);

CREATE TABLE film_genres (
    film_id INTEGER,
    genre_id INTEGER,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);

CREATE TABLE likes (
    film_id INTEGER,
    user_id INTEGER,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE friendships (
    user_id INTEGER,
    friend_id INTEGER,
    status ENUM('UNCONFIRMED', 'CONFIRMED') NOT NULL DEFAULT 'UNCONFIRMED',
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE,
    CHECK (user_id != friend_id)
);

INSERT INTO mpa_ratings (code, description) VALUES
('G', 'у фильма нет возрастных ограничений'),
('PG', 'детям рекомендуется смотреть фильм с родителями'),
('PG-13', 'детям до 13 лет просмотр не желателен'),
('R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
('NC-17', 'лицам до 18 лет просмотр запрещён');

INSERT INTO genres (name) VALUES
('Комедия'),
('Драма'),
('Мультфильм'),
('Триллер'),
('Документальный'),
('Боевик');

CREATE INDEX idx_films_mpa_id ON films(mpa_id);
CREATE INDEX idx_films_release_date ON films(release_date);
CREATE INDEX idx_film_genres_film_id ON film_genres(film_id);
CREATE INDEX idx_film_genres_genre_id ON film_genres(genre_id);
CREATE INDEX idx_likes_film_id ON likes(film_id);
CREATE INDEX idx_likes_user_id ON likes(user_id);
CREATE INDEX idx_friendships_user_id ON friendships(user_id);
CREATE INDEX idx_friendships_friend_id ON friendships(friend_id);
CREATE INDEX idx_friendships_status ON friendships(status);