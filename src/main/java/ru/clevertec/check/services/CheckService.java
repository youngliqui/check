package ru.clevertec.check.services;

import ru.clevertec.check.models.Check;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Interface for working with receipts
 */
public interface CheckService {
    /**
     * Creating and saving a receipt
     *
     * @param idsAndQuantities   - map consisting of product id = quantity
     * @param discountCardNumber - discount card number
     * @param balanceDebitCard   - debit card balance
     */
    void generateCheck(Map<Integer, Integer> idsAndQuantities, String discountCardNumber, BigDecimal balanceDebitCard);

    /**
     * Generating a receipt with an error
     *
     * @param message - error message
     */
    void generateExceptionCheck(String message);

    /**
     * Printing a receipt to the console
     *
     * @param check            - the receipt that will be printed
     * @param balanceDebitCard - debit card balance
     */
    void printCheckToConsole(Check check, BigDecimal balanceDebitCard);
}
