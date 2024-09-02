package ru.clevertec.check.repositories.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.clevertec.check.config.DataSource;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.builder.ProductBuilder;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcProductRepositoryTest {
    private JdbcProductRepository repository;
    private Connection connection;

    private final String databaseUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=1";
    private final String databaseUsername = "sa";
    private final String databasePassword = "";

    @BeforeAll
    void setUp() throws SQLException {
        DataSource.setUrl(databaseUrl);
        DataSource.setUsername(databaseUsername);
        DataSource.setPassword(databasePassword);

        connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE public.product
                    (
                        id                BIGSERIAL PRIMARY KEY,
                        description       VARCHAR(50)    NOT NULL,
                        price             DECIMAL(10, 2) NOT NULL CHECK (price >= 0.00),
                        quantity_in_stock INTEGER        NOT NULL CHECK (quantity_in_stock >= 0),
                        wholesale_product BOOLEAN        NOT NULL
                    );
                     """);

            statement.execute("INSERT INTO public.product (description, price, quantity_in_stock, wholesale_product)" +
                    "VALUES ('prod1', 1.25, 10, true)");
            statement.execute("INSERT INTO public.product (description, price, quantity_in_stock, wholesale_product) " +
                    "VALUES ('prod2', 0.85, 7, false)");
            statement.execute("INSERT INTO public.product (description, price, quantity_in_stock, wholesale_product) " +
                    "VALUES ('prod3', 2.23, 3, false)");
        }

        repository = new JdbcProductRepository();
    }

    @AfterAll
    void tearDown() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS public.product");
        } finally {
            connection.close();
        }
    }

    @Test
    void shouldGetProductById() throws SQLException {
        Product expectedProduct1 = new ProductBuilder()
                .setId(1)
                .setDescription("prod1")
                .setQuantity(10)
                .setPrice(BigDecimal.valueOf(1.25))
                .setWholesale(true)
                .build();

        Optional<Product> retrievedProduct = repository.getProductById(1);

        assertThat(retrievedProduct).isPresent();
        assertThat(retrievedProduct.get()).isEqualTo(expectedProduct1);
    }

    @Test
    void shouldGetProductListByIds() throws SQLException {
        List<Integer> ids = List.of(1, 2);
        Product expectedProduct1 = new ProductBuilder()
                .setId(1)
                .setDescription("prod1")
                .setQuantity(10)
                .setPrice(BigDecimal.valueOf(1.25))
                .setWholesale(true)
                .build();
        Product expectedProduct2 = new ProductBuilder()
                .setId(2)
                .setDescription("prod2")
                .setQuantity(7)
                .setPrice(BigDecimal.valueOf(0.85))
                .setWholesale(false)
                .build();

        var resultList = repository.getProductsByIds(ids);

        assertThat(resultList).hasSize(2);
        assertThat(resultList).containsExactlyInAnyOrder(expectedProduct1, expectedProduct2);
    }

    @Test
    void shouldAddProduct() throws SQLException {
        Product addedProduct = new ProductBuilder()
                .setId(4)
                .setDescription("added")
                .setPrice(BigDecimal.valueOf(1.23))
                .setQuantity(5)
                .build();

        repository.addProduct(addedProduct);

        Optional<Product> retrievedProduct = repository.getProductById(4);
        assertThat(retrievedProduct).isPresent();
        assertThat(retrievedProduct.get()).isEqualTo(addedProduct);
    }

    @Test
    void shouldUpdateProduct() throws SQLException {
        Product updatedProduct = new ProductBuilder()
                .setId(3)
                .setDescription("updated")
                .setPrice(BigDecimal.valueOf(2.22))
                .setQuantity(12)
                .build();

        repository.updateProduct(3, updatedProduct);

        Optional<Product> retrievedProduct = repository.getProductById(3);
        assertThat(retrievedProduct).isPresent();
        assertThat(retrievedProduct.get()).isEqualTo(updatedProduct);
    }

    @Test
    void shouldDeleteProduct() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) " +
                    "VALUES (5, 'deleted', 0.43, 10, true)");
        }

        repository.deleteProduct(5);

        Optional<Product> retrievedProduct = repository.getProductById(5);
        assertThat(retrievedProduct).isEmpty();
    }

    @Test
    void shouldUpdateProductQuantity() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) " +
                    "VALUES (6, 'prod6', 0.21, 10, false)");
        }

        repository.updateProductQuantity(6, 8);

        Optional<Product> retrievedProduct = repository.getProductById(6);
        assertThat(retrievedProduct).isPresent();
        assertThat(retrievedProduct.get().getQuantity()).isEqualTo(2);
    }
}