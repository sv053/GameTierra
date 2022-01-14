BEGIN
TRANSACTION;
CREATE TABLE IF NOT EXISTS "game"
(
    "gameId"
    INTEGER,
    "name"
    TEXT
    NOT
    NULL
    CHECK
(
    "name"
    !=
    ''
),
    "price" REAL CHECK
(
    "price" >
    0
    AND
    "price" <
    100000
),
    PRIMARY KEY
(
    "gameId" AUTOINCREMENT
)
    );
CREATE TABLE IF NOT EXISTS "tier"
(
    "tierId"
    INTEGER,
    "level"
    TEXT
    NOT
    NULL
    CHECK
(
    "level"
    !=
    ''
),
    "percentage" REAL CHECK
(
    "percentage" >
    0
    AND
    "percentage" <
    1
),
    PRIMARY KEY
(
    "tierId" AUTOINCREMENT
)
    );
CREATE TABLE IF NOT EXISTS "user"
(
    "userId"
    INTEGER,
    "login"
    TEXT
    NOT
    NULL
    CHECK
(
    "login"
    !=
    ''
),
    "balance" REAL CHECK
(
    "balance" >
    0
    AND
    "balance" <
    100000
),
    "tierId" INTEGER,
    PRIMARY KEY
(
    "userId" AUTOINCREMENT
),
    CONSTRAINT "tier_fk" FOREIGN KEY
(
    "tierId"
) REFERENCES "tier"
(
    "tierId"
) ON DELETE CASCADE
    );
CREATE TABLE IF NOT EXISTS "user_game"
(
    "user_gameId"
    INTEGER,
    "userId"
    INTEGER,
    "gameId"
    INTEGER,
    PRIMARY
    KEY
(
    "user_gameId"
    AUTOINCREMENT
),
    CONSTRAINT "user_fk" FOREIGN KEY
(
    "userId"
) REFERENCES "user"
(
    "userId"
) ON DELETE CASCADE,
    CONSTRAINT "game_fk" FOREIGN KEY
(
    "gameId"
) REFERENCES "game"
(
    "gameId"
)
  ON DELETE CASCADE
    );
COMMIT;
