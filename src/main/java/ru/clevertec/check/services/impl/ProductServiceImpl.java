package ru.clevertec.check.services.impl;

import ru.clevertec.check.exceptions.ProductNotFoundException;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.repositories.ProductRepository;
import ru.clevertec.check.services.ProductService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProductsByIds(List<Integer> ids) throws SQLException, ProductNotFoundException {
        return Optional.ofNullable(productRepository.getProductsByIds(ids))
                .filter(p -> !p.isEmpty() && p.size() == ids.size())
                .orElseThrow(() -> new ProductNotFoundException("all products was not found"));
    }
}
