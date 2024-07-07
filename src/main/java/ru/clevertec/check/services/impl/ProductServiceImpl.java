package ru.clevertec.check.services.impl;

import ru.clevertec.check.exceptions.ProductNotFoundException;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.repositories.ProductRepository;
import ru.clevertec.check.services.ProductService;

import java.io.IOException;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProductsByIds(List<Integer> ids) throws ProductNotFoundException, IOException {
        return productRepository.getProductsByIds(ids);
    }
}