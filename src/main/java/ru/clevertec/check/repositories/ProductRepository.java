package ru.clevertec.check.repositories;


import ru.clevertec.check.exceptions.ProductNotFoundException;
import ru.clevertec.check.models.Product;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * The interface defines operations for working with products
 */
public interface ProductRepository {
    /**
     * Receives the product by ID
     *
     * @param id - product ID
     * @return product, Product object
     */
    Optional<Product> getProductById(int id) throws ProductNotFoundException, IOException;

    /**
     * Returns a list of products by ID list
     *
     * @param ids - list of IDs
     * @return list of products
     */
    List<Product> getProductsByIds(List<Integer> ids) throws ProductNotFoundException, IOException;
}