package ru.clevertec.check.exceptions;

public class ProductNotFoundException extends Exception {
    private final int productId;

    public ProductNotFoundException(int productId) {
        this.productId = productId;
    }

    @Override
    public String getMessage() {
        return "Product with id - " + productId + " was not found";
    }
}
