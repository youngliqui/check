package ru.clevertec.check.services;


import ru.clevertec.check.exceptions.DiscountCardNotFoundException;
import ru.clevertec.check.models.DiscountCard;

import java.sql.SQLException;

/**
 * The receipt that will be printed
 */
public interface DiscountCardService {
    /**
     * Getting a discount card by number
     *
     * @param number - number of the discount card
     * @return discount card
     * @throws DiscountCardNotFoundException - when the card was not found
     * @throws SQLException                  - when a database error occurs
     */
    DiscountCard getDiscountCardByNumber(String number) throws SQLException, DiscountCardNotFoundException;

    DiscountCard getDiscountCardById(int id) throws SQLException, DiscountCardNotFoundException;

    void addDiscountCard(DiscountCard discountCard) throws SQLException;

    void updateDiscountCardById(int id, DiscountCard discountCard) throws SQLException;

    void deleteDiscountCardById(int id) throws SQLException;
}
