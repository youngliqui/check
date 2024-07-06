package ru.clevertec.check.models.builder;

import ru.clevertec.check.models.DiscountCard;

public class DiscountCardBuilder {
    private int id;
    private String number;
    private int discount;

    public DiscountCardBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public DiscountCardBuilder setNumber(String number) {
        this.number = number;
        return this;
    }

    public DiscountCardBuilder setDiscount(int discount) {
        this.discount = discount;
        return this;
    }

    public DiscountCard build() {
        return new DiscountCard(id, number, discount);
    }
}
