package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.exceptions.ProductNotFoundException;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.builder.ProductBuilder;
import ru.clevertec.check.repositories.ProductRepository;
import ru.clevertec.check.services.ProductService;
import ru.clevertec.check.services.impl.ProductServiceImpl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setup() {
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    @DisplayName("should get products list by correct ids")
    void shouldGetProductsByIds() throws SQLException, ProductNotFoundException {
        List<Product> expectedProducts = List.of(createProduct1(), createProduct2());
        List<Integer> ids = expectedProducts.stream().map(Product::getId).collect(Collectors.toList());
        when(productRepository.getProductsByIds(ids)).thenReturn(expectedProducts);

        List<Product> actualProducts = productService.getProductsByIds(ids);

        assertThat(actualProducts).isEqualTo(expectedProducts);
    }

    @Test
    @DisplayName("should throw product not found exception when products were not found")
    void shouldThrowProductNotFoundExceptionWhenProductsNotFound() throws SQLException {
        when(productRepository.getProductsByIds(anyList())).thenReturn(Collections.emptyList());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductsByIds(anyList()));
    }

    @Test
    @DisplayName("should throw product not found exception when not all products were found")
    void shouldThrowProductNotFoundExceptionWhenNotAllProductsFound() throws SQLException {
        List<Product> foundProducts = List.of(createProduct1(), createProduct2());
        List<Integer> ids = foundProducts.stream().map(Product::getId).collect(Collectors.toList());
        ids.add(100);

        when(productRepository.getProductsByIds(ids)).thenReturn(foundProducts);

        assertThrows(
                ProductNotFoundException.class, () -> productService.getProductsByIds(ids)
        );
    }

    @Test
    @DisplayName("should throw sql exception when repository throw sql exception")
    void shouldThrowSqlExceptionWhenRepositoryThrowsSqlException() throws SQLException {
        List<Integer> ids = List.of(1, 2);
        when(productRepository.getProductsByIds(ids)).thenThrow(new SQLException("Database error"));

        assertThrows(
                SQLException.class, () -> productService.getProductsByIds(ids)
        );
    }

    private Product createProduct1() {
        return new ProductBuilder()
                .setId(1)
                .setDescription("Test Product 1")
                .setQuantity(10)
                .setPrice(new BigDecimal("12.12"))
                .setWholesale(true)
                .build();
    }

    private Product createProduct2() {
        return new ProductBuilder()
                .setId(2)
                .setDescription("Test Product 2")
                .setQuantity(5)
                .setPrice(new BigDecimal("5.79"))
                .setWholesale(false)
                .build();
    }
}
