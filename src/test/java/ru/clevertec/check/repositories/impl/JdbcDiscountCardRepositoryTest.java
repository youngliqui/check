package ru.clevertec.check.repositories.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.clevertec.check.config.DataSource;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.builder.DiscountCardBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcDiscountCardRepositoryTest {
    private JdbcDiscountCardRepository repository;
    private Connection connection;

    private final String databaseUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=1";
    private final String databaseUsername = "sa";
    private final String databasePassword = "";

    @BeforeAll
    void setUp() throws SQLException {
        DataSource.setUrl(databaseUrl);
        DataSource.setUsername(databaseUsername);
        DataSource.setPassword(databasePassword);

        connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE public.discount_card
                    (
                        id     BIGSERIAL PRIMARY KEY,
                        number INTEGER  NOT NULL UNIQUE,
                        amount SMALLINT NOT NULL CHECK (0 <= amount AND amount <= 100)
                    );
                    """);

            statement.execute("INSERT INTO public.discount_card (number, amount) VALUES ('1111', 3)");
            statement.execute("INSERT INTO public.discount_card (number, amount) VALUES ('2222', 4)");
        }

        repository = new JdbcDiscountCardRepository();
    }

    @AfterAll
    void tearDown() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS public.discount_card");
        } finally {
            connection.close();
        }
    }

    @Test
    void shouldGetDiscountCardById() throws SQLException {
        Optional<DiscountCard> retrievedCard = repository.getDiscountCardById(1);

        assertThat(retrievedCard).isPresent();
        assertAll(
                () -> assertThat(retrievedCard.get().getId()).isEqualTo(1),
                () -> assertThat(retrievedCard.get().getNumber()).isEqualTo("1111"),
                () -> assertThat(retrievedCard.get().getDiscount()).isEqualTo(3)
        );
    }

    @Test
    void shouldAddDiscountCard() throws SQLException {
        DiscountCard discountCard = new DiscountCardBuilder()
                .setId(3)
                .setNumber("3333")
                .setDiscount(2)
                .build();


        repository.addDiscountCard(discountCard);

        Optional<DiscountCard> retrievedCard = repository.getDiscountCardById(discountCard.getId());
        assertThat(retrievedCard).isPresent();
        assertThat(retrievedCard.get().getNumber()).isEqualTo(discountCard.getNumber());
    }

    @Test
    void shouldGetDiscountCardByNumber() throws SQLException {
        Optional<DiscountCard> retrievedCard = repository.getDiscountCardByNumber("1111");

        assertThat(retrievedCard).isPresent();
        assertAll(
                () -> assertThat(retrievedCard.get().getId()).isEqualTo(1),
                () -> assertThat(retrievedCard.get().getNumber()).isEqualTo("1111"),
                () -> assertThat(retrievedCard.get().getDiscount()).isEqualTo(3)
        );
    }

    @Test
    void shouldGetOptionalEmptyByNonExistentNumber() throws SQLException {
        var result = repository.getDiscountCardByNumber("10000");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldUpdateDiscountCard() throws SQLException {
        DiscountCard updatedCard = new DiscountCardBuilder()
                .setNumber("1488")
                .setDiscount(6)
                .build();

        repository.updateDiscountCard(2, updatedCard);

        Optional<DiscountCard> retrievedCard = repository.getDiscountCardById(2);

        assertThat(retrievedCard).isPresent();
        assertAll(
                () -> assertThat(retrievedCard.get().getId()).isEqualTo(2),
                () -> assertThat(retrievedCard.get().getNumber()).isEqualTo(updatedCard.getNumber()),
                () -> assertThat(retrievedCard.get().getDiscount()).isEqualTo(updatedCard.getDiscount())
        );
    }

    @Test
    void shouldDeleteDiscountCard() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO public.discount_card (id, number, amount) VALUES (5 ,'8988', 9)");
        }

        repository.deleteDiscountCard(5);

        Optional<DiscountCard> retrievedDiscountCard = repository.getDiscountCardById(5);
        assertThat(retrievedDiscountCard).isEmpty();
    }
}