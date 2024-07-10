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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isWholesale() {
        return isWholesale;
    }

    public void setWholesale(boolean wholesale) {
        isWholesale = wholesale;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
