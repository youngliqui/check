package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.exceptions.DiscountCardNotFoundException;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.builder.DiscountCardBuilder;
import ru.clevertec.check.repositories.DiscountCardRepository;
import ru.clevertec.check.services.DiscountCardService;
import ru.clevertec.check.services.impl.DiscountCardServiceImpl;

import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiscountCardServiceImplTest {
    @Mock
    private DiscountCardRepository discountCardRepository;

    private DiscountCardService discountCardService;

    @BeforeEach
    void setup() {
        discountCardService = new DiscountCardServiceImpl(discountCardRepository);
    }

    @Test
    @DisplayName("should get discount card by correct number")
    void shouldGetDiscountCardByNumber() throws SQLException, DiscountCardNotFoundException {
        DiscountCard expectedDiscountCard = new DiscountCardBuilder()
                .setId(1)
                .setNumber("1111")
                .setDiscount(3)
                .build();

        when(discountCardRepository.getDiscountCardByNumber(expectedDiscountCard.getNumber()))
                .thenReturn(Optional.of(expectedDiscountCard));

        DiscountCard actualDiscountCard = discountCardService.getDiscountCardByNumber(expectedDiscountCard.getNumber());

        assertAll(
                () -> {
                    assertThat(actualDiscountCard.getId()).isEqualTo(expectedDiscountCard.getId());
                    assertThat(actualDiscountCard.getNumber()).isEqualTo(expectedDiscountCard.getNumber());
                    assertThat(actualDiscountCard.getDiscount()).isEqualTo(expectedDiscountCard.getDiscount());
                }
        );
    }

    @Test
    @DisplayName("should throw exception when discount card was not found")
    void shouldThrowExceptionWhenDiscountCardNotFound() throws SQLException {
        when(discountCardRepository.getDiscountCardByNumber(anyString())).thenReturn(Optional.empty());

        assertThrows(
                DiscountCardNotFoundException.class,
                () -> discountCardService.getDiscountCardByNumber(anyString())
        );
    }

    @Test
    @DisplayName("should throw exception when repository throws sql exception")
    void shouldThrowSqlExceptionWhenRepositoryThrowsSqlException() throws SQLException {
        when(discountCardRepository.getDiscountCardByNumber(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> discountCardService.getDiscountCardByNumber(anyString()));
    }
}
