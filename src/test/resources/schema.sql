CREATE TABLE IF NOT EXISTS game
(
    id              INTEGER PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(350) NOT NULL,
    price           REAL
);

CREATE TABLE IF NOT EXISTS tier
(
    id              INTEGER PRIMARY KEY AUTO_INCREMENT,
    level           VARCHAR(30) NOT NULL,
    percentage      REAL
);

CREATE TABLE IF NOT EXISTS user
(
    id              INTEGER PRIMARY KEY AUTO_INCREMENT,
    login           VARCHAR(150) NOT NULL,
    balance         REAL,
    tier_id         INTEGER,
    FOREIGN KEY(tier_id) REFERENCES tier(id)
);

CREATE TABLE IF NOT EXISTS orders
(
    id              INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id         INTEGER,
    game_id         INTEGER,
    order_datetime  DATETIME,
    FOREIGN KEY(user_id) REFERENCES user(id),
    FOREIGN KEY(game_id) REFERENCES game(id)
);
