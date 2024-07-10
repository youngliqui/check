# Check Generator

Приложение генерирует чек для магазина исходя из входных параметров

- Создано на Java 21
- Gradle 8.5
- PostgreSQL
- Место хранения DDL/DML в файле `src/main/resources/data.sql`

# Инструкция по запуску:

По команде создается .war файл `/build/libs/clevertec-check.war`

```
./gradlew build
```

Необходимо также указать системные переменные:
```
datasource.url=jdbc:postgresql://localhost:5432/check
datasource.username=postgres
datasource.password=postgres
```

### Реазизовано RESTFUL-API(Servlet) приложение:
- Получение чека. `POST http://localhost:8080/check`:
```
{
    "products": [
        {
            "id": 7,
            "quantity": 1
        },
        {
            "id": 8,
            "quantity": 2
        }
                ],
    "discountCard": 1111,
    "balanceDebitCard": 100
}
```
Ответ:
{
"products": [
{
"id": 7,
"quantity": 1
},
{
"id": 8,
"quantity": 2
}
],
"discountCard": 1111,
"balanceDebitCard": 100
}

- Также работают все CRUD-операции с Товарами и Дисконтными картами(получение, удаление. добавление, изменение)

- При ошибках возвращаются соответвующие статус-коды.

### Функционал покрыт юнит-тестами:

![coverage.png](coverage.png)