package ru.clevertec.check.config;

import java.math.BigDecimal;

public class AppConfig {
    public static final String RESULT_FILE_DEFAULT_PATH = "./result.csv";
    private static String resultFilePath = RESULT_FILE_DEFAULT_PATH;
    public static final Integer WHOLESALE_COUNT = 5;
    public static final BigDecimal WHOLESALE_PERCENT = BigDecimal.TEN;

    public static void setResultFilePath(String path) {
        resultFilePath = path;
    }

    public static String getResultFilePath() {
        return resultFilePath;
    }
}
