DROP TABLE IF EXISTS vehicle CASCADE;

CREATE TABLE IF NOT EXISTS vehicle
(

    id                            BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand                         VARCHAR(255)           NOT NULL,
    model                         VARCHAR(255)           NOT NULL,
    model_year                    YEAR                   NOT NULL,
    price                         DOUBLE PRECISION(7, 2) NOT NULL,
    vehicle_identification_number VARCHAR(255) UNIQUE
);
