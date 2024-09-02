package ru.clevertec.check.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.exceptions.DiscountCardNotFoundException;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.builder.DiscountCardBuilder;
import ru.clevertec.check.repositories.DiscountCardRepository;

import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscountCardServiceImplTest {
    @InjectMocks
    private DiscountCardServiceImpl discountCardService;
    @Spy
    private DiscountCardRepository discountCardRepository;

    private DiscountCard discountCard;

    @BeforeEach
    void setUp() {
        discountCard = createDiscountCard(1, "1111", 5);
    }

    @Test
    void shouldGetDiscountCardByNumber() throws SQLException, DiscountCardNotFoundException {
        when(discountCardRepository.getDiscountCardByNumber(discountCard.getNumber()))
                .thenReturn(Optional.of(discountCard));

        var result = discountCardService.getDiscountCardByNumber(discountCard.getNumber());

        assertThat(result).isEqualTo(discountCard);
    }

    @Test
    void shouldThrowExceptionWhenGetDiscountCardByNonExistentNumber() throws SQLException {
        String incorrectNumber = "dummy";

        when(discountCardRepository.getDiscountCardByNumber(incorrectNumber)).thenReturn(Optional.empty());

        assertThrows(DiscountCardNotFoundException.class,
                () -> discountCardService.getDiscountCardByNumber(incorrectNumber));
    }

    @Test
    void shouldGetDiscountCardById() throws SQLException, DiscountCardNotFoundException {
        when(discountCardRepository.getDiscountCardById(discountCard.getId()))
                .thenReturn(Optional.of(discountCard));

        var result = discountCardService.getDiscountCardById(discountCard.getId());

        assertThat(result).isEqualTo(discountCard);
    }

    @Test
    void shouldThrowExceptionWhenGetDiscountCardByNonExistentId() throws SQLException {
        int incorrectId = -10;

        when(discountCardRepository.getDiscountCardById(incorrectId)).thenReturn(Optional.empty());

        assertThrows(DiscountCardNotFoundException.class,
                () -> discountCardService.getDiscountCardById(incorrectId));
    }

    @Test
    void shouldAddDiscountCard() throws SQLException {
        discountCardService.addDiscountCard(discountCard);

        verify(discountCardRepository, times(1)).addDiscountCard(discountCard);
    }

    @Test
    void shouldUpdateDiscountCardById() throws SQLException {
        discountCardService.updateDiscountCardById(discountCard.getId(), discountCard);

        verify(discountCardRepository, times(1)).updateDiscountCard(discountCard.getId(), discountCard);
    }

    @Test
    void shouldDeleteDiscountCardById() throws SQLException {
        int id = 10;

        discountCardService.deleteDiscountCardById(id);

        verify(discountCardRepository, times(1)).deleteDiscountCard(id);
    }

    private static DiscountCard createDiscountCard(int id, String number, int discount) {
        return new DiscountCardBuilder()
                .setDiscount(discount)
                .setId(id)
                .setNumber(number)
                .build();
    }
}