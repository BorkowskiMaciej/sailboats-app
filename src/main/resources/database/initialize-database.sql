CREATE TABLE IF NOT EXISTS przykladowa_tabela
(
    id    SERIAL PRIMARY KEY,
    nazwa VARCHAR(255) NOT NULL,
    opis  TEXT
);

CREATE TABLE IF NOT EXISTS boat
(
    id    bigint PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL
);