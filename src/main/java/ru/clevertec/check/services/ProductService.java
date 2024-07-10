package ru.clevertec.check.services;


import ru.clevertec.check.exceptions.ProductNotFoundException;
import ru.clevertec.check.models.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Interface for working with products
 */
public interface ProductService {
    /**
     * Getting a list of products by ID list
     *
     * @param ids - list of product IDs
     * @return list of products
     * @throws SQLException             - when a database error occurs
     * @throws ProductNotFoundException - when the product was not found
     */
    List<Product> getProductsByIds(List<Integer> ids) throws SQLException, ProductNotFoundException;

    Product getProductById(int id) throws SQLException, ProductNotFoundException;

    void addProduct(Product product) throws SQLException;

    void updateProductById(int id, Product product) throws SQLException;

    void deleteProductById(int id) throws SQLException;

    void updateProductQuantities(Map<Integer, Integer> idsAndQuantities) throws SQLException;
}
