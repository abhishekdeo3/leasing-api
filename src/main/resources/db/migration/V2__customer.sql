DROP TABLE IF EXISTS customer CASCADE;

CREATE TABLE IF NOT EXISTS customer
(

    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    last_name  VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    birthdate  DATE         NOT NULL
);