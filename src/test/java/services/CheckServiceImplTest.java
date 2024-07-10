package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.exceptions.DiscountCardNotFoundException;
import ru.clevertec.check.exceptions.ProductNotFoundException;
import ru.clevertec.check.models.Check;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.builder.DiscountCardBuilder;
import ru.clevertec.check.models.builder.ProductBuilder;
import ru.clevertec.check.repositories.CheckRepository;
import ru.clevertec.check.services.CheckService;
import ru.clevertec.check.services.DiscountCardService;
import ru.clevertec.check.services.ProductService;
import ru.clevertec.check.services.impl.CheckServiceImpl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckServiceImplTest {
    @Mock
    private CheckRepository checkRepository;
    @Mock
    private ProductService productService;
    @Mock
    private DiscountCardService discountCardService;

    private CheckService checkService;


    @BeforeEach
    void setup() {
        checkService = new CheckServiceImpl(checkRepository, productService, discountCardService);
    }

    @Test
    void shouldGenerateCheckAndSaveToRepository() throws SQLException, ProductNotFoundException, DiscountCardNotFoundException {
        Product testProduct = createProduct();
        List<Product> products = List.of(testProduct);

        DiscountCard discountCard = createDiscountCard();

        when(productService.getProductsByIds(anyList())).thenReturn(products);
        when(discountCardService.getDiscountCardByNumber(anyString())).thenReturn(discountCard);
        doAnswer(invocation -> {
            Check savedCheck = invocation.getArgument(0);
            savedCheck.setDateTime(LocalDateTime.now());
            return null;
        }).when(checkRepository).save(any(Check.class));


        checkService.generateCheck(
                getIdsAndQuantities(testProduct.getId(), 3), discountCard.getNumber(), new BigDecimal("100.00")
        );

        verify(checkRepository, times(1)).save(any(Check.class));
    }

    @Test
    void shouldGenerateExceptionWhenNotEnoughMoney() throws SQLException, ProductNotFoundException, DiscountCardNotFoundException {
        Product testProduct = createProduct();
        List<Product> products = List.of(testProduct);

        DiscountCard discountCard = createDiscountCard();

        when(productService.getProductsByIds(anyList())).thenReturn(products);
        when(discountCardService.getDiscountCardByNumber(anyString())).thenReturn(discountCard);

        checkService.generateCheck(
                getIdsAndQuantities(testProduct.getId(), 3), discountCard.getNumber(), BigDecimal.ZERO
        );

        verify(checkRepository, times(1)).saveException("ERROR\nNOT ENOUGH MONEY");
    }

    @Test
    void shouldGenerateExceptionCheckWhenProductNotFound() throws SQLException, ProductNotFoundException {
        when(productService.getProductsByIds(anyList())).thenThrow(new ProductNotFoundException("Product was not found"));

        checkService.generateCheck(
                getIdsAndQuantities(1, 3), "1488", new BigDecimal("100.00")
        );

        verify(checkRepository, times(1)).saveException("ERROR\nBAD REQUEST");
    }

    @Test
    void shouldGenerateExceptionWhenDiscountCardNotFound() throws SQLException, ProductNotFoundException, DiscountCardNotFoundException {
        Product testProduct = createProduct();
        List<Product> products = List.of(testProduct);

        when(productService.getProductsByIds(anyList())).thenReturn(products);
        when(discountCardService.getDiscountCardByNumber(anyString())).thenThrow(
                new DiscountCardNotFoundException("Discount card was not found")
        );

        checkService.generateCheck(
                getIdsAndQuantities(testProduct.getId(), 3), "1488", new BigDecimal("100.00")
        );

        verify(checkRepository, times(1)).saveException("ERROR\nBAD REQUEST");
    }

    @Test
    void shouldGenerateExceptionWhenQuantityIsViolated() throws SQLException, ProductNotFoundException {
        Product testProduct = createProduct();
        List<Product> products = List.of(testProduct);

        when(productService.getProductsByIds(anyList())).thenReturn(products);

        checkService.generateCheck(
                getIdsAndQuantities(testProduct.getId(), 100), "1488", new BigDecimal("100.00")
        );

        verify(checkRepository, times(1)).saveException("ERROR\nBAD REQUEST");
    }

    @Test
    void shouldGenerateExceptionCheckWhenSQLException() throws SQLException, ProductNotFoundException {
        when(productService.getProductsByIds(anyList())).thenThrow(new SQLException());

        checkService.generateCheck(
                getIdsAndQuantities(1, 3), "1488", new BigDecimal("100.00")
        );

        verify(checkRepository, times(1)).saveException("ERROR\nINTERNAL SERVER ERROR");
    }

    private static Product createProduct() {
        return new ProductBuilder()
                .setId(1)
                .setDescription("Test Product")
                .setPrice(new BigDecimal("2.12"))
                .setWholesale(true)
                .setQuantity(10)
                .build();
    }

    private static DiscountCard createDiscountCard() {
        return new DiscountCardBuilder()
                .setId(2)
                .setNumber("2323")
                .setDiscount(4)
                .build();
    }

    private static Map<Integer, Integer> getIdsAndQuantities(int productId, int quantity) {
        return Map.of(productId, quantity);
    }
}
