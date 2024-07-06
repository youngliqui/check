package ru.clevertec.check.exceptions;

public class DiscountCardNotFoundException extends Exception {
    private final String cardNumber;

    public DiscountCardNotFoundException(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String getMessage() {
        return "Discount card with number - " + cardNumber + " was not found";
    }
}
