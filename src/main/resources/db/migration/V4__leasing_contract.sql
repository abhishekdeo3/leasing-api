DROP TABLE IF EXISTS leasing_contract CASCADE;

CREATE TABLE IF NOT EXISTS leasing_contract
(

    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    monthly_rate DOUBLE PRECISION(7, 2) NOT NULL,
    valid_from   DATE                   NOT NULL,
    valid_until  DATE                   NOT NULL,

    customer_id  BIGINT,
    vehicle_id   BIGINT UNIQUE,

    CONSTRAINT leasing_contract__customer__fkey FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT leasing_contract__vehicle__fkey FOREIGN KEY (vehicle_id) REFERENCES vehicle (id)
);

create index idx__leasing_contract__customer_id on leasing_contract (customer_id);
create index idx__leasing_contract__vehicle_id on leasing_contract (vehicle_id);