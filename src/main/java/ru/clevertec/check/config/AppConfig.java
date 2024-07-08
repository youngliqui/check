package ru.clevertec.check.config;

import java.math.BigDecimal;

public class AppConfig {
    public static final String RESULT_FILE_DEFAULT_PATH = "./result.csv";
    private static String resultFilePath = RESULT_FILE_DEFAULT_PATH;

    public static void setResultFilePath(String path) {
        resultFilePath = path;
    }

    public static String getResultFilePath() {
        return resultFilePath;
    }

    public static final Integer WHOLESALE_COUNT = 5;
    public static final BigDecimal WHOLESALE_PERCENT = BigDecimal.TEN;

    private static String datasourceUrl;
    private static String datasourceUsername;
    private static String datasourcePassword;

    public static String getDatasourceUsername() {
        return datasourceUsername;
    }

    public static void setDatasourceUsername(String datasourceUsername) {
        AppConfig.datasourceUsername = datasourceUsername;
    }

    public static String getDatasourcePassword() {
        return datasourcePassword;
    }

    public static void setDatasourcePassword(String datasourcePassword) {
        AppConfig.datasourcePassword = datasourcePassword;
    }

    public static String getDatasourceUrl() {
        return datasourceUrl;
    }

    public static void setDatasourceUrl(String datasourceUrl) {
        AppConfig.datasourceUrl = datasourceUrl;
    }
}
