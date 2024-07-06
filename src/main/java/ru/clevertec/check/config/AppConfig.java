package ru.clevertec.check.config;

import java.math.BigDecimal;

public class AppConfig {
    public static final String RESULT_FILE_PATH = "./result.csv";
    public static final String PRODUCT_FILE_PATH = "./src/main/resources/products.csv";
    public static final String DISCOUNT_CARD_FILE_PATH = "./src/main/resources/discountCards.csv";
    public static final Integer WHOLESALE_COUNT = 5;
    public static final BigDecimal WHOLESALE_PERCENT = BigDecimal.TEN;
    public static final String CSV_DELIMITER = ";";

}
