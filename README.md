### ER-диаграмма

```mermaid
erDiagram
    USERS {
        int id PK
        string email UK
        string login UK
        string name
        date birthday
    }
    
    FILMS {
        int id PK
        string name
        string description
        date release_date
        int duration
        int mpa_id FK
    }
    
    GENRES {
        int id PK
        string name UK
    }
    
    MPA_RATINGS {
        int id PK
        string code UK
        string description
    }
    
    FILM_GENRES {
        int film_id FK
        int genre_id FK
    }
    
    LIKES {
        int film_id FK
        int user_id FK
    }
    
    FRIENDSHIPS {
        int user_id FK
        int friend_id FK
        string status
    }
    
    USERS ||--o{ LIKES : "ставит лайки"
    FILMS ||--o{ LIKES : "получает лайки"
    
    USERS ||--o{ FRIENDSHIPS : "инициирует дружбу"
    USERS ||--o{ FRIENDSHIPS : "принимает дружбу"
    
    FILMS ||--o{ FILM_GENRES : "имеет жанры"
    GENRES ||--o{ FILM_GENRES : "принадлежит фильмам"
    
    MPA_RATINGS ||--o{ FILMS : "классифицирует"
```

### Описание таблиц

#### USERS
Хранит информацию о пользователях системы.
- `id` - первичный ключ
- `email` - уникальный email пользователя
- `login` - уникальный логин пользователя
- `name` - имя пользователя (может быть пустым, тогда используется login)
- `birthday` - дата рождения

#### FILMS
Хранит информацию о фильмах.
- `id` - первичный ключ
- `name` - название фильма
- `description` - описание фильма (максимум 200 символов)
- `release_date` - дата релиза (не раньше 28.12.1895)
- `duration` - продолжительность в минутах
- `mpa_id` - внешний ключ на таблицу MPA_RATINGS

#### GENRES
Справочная таблица жанров фильмов.
- `id` - первичный ключ
- `name` - название жанра (Комедия, Драма, Мультфильм, Триллер, Документальный, Боевик)

#### MPA_RATINGS
Справочная таблица рейтингов MPA.
- `id` - первичный ключ
- `code` - код рейтинга (G, PG, PG-13, R, NC-17)
- `description` - описание рейтинга

#### FILM_GENRES
Связующая таблица для связи многие-ко-многим между фильмами и жанрами.
- `film_id` - внешний ключ на таблицу FILMS
- `genre_id` - внешний ключ на таблицу GENRES
- Составной первичный ключ (film_id, genre_id)

#### LIKES
Связующая таблица для связи многие-ко-многим между пользователями и фильмами (лайки).
- `film_id` - внешний ключ на таблицу FILMS
- `user_id` - внешний ключ на таблицу USERS
- Составной первичный ключ (film_id, user_id)

#### FRIENDSHIPS
Связующая таблица для связи многие-ко-многим между пользователями (дружба).
- `user_id` - внешний ключ на таблицу USERS (инициатор дружбы)
- `friend_id` - внешний ключ на таблицу USERS (получатель запроса)
- `status` - статус дружбы (UNCONFIRMED, CONFIRMED)
- Составной первичный ключ (user_id, friend_id)