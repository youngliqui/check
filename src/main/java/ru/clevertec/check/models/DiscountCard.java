package ru.clevertec.check.models;

/**
 * The entity of a discount card
 */
public class DiscountCard {
    private int id;
    private String number;
    private int discount;

    public DiscountCard(int id, String number, int discount) {
        this.id = id;
        this.number = number;
        this.discount = discount;
    }

    public String getNumber() {
        return number;
    }

    public int getDiscount() {
        return discount;
    }

    public int getId() {
        return id;
    }
}
