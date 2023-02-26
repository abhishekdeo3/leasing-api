# leasing-api

## Steps to Run

```
gradle clean build 
docker compose up -d
java -jar build/libs/leasing-api-0.0.1-SNAPSHOT.jar
```
### Build with Jacoco Test Coverage and Dependency check Report
```
./gradlew clean build jacocoTestCoverageVerification dependencyCheckAnalyze
```

### MySQL Database Available at:
```
http://localhost:8180/?server=db&username=user&db=allane
```
### Swagger UI

```
User: allane
Password: allane

http://localhost:8091/swagger-ui/index.html
```

### Sample CRUD Requests
```
curl --location --request GET 'localhost:8091/contract' \
--header 'Authorization: Basic YWxsYW5lOmFsbGFuZQ==' \
--header 'Cookie: JSESSIONID=3F458C69AE646C40CC3DFD6DA6844988'
```

```
{
    "contracts": [
        {
            "contract_number": 3000,
            "monthly_rate": 500.89,
            "valid_from": "2023-01-01",
            "valid_until": "2026-12-31",
            "customer": {
                "customer_id": 1000,
                "first_name": "Decker",
                "last_name": "John",
                "birth_date": "1991-01-10"
            },
            "vehicle": {
                "vehicle_id": 20000,
                "brand": "Audi",
                "model": "A3",
                "model_year": 2013,
                "vehicle_identification_number": null,
                "price": 34893.89
            }
        },
        {
            "contract_number": 3001,
            "monthly_rate": 234.89,
            "valid_from": "2023-01-01",
            "valid_until": "2026-12-31",
            "customer": {
                "customer_id": 1001,
                "first_name": "Christian",
                "last_name": "Patric",
                "birth_date": "1992-02-11"
            },
            "vehicle": {
                "vehicle_id": 20001,
                "brand": "BMW",
                "model": "X3",
                "model_year": 2014,
                "vehicle_identification_number": "M123456D",
                "price": 52526.98
            }
        },
        {
            "contract_number": 3002,
            "monthly_rate": 678.45,
            "valid_from": "2023-01-01",
            "valid_until": "2026-12-31",
            "customer": {
                "customer_id": 1002,
                "first_name": "Thomas",
                "last_name": "Peter",
                "birth_date": "1993-03-12"
            },
            "vehicle": {
                "vehicle_id": 20002,
                "brand": "Hundai",
                "model": "i10",
                "model_year": 2015,
                "vehicle_identification_number": "DA3456Z",
                "price": 50883.31
            }
        },
        {
            "contract_number": 3004,
            "monthly_rate": 455.67,
            "valid_from": "2023-01-01",
            "valid_until": "2026-12-31",
            "customer": {
                "customer_id": 1003,
                "first_name": "James",
                "last_name": "Sebastian",
                "birth_date": "1994-04-13"
            },
            "vehicle": {
                "vehicle_id": 20003,
                "brand": "KIA",
                "model": "Seltos",
                "model_year": 2016,
                "vehicle_identification_number": "F19876T",
                "price": 49583.32
            }
        },
        {
            "contract_number": 3005,
            "monthly_rate": 897.67,
            "valid_from": "2023-01-01",
            "valid_until": "2026-12-31",
            "customer": {
                "customer_id": 1004,
                "first_name": "Son",
                "last_name": "Michael",
                "birth_date": "1995-05-14"
            },
            "vehicle": {
                "vehicle_id": 20004,
                "brand": "Skoda",
                "model": "Octivia",
                "model_year": 2017,
                "vehicle_identification_number": "W56479K",
                "price": 51183.34
            }
        },
        {
            "contract_number": 3006,
            "monthly_rate": 345.67,
            "valid_from": "2023-01-01",
            "valid_until": "2026-12-31",
            "customer": {
                "customer_id": 1000,
                "first_name": "Decker",
                "last_name": "John",
                "birth_date": "1991-01-10"
            },
            "vehicle": {
                "vehicle_id": 20005,
                "brand": "Suzuki",
                "model": "Celerio",
                "model_year": 2018,
                "vehicle_identification_number": "G234567U",
                "price": 51099.98
            }
        },
        {
            "contract_number": 3007,
            "monthly_rate": 124.67,
            "valid_from": "2023-01-01",
            "valid_until": "2026-12-31",
            "customer": {
                "customer_id": 1002,
                "first_name": "Thomas",
                "last_name": "Peter",
                "birth_date": "1993-03-12"
            },
            "vehicle": {
                "vehicle_id": 20006,
                "brand": "Volkswagen",
                "model": "Polo",
                "model_year": 2019,
                "vehicle_identification_number": "B3456789E",
                "price": 48137.54
            }
        }
    ]
}
```