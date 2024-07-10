package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.config.AppConfig;
import ru.clevertec.check.exceptions.QuantityException;
import ru.clevertec.check.models.Check;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.builder.DiscountCardBuilder;
import ru.clevertec.check.models.builder.ProductBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CheckTest {

    @Test
    @DisplayName("should calculate product total and discount without wholesale")
    void shouldCalculateProductTotalAndDiscount() throws QuantityException {
        DiscountCard discountCard = createDiscountCard();
        Product product = createProduct1();
        int quantity = 4;
        Map<Integer, Integer> idsAndQuantities = Map.of(product.getId(), quantity);

        Check check = new Check(List.of(product), discountCard, idsAndQuantities);

        BigDecimal discountPercentage = BigDecimal.valueOf(discountCard.getDiscount());
        BigDecimal productPrice = product.getPrice();

        BigDecimal expectedTotal = productPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal expectedDiscount = expectedTotal.multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100), 2, 4);

        assertThat(check.getProductsTotal().get(product.getId())).isEqualTo(expectedTotal);
        assertThat(check.getProductsDiscount().get(product.getId())).isEqualTo(expectedDiscount);
    }

    @Test
    @DisplayName("should calculate product total and discount with wholesale")
    void shouldCalculateProductTotalAndDiscountWithWholesale() throws QuantityException {
        DiscountCard discountCard = createDiscountCard();
        Product product = createProduct2();
        int quantity = 6;
        Map<Integer, Integer> idsAndQuantities = Map.of(product.getId(), quantity);

        Check check = new Check(List.of(product), discountCard, idsAndQuantities);

        BigDecimal discountPercentage = AppConfig.WHOLESALE_PERCENT;
        BigDecimal productPrice = product.getPrice();

        BigDecimal expectedTotal = productPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal expectedDiscount = expectedTotal.multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100), 2, 4);

        assertThat(check.getProductsTotal().get(product.getId())).isEqualTo(expectedTotal);
        assertThat(check.getProductsDiscount().get(product.getId())).isEqualTo(expectedDiscount);
    }

    @Test
    void shouldThrowQuantityExceptionWhenQuantityLessThanRequired() {
        Product product = createProduct1();
        int quantity = 100;

        assertThrows(QuantityException.class, () -> new Check(List.of(product), null,
                Map.of(product.getId(), quantity)));
    }

    private Product createProduct1() {
        return new ProductBuilder()
                .setId(1)
                .setDescription("Test Product 1")
                .setQuantity(10)
                .setPrice(new BigDecimal("12.12"))
                .setWholesale(false)
                .build();
    }

    private Product createProduct2() {
        return new ProductBuilder()
                .setId(2)
                .setDescription("Test Product 2")
                .setQuantity(10)
                .setPrice(new BigDecimal("5.79"))
                .setWholesale(true)
                .build();
    }

    private static DiscountCard createDiscountCard() {
        return new DiscountCardBuilder()
                .setId(2)
                .setNumber("2323")
                .setDiscount(4)
                .build();
    }
}