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
    token_value VARCHAR(150),
    user_login  VARCHAR(150) PRIMARY KEY,
    FOREIGN KEY (user_login) REFERENCES user (login)
);

