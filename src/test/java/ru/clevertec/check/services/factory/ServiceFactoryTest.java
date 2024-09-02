package ru.clevertec.check.services.factory;

import org.junit.jupiter.api.Test;
import ru.clevertec.check.services.CheckService;
import ru.clevertec.check.services.DiscountCardService;
import ru.clevertec.check.services.ProductService;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceFactoryTest {
    @Test
    void shouldCreateDiscountCardService() {
        DiscountCardService discountCardService = ServiceFactory.createDiscountCardService();

        assertThat(discountCardService).isNotNull();
    }

    @Test
    void shouldCreateProductService() {
        ProductService productService = ServiceFactory.createProductService();

        assertThat(productService).isNotNull();
    }

    @Test
    void shouldCreateCheckService() {
        CheckService checkService = ServiceFactory.createCheckService();

        assertThat(checkService).isNotNull();
    }
}