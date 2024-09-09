package ru.clevertec.check.models;

import java.math.BigDecimal;
import java.util.Objects;

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

    public String getDescription() {
        return description;
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

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && quantity == product.quantity && isWholesale == product.isWholesale && Objects.equals(description, product.description) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, quantity, isWholesale, price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", isWholesale=" + isWholesale +
                ", price=" + price +
                '}';
    }
}
