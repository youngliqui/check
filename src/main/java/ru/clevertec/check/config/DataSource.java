package ru.clevertec.check.config;

public class DataSource {
    private static String url = System.getProperty("datasource.url");
    private static String username = System.getProperty("datasource.username");
    private static String password = System.getProperty("datasource.password");

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        DataSource.url = url;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        DataSource.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        DataSource.password = password;
    }
}
