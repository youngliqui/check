package repositories;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.builder.ProductBuilder;
import ru.clevertec.check.repositories.ProductRepository;
import ru.clevertec.check.repositories.impl.JdbcProductRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.clevertec.check.config.AppConfig.*;

@DisplayName("JdbcProductRepository test")
public class JdbcProductRepositoryTest {
    private static final Product CORRECT_PRODUCT_1 = createCorrectProduct1();
    private static final Product CORRECT_PRODUCT_2 = createCorrectProduct2();

    private final ProductRepository productRepository = new JdbcProductRepository();

    @BeforeAll
    static void init() {
        setDatasourceUrl(TestDatabaseConnection.URL);
        setDatasourceUsername(TestDatabaseConnection.USERNAME);
        setDatasourcePassword(TestDatabaseConnection.PASSWORD);
    }

    @Test
    @DisplayName("should return correct product when id is valid")
    void getProductByCorrectId() {
        try {
            Optional<Product> currentProductOptional = productRepository.getProductById(CORRECT_PRODUCT_1.getId());

            assertThat(currentProductOptional).isPresent();

            Product currentProduct = currentProductOptional.get();

            assertAll(
                    () -> {
                        assertThat(currentProduct.getId()).isEqualTo(CORRECT_PRODUCT_1.getId());
                        assertThat(currentProduct.getPrice()).isEqualTo(CORRECT_PRODUCT_1.getPrice());
                        assertThat(currentProduct.getDescription()).isEqualTo(CORRECT_PRODUCT_1.getDescription());
                        assertThat(currentProduct.getQuantity()).isEqualTo(CORRECT_PRODUCT_1.getQuantity());
                        assertThat(currentProduct.isWholesale()).isEqualTo(CORRECT_PRODUCT_1.isWholesale());
                    }
            );

        } catch (SQLException e) {
            fail("Database error: " + e.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideCorrectIdList")
    @DisplayName("should return correct products when ids are valid")
    void getProductsListByCorrectIdList(List<Integer> correctIds) {
        try {
            List<Product> currentProducts = productRepository.getProductsByIds(correctIds);

            assertThat(currentProducts).hasSize(correctIds.size());

            for (int i = 0; i < currentProducts.size(); i++) {
                Product currentProduct = currentProducts.get(i);
                Product expectedProduct = getExpectedProduct(correctIds.get(i));
                assertAll(
                        () -> {
                            assertThat(currentProduct.getId()).isEqualTo(expectedProduct.getId());
                            assertThat(currentProduct.getQuantity()).isEqualTo(expectedProduct.getQuantity());
                            assertThat(currentProduct.getDescription()).isEqualTo(expectedProduct.getDescription());
                            assertThat(currentProduct.getPrice()).isEqualTo(expectedProduct.getPrice());
                            assertThat(currentProduct.isWholesale()).isEqualTo(expectedProduct.isWholesale());
                        }
                );
            }
        } catch (SQLException e) {
            fail("Database error: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("should return empty list when ids are incorrect")
    void getEmptyListWhenIdIsIncorrect() {
        int incorrectId = -1;

        try {
            List<Product> currentProducts = productRepository.getProductsByIds(List.of(incorrectId));

            assertThat(currentProducts).isEmpty();
        } catch (SQLException e) {
            fail("Database error: " + e.getMessage());
        }
    }

    private static Stream<Arguments> provideCorrectIdList() {
        return Stream.of(
                Arguments.arguments(List.of(CORRECT_PRODUCT_1.getId()), CORRECT_PRODUCT_2.getId()),
                Arguments.arguments(List.of(CORRECT_PRODUCT_1.getId()))
        );
    }

    private Product getExpectedProduct(int id) {
        if (id == CORRECT_PRODUCT_1.getId()) {
            return CORRECT_PRODUCT_1;
        } else if (id == CORRECT_PRODUCT_2.getId()) {
            return CORRECT_PRODUCT_2;
        } else {
            throw new IllegalArgumentException("Incorrect product id: " + id);
        }
    }

    private static Product createCorrectProduct1() {
        return new ProductBuilder()
                .setId(8)
                .setDescription("Packed oranges 1kg")
                .setQuantity(12)
                .setPrice(new BigDecimal("3.20"))
                .setWholesale(false)
                .build();
    }

    private static Product createCorrectProduct2() {
        return new ProductBuilder()
                .setId(13)
                .setDescription("Baguette 360g")
                .setQuantity(10)
                .setPrice(new BigDecimal("1.30"))
                .setWholesale(true)
                .build();
    }
}
