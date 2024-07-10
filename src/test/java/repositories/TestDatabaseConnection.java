package repositories;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestDatabaseConnection {
    static final String URL = "jdbc:postgresql://localhost:5432/check";
    static final String USERNAME = "postgres";
    static final String PASSWORD = "postgres";

    @Test
    void checkConnectionWithCorrectData() {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            assertThat(connection).isNotNull();
        } catch (SQLException e) {
            fail("Connection failed with correct data: " + e.getMessage());
        }
    }

    @Test
    void throwExceptionWhenConnectionWithIncorrectData() {
        String incorrectUrl = "dummy";
        String incorrect_username = "dummy";
        String incorrect_password = "dummy";

        assertThrows(
                SQLException.class, () -> DriverManager.getConnection(incorrectUrl, incorrect_username, incorrect_password)
        );
    }
}
