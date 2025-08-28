CREATE TABLE crediya_role
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE crediya_user
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(100)               NOT NULL,
    lastname       VARCHAR(100)               NOT NULL,
    id_role        BIGINT REFERENCES crediya_role (id) NOT NULL,
    email          VARCHAR(150)               NOT NULL UNIQUE,
    identification VARCHAR(20)                NOT NULL UNIQUE,
    salary_base    NUMERIC,
    birth_date     DATE,
    address         VARCHAR(255),
    phone          VARCHAR(20)
);