package ru.clevertec.check.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.exceptions.ProductNotFoundException;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.builder.ProductBuilder;
import ru.clevertec.check.repositories.ProductRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @InjectMocks
    private ProductServiceImpl productService;
    @Spy
    private ProductRepository productRepository;

    @Test
    void shouldAddProduct() throws SQLException {
        Product product = createProduct(1, "product 1", 5, true, new BigDecimal("5.34"));

        productService.addProduct(product);

        verify(productRepository).addProduct(product);
    }

    @Test
    void shouldDeleteProductById() throws SQLException {
        int productId = 10;

        productService.deleteProductById(productId);

        verify(productRepository).deleteProduct(productId);
    }

    @Test
    void shouldUpdateProductById() throws SQLException {
        int productId = 12;
        Product newProduct = createProduct(1, "prod1", 1, false, new BigDecimal("1.02"));

        productService.updateProductById(productId, newProduct);

        verify(productRepository).updateProduct(productId, newProduct);
    }

    @Test
    void shouldUpdateProductQuantities() throws SQLException {
        Map<Integer, Integer> idsAndQuantities = Map.of(1, 2, 4, 10);

        productService.updateProductQuantities(idsAndQuantities);

        verify(productRepository, times(1)).updateProductQuantity(1, 2);
        verify(productRepository, times(1)).updateProductQuantity(4, 10);
    }

    @Test
    void shouldGetProductById() throws SQLException, ProductNotFoundException {
        Product product = createProduct(1, "prod1", 10, false, new BigDecimal("0.23"));

        when(productRepository.getProductById(product.getId())).thenReturn(Optional.of(product));

        productService.getProductById(product.getId());

        verify(productRepository).getProductById(product.getId());
    }

    @Test
    void shouldThrowExceptionWhenGetProductByNonExistentId() throws SQLException {
        int incorrectId = 10;

        when(productRepository.getProductById(incorrectId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(incorrectId));
        verify(productRepository).getProductById(incorrectId);
    }

    @Test
    void shouldGetProductsByIds() throws SQLException, ProductNotFoundException {
        List<Product> products = List.of(
                createProduct(1, "prod1", 2, false, new BigDecimal("2.02")),
                createProduct(12, "prod12", 10, true, new BigDecimal("1.10"))
        );
        List<Integer> ids = products.stream().map(Product::getId).toList();

        when(productRepository.getProductsByIds(ids)).thenReturn(products);

        var result = productService.getProductsByIds(ids);

        assertThat(result).containsAll(products);
    }

    @Test
    void shouldThrowExceptionWhenGetProductsByNonExistentId() throws SQLException {
        List<Integer> incorrectIds = List.of(10, 20);

        when(productRepository.getProductsByIds(incorrectIds)).thenReturn(Collections.emptyList());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductsByIds(incorrectIds));
        verify(productRepository).getProductsByIds(incorrectIds);
    }

    private static Product createProduct(int id, String desc, int quantity, boolean isWholesale, BigDecimal price) {
        return new ProductBuilder()
                .setId(id)
                .setDescription(desc)
                .setQuantity(quantity)
                .setWholesale(isWholesale)
                .setPrice(price)
                .build();
    }
}