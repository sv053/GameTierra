BEGIN TRANSACTION;

CREATE TABLE IF NOT EXISTS 'game'
(
    'id'                INTEGER,
    'name'              TEXT NOT NULL CHECK ('name' != ''),
    'price'             REAL,
    PRIMARY KEY ('id' AUTOINCREMENT)
);

CREATE TABLE IF NOT EXISTS 'tier'
(
    'id'                INTEGER,
    'level'             TEXT NOT NULL CHECK ('level' != ''),
    'percentage'        REAL,
    PRIMARY KEY ('id' AUTOINCREMENT)
);

CREATE TABLE IF NOT EXISTS 'user'
(
    'id'                INTEGER,
    'login'             TEXT NOT NULL CHECK ('login' != ''),
    'balance'           REAL,
    'tier_id'           INTEGER,
    PRIMARY KEY ('id' AUTOINCREMENT),
    CONSTRAINT 'tier_fk' FOREIGN KEY ('tier_id') REFERENCES 'tier' ('id')
);

CREATE TABLE IF NOT EXISTS 'orders'
(
    'id'                INTEGER,
    'user_id'           INTEGER,
    'game_id'           INTEGER,
    'order_datetime'    DATETIME_INTERVAL_CODE,
    PRIMARY KEY ('id' AUTOINCREMENT),
    CONSTRAINT 'user_fk' FOREIGN KEY ('user_id') REFERENCES 'user' ('id') ON DELETE CASCADE,
    CONSTRAINT 'game_fk' FOREIGN KEY ('game_id') REFERENCES 'game' ('id') ON DELETE CASCADE
);

COMMIT;



