INSERT INTO allane.customer(id, last_name, first_name, birthdate)
VALUES (1000, 'John', 'Decker', '1991-01-10'),
       (1001, 'Patric', 'Christian', '1992-02-11'),
       (1002, 'Peter', 'Thomas', '1993-03-12'),
       (1003, 'Sebastian', 'James', '1994-04-13'),
       (1004, 'Michael', 'Son', '1995-05-14');

INSERT INTO allane.vehicle(id, brand, model, model_year, price, vehicle_identification_number)
VALUES (20000, 'Audi', 'A3', '2013', 34893.89, null),
       (20001, 'BMW', 'X3', '2014', 52526.98, 'M123456D'),
       (20002, 'Hundai', 'i10', '2015', 50883.31, 'DA3456Z'),
       (20003, 'KIA', 'Seltos', '2016', 49583.32, 'F19876T'),
       (20004, 'Skoda', 'Octivia', '2017', 51183.34, 'W56479K'),
       (20005, 'Suzuki', 'Celerio', '2018', 51099.98, 'G234567U'),
       (20006, 'Volkswagen', 'Polo', '2019', 48137.54, 'B3456789E');


INSERT INTO allane.leasing_contract(id, monthly_rate, valid_from, valid_until, customer_id, vehicle_id)
VALUES (3000, 500.89, '2023-01-01', '2026-12-31', 1000, 20000),
       (3001, 234.89, '2023-01-01', '2026-12-31', 1001, 20001),
       (3002, 678.45, '2023-01-01', '2026-12-31', 1002, 20002),
       (3004, 455.67, '2023-01-01', '2026-12-31', 1003, 20003),
       (3005, 897.67, '2023-01-01', '2026-12-31', 1004, 20004),
       (3006, 345.67, '2023-01-01', '2026-12-31', 1000, 20005),
       (3007, 124.67, '2023-01-01', '2026-12-31', 1002, 20006);