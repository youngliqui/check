package ru.clevertec.check.models;

import java.math.BigDecimal;

/**
 * The entity of the product
 */
public class Product {
    private int id;
    private String description;
    private int quantity;
    private boolean isWholesale;
    private BigDecimal price;

    public Product(int id, String description, BigDecimal price, int quantity, boolean isWholesale) {
        this.id = id;
        this.description = description;
        this.quantity = quantity;
        this.isWholesale = isWholesale;
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isWholesale() {
        return isWholesale;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
