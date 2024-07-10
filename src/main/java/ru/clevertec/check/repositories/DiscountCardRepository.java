package ru.clevertec.check.repositories;


import ru.clevertec.check.models.DiscountCard;

import java.sql.SQLException;
import java.util.Optional;

/**
 * The interface defines operations for working with discount cards
 */
public interface DiscountCardRepository {
    /**
     * Getting a discount card by number
     *
     * @param number - the number of the required card
     * @return an Optional object containing a discount card
     */
    Optional<DiscountCard> getDiscountCardByNumber(String number) throws SQLException;

    Optional<DiscountCard> getDiscountCardById(int id) throws SQLException;

    void addDiscountCard(DiscountCard discountCard) throws SQLException;

    void updateDiscountCard(int id, DiscountCard discountCard) throws SQLException;

    void deleteDiscountCard(int id) throws SQLException;
}
