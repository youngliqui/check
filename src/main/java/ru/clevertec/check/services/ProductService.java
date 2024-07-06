package ru.clevertec.check.services;


import ru.clevertec.check.exceptions.ProductNotFoundException;
import ru.clevertec.check.models.Product;

import java.util.List;

/**
 * Interface for working with products
 */
public interface ProductService {
    /**
     * Getting a list of products by ID list
     *
     * @param ids - list of product IDs
     * @return list of products
     * @throws ProductNotFoundException - when at least one product was not found
     */
    List<Product> getProductsByIds(List<Integer> ids) throws ProductNotFoundException;
}
