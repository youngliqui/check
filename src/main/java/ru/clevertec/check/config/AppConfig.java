package ru.clevertec.check.config;

import java.math.BigDecimal;

public class AppConfig {
    private static String RESULT_FILE_PATH = "./result.csv";
    private static String PRODUCT_FILE_PATH = "./src/main/resources/products.csv";
    public static final String DISCOUNT_CARD_FILE_PATH = "./src/main/resources/discountCards.csv";
    public static final Integer WHOLESALE_COUNT = 5;
    public static final BigDecimal WHOLESALE_PERCENT = BigDecimal.TEN;
    public static final String CSV_DELIMITER = ";";

    public static void setResultFilePath(String path) {
        RESULT_FILE_PATH = path;
    }

    public static void setProductFilePath(String path) {
        PRODUCT_FILE_PATH = path;
    }

    public static String getResultFilePath() {
        return RESULT_FILE_PATH;
    }

    public static String getProductFilePath() {
        return PRODUCT_FILE_PATH;
    }
}
