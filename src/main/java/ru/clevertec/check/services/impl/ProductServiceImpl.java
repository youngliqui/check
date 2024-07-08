package ru.clevertec.check.services.impl;

import ru.clevertec.check.exceptions.ProductNotFoundException;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.repositories.ProductRepository;
import ru.clevertec.check.services.ProductService;

import java.sql.SQLException;
import java.util.List;

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
}
