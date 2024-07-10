package repositories;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.builder.DiscountCardBuilder;
import ru.clevertec.check.repositories.DiscountCardRepository;
import ru.clevertec.check.repositories.impl.JdbcDiscountCardRepository;

import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.clevertec.check.config.DataSource.*;

public class JdbcDiscountCardRepositoryTest {
    private static final DiscountCard CORRECT_DISCOUNT_CARD = createCorrectDiscountCard();
    private final DiscountCardRepository discountCardRepository = new JdbcDiscountCardRepository();

    @BeforeAll
    static void init() {
        setUrl(TestDatabaseConnection.URL);
        setUsername(TestDatabaseConnection.USERNAME);
        setPassword(TestDatabaseConnection.PASSWORD);
    }

    @Test
    @DisplayName("should return correct discount card when number is valid")
    void getDiscountCardByCorrectNumber() {
        try {
            Optional<DiscountCard> currentCardOptional = discountCardRepository.getDiscountCardByNumber(
                    CORRECT_DISCOUNT_CARD.getNumber()
            );

            assertThat(currentCardOptional).isPresent();

            DiscountCard currentCard = currentCardOptional.get();

            assertAll(
                    () -> {
                        assertThat(currentCard.getNumber()).isEqualTo(CORRECT_DISCOUNT_CARD.getNumber());
                        assertThat(currentCard.getDiscount()).isEqualTo(CORRECT_DISCOUNT_CARD.getDiscount());
                        assertThat(currentCard.getId()).isEqualTo(CORRECT_DISCOUNT_CARD.getId());
                    }
            );

        } catch (SQLException e) {
            fail("Database error: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("should return empty list when number is incorrect")
    void getEmptyWhenNumberIsIncorrect() {
        String incorrectNumber = "-1";

        try {
            Optional<DiscountCard> discountCardOptional = discountCardRepository.getDiscountCardByNumber(incorrectNumber);

            assertThat(discountCardOptional).isEmpty();
        } catch (SQLException e) {
            fail("Database error: " + e.getMessage());
        }
    }

    private static DiscountCard createCorrectDiscountCard() {
        return new DiscountCardBuilder()
                .setId(3)
                .setDiscount(4)
                .setNumber("3333")
                .build();
    }
}
