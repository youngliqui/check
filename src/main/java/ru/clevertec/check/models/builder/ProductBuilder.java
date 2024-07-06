package ru.clevertec.check.models.builder;

import ru.clevertec.check.models.Product;

import java.math.BigDecimal;

public class ProductBuilder {
    private int id;
    private String description;
    private int quantity;
    private boolean isWholesale;
    private BigDecimal price;

    public ProductBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public ProductBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public ProductBuilder setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public ProductBuilder setWholesale(boolean wholesale) {
        this.isWholesale = wholesale;
        return this;
    }

    public ProductBuilder setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Product build() {
        return new Product(id, description, price, quantity, isWholesale);
    }
}
