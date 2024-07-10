package ru.clevertec.check.services.impl;

import ru.clevertec.check.exceptions.ProductNotFoundException;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.repositories.ProductRepository;
import ru.clevertec.check.services.ProductService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProductsByIds(List<Integer> ids) throws SQLException, ProductNotFoundException {
        List<Product> products = productRepository.getProductsByIds(ids);
        if (products.isEmpty() || ids.size() != products.size()) {
            throw new ProductNotFoundException("all products was not found");
        }

        return products;
    }

    @Override
    public Product getProductById(int id) throws SQLException, ProductNotFoundException {
        return productRepository.getProductById(id).orElseThrow(
                () -> new ProductNotFoundException("product with id - " + id + " was not found")
        );
    }

    @Override
    public void addProduct(Product product) throws SQLException {
        productRepository.addProduct(product);
    }

    @Override
    public void updateProductById(int id, Product product) throws SQLException {
        productRepository.updateProduct(id, product);
    }

    @Override
    public void deleteProductById(int id) throws SQLException {
        productRepository.deleteProduct(id);
    }

    @Override
    public void updateProductQuantities(Map<Integer, Integer> idsAndQuantities) throws SQLException {
        for (Map.Entry<Integer, Integer> entry : idsAndQuantities.entrySet()) {
            int productId = entry.getKey();
            int newQuantity = entry.getValue();
            productRepository.updateProductQuantity(productId, newQuantity);
        }
    }
}
