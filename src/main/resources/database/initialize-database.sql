-- DROP TABLE IF EXISTS reservation;
-- DROP TABLE IF EXISTS offer;
-- DROP TABLE IF EXISTS boat;
-- DROP TABLE IF EXISTS "app_user";

CREATE TABLE IF NOT EXISTS "app_user"
(
    id                  SERIAL PRIMARY KEY,
    username            VARCHAR(255) NOT NULL UNIQUE,
    password            VARCHAR(255) NOT NULL,
    name                VARCHAR(255) NOT NULL,
    surname             VARCHAR(255) NOT NULL,
    phone_number        VARCHAR(255) NOT NULL,
    email               VARCHAR(255) NOT NULL,
    is_company          BOOLEAN      NOT NULL,
    is_confirmed        BOOLEAN      NOT NULL,
    confirmation_code   VARCHAR(255),
    reset_password_code VARCHAR(255),
    company_name        VARCHAR(255),
    TIN                 VARCHAR(255),
    address             VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS app_user_roles
(
    user_id INT NOT NULL,
    role    VARCHAR(255) NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES app_user (id)
            ON DELETE CASCADE,
    CONSTRAINT app_user_roles_pkey
        PRIMARY KEY (user_id, role)
);


CREATE TABLE IF NOT EXISTS boat
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(255)                                         NOT NULL,
    type          VARCHAR(255)                                         NOT NULL,
    model         VARCHAR(255)                                         NOT NULL,
    max_headcount INTEGER                                              NOT NULL,
    cabins_number INTEGER                                              NOT NULL,
    prod_year     INTEGER                                              NOT NULL,
    engine_power  INTEGER                                              NOT NULL,
    owner_id      INTEGER REFERENCES "app_user" (id) ON DELETE CASCADE NOT NULL,
    image         BYTEA,
    image_name    VARCHAR(255)

);

CREATE TABLE IF NOT EXISTS offer
(
    id          SERIAL PRIMARY KEY,
    boat_id     INTEGER REFERENCES boat (id) ON DELETE CASCADE,
    owner_id    INTEGER REFERENCES "app_user" (id) ON DELETE CASCADE,
    port        VARCHAR(255) NOT NULL,
    price       INTEGER      NOT NULL,
    deposit     INTEGER      NOT NULL,
    start_date  DATE         NOT NULL,
    end_date    DATE         NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS reservation
(
    id          SERIAL PRIMARY KEY,
    offer_id    INTEGER REFERENCES offer (id) ON DELETE CASCADE,
    landlord_id INTEGER REFERENCES "app_user" (id) ON DELETE CASCADE,
    tenant_id   INTEGER REFERENCES "app_user" (id) ON DELETE CASCADE
);
