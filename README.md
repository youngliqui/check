# Check Generator
Приложение генерирует чек для магазина исходя из входных параметров
- Создано на Java 21
- Место хранения файлов `products.csv` и `discountCards.csv`: `./src/main/resources`
# Инструкция по запуску:
При использовании **Java 21**:
```
./gradlew shadowJar
```
```
java -jar .\build\libs\app.jar 1-1 2-12 discountCard=3333 balanceDebitCard=50 pathToFile=./products.csv saveToFile=./result_file.csv
```
Формат команды:

`java -jar .\build\libs\app.jar <items> discountCard=<cardNumber> balanceDebitCard=<balance> pathToFile=<pathToFile> saveToFile=<saveToFile>`

Расшифровка команды:
- `<items>` - (id-quantity): ID товара-Количество
- `<cardNumber>` - номер дисконтной карты
- `<balance>` - баланс дебетовой карты
- `<pathToFile>` - путь к файлу с товарами
- `<saveToFile>` - путь к файлу с результатом

### По команде создается файл `result_file.csv`:
```text
Date;Time
07.07.2024;13:53:24

QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL
1;Ice cream;1.19$;0.05$;1.19$
12;Bread;0.57$;0.68$;6.84$

DISCOUNT CARD;DISCOUNT PERCENTAGE
3333;4%

TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT
8.03$;0.73$;7.30$
```

### Дублирование чека в консоли:
![check_console.png](check_console.png)

### Проверка выбрасывания ошибок:
- команда: `java -jar .\build\libs\app.jar 1-1 2-12 discountCard=3333 balanceDebitCard=50 pathToFile=./products.csv`.
Результат, файл `result.csv`:
```text
ERROR
BAD REQUEST
```

- команда: `java -jar .\build\libs\app.jar 1-1 2-12 discountCard=3333 balanceDebitCard=50 saveToFile=./result_file.csv`.
Результат, файл `result_file.csv`:
```text
ERROR
BAD REQUEST
```