CREATE TABLE IF NOT EXISTS game
(
    id    INTEGER PRIMARY KEY AUTO_INCREMENT,
    name  VARCHAR(350) NOT NULL UNIQUE,
    price REAL
);

CREATE TABLE IF NOT EXISTS tier
(
    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(30) NOT NULL UNIQUE,
    percentage REAL
);

CREATE TABLE IF NOT EXISTS user
(
    id       INTEGER PRIMARY KEY AUTO_INCREMENT,
    login    VARCHAR(150) NOT NULL UNIQUE,
    balance  REAL,
    tier_id  INTEGER,
    password VARCHAR(150),
    FOREIGN KEY (tier_id) REFERENCES tier (id)
);

CREATE TABLE IF NOT EXISTS orders
(
    id             INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id        INTEGER,
    game_id        INTEGER,
    order_datetime DATETIME,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (game_id) REFERENCES game (id)
);

CREATE TABLE IF NOT EXISTS token
(
    id              INTEGER PRIMARY KEY AUTO_INCREMENT,
    token_value     NVARCHAR(150),
    user_id         INTEGER,
    expiration_date DATETIME,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS review
(
    id              INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id         INTEGER,
    game_id         INTEGER,
    rating          INTEGER,
    opinion         TEXT,
    review_datetime DATETIME,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (game_id) REFERENCES game (id)
);
