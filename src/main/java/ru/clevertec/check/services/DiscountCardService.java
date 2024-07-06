package ru.clevertec.check.services;


import ru.clevertec.check.exceptions.DiscountCardNotFoundException;
import ru.clevertec.check.models.DiscountCard;

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
     */
    DiscountCard getDiscountCardByNumber(String number) throws DiscountCardNotFoundException;
}
