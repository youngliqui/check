package ru.clevertec.check.config;

import java.math.BigDecimal;

public class AppConfig {
    public static final String RESULT_FILE_DEFAULT_PATH = "./result.csv";
    public static final String PRODUCT_FILE_DEFAULT_PATH = "./src/main/resources/products.csv";
    private static String resultFilePath = RESULT_FILE_DEFAULT_PATH;
    private static String productFilePath = PRODUCT_FILE_DEFAULT_PATH;
    public static final String DISCOUNT_CARD_FILE_PATH = "./src/main/resources/discountCards.csv";
    public static final Integer WHOLESALE_COUNT = 5;
    public static final BigDecimal WHOLESALE_PERCENT = BigDecimal.TEN;
    public static final String CSV_DELIMITER = ";";

    public static void setResultFilePath(String path) {
        resultFilePath = path;
    }

    public static void setProductFilePath(String path) {
        productFilePath = path;
    }

    public static String getResultFilePath() {
        return resultFilePath;
    }

    public static String getProductFilePath() {
        return productFilePath;
    }
}
