package ru.clevertec.check.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.exceptions.DiscountCardNotFoundException;
import ru.clevertec.check.exceptions.NotEnoughMoneyException;
import ru.clevertec.check.exceptions.ProductNotFoundException;
import ru.clevertec.check.exceptions.QuantityException;
import ru.clevertec.check.models.Check;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.builder.DiscountCardBuilder;
import ru.clevertec.check.models.builder.ProductBuilder;
import ru.clevertec.check.repositories.CheckRepository;
import ru.clevertec.check.services.DiscountCardService;
import ru.clevertec.check.services.ProductService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.clevertec.check.exceptions.ErrorMessage.*;

@ExtendWith(MockitoExtension.class)
class CheckServiceImplTest {
    @InjectMocks
    private CheckServiceImpl checkService;
    @Spy
    private CheckRepository checkRepository;
    @Mock
    private ProductService productService;
    @Mock
    private DiscountCardService discountCardService;
    @Captor
    private ArgumentCaptor<Check> argumentCaptor;

    private DiscountCard discountCard;
    private BigDecimal balanceDebitCard;
    private Map<Integer, Integer> idsAndQuantities;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        discountCard = createDiscountCard(1, "1111", 3);
        balanceDebitCard = new BigDecimal("100.00");
        products = List.of(
                createProduct(1, 10, new BigDecimal("5.00"), "prod1", true),
                createProduct(2, 8, new BigDecimal("3.00"), "prod2", false)
        );
        idsAndQuantities = Map.of(1, 8, 2, 5);
    }

    @Test
    void shouldGenerateCheckAndSaveToRepository() throws SQLException, ProductNotFoundException,
            DiscountCardNotFoundException {

        when(productService.getProductsByIds(anyList())).thenReturn(products);
        when(discountCardService.getDiscountCardByNumber(anyString())).thenReturn(discountCard);
        doAnswer(invocation -> {
            Check savedCheck = invocation.getArgument(0);
            LocalDateTime dateTime = LocalDateTime.of(2000, 10, 10, 12, 12);
            savedCheck.setDateTime(dateTime);
            return dateTime;
        }).when(checkRepository).save(any(Check.class));


        checkService.generateCheck(idsAndQuantities, discountCard.getNumber(), balanceDebitCard);


        verify(checkRepository, times(1)).save(argumentCaptor.capture());
        Check savedCheck = argumentCaptor.getValue();

        assertAll(() -> {
            assertThat(savedCheck.getProducts())
                    .hasSize(products.size())
                    .containsAnyElementsOf(products);
            assertThat(savedCheck.getDiscountCard())
                    .isEqualTo(discountCard);
        });
        assertAll(() -> {
            assertThat(savedCheck.getTotalPrice())
                    .isEqualTo(new BigDecimal("55.00"));
            assertThat(savedCheck.getTotalDiscount())
                    .isEqualTo(new BigDecimal("4.45"));
            assertThat(savedCheck.getTotalWithDiscount())
                    .isEqualTo(new BigDecimal("50.55"));
        });
    }

    @Test
    void shouldGenerateExceptionIfThereIsNotEnoughMoney() throws SQLException, ProductNotFoundException {
        BigDecimal balanceDebitCard = BigDecimal.ZERO;

        when(productService.getProductsByIds(anyList())).thenReturn(products);

        checkService.generateCheck(idsAndQuantities, null, balanceDebitCard);

        verify(checkRepository).saveException("ERROR\n" + NOT_ENOUGH_MONEY.getMessage());
    }

    @Test
    void shouldGenerateExceptionWhenProductNotFound() throws SQLException, ProductNotFoundException {
        when(productService.getProductsByIds(anyList())).thenThrow(ProductNotFoundException.class);

        checkService.generateCheck(Map.of(1, 2), null, new BigDecimal("100.00"));

        verify(checkRepository).saveException("ERROR\n" + BAD_REQUEST.getMessage());
    }

    @Test
    void shouldGenerateExceptionWhenThrowsSQLException() throws SQLException, ProductNotFoundException {
        when(productService.getProductsByIds(anyList())).thenThrow(SQLException.class);

        checkService.generateCheck(Map.of(1, 10), null, new BigDecimal("50.00"));

        verify(checkRepository).saveException("ERROR\n" + INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void shouldGenerateCheckRestAndGetCheckString() throws SQLException, ProductNotFoundException,
            DiscountCardNotFoundException, NotEnoughMoneyException, QuantityException {

        when(productService.getProductsByIds(anyList())).thenReturn(products);
        when(discountCardService.getDiscountCardByNumber(anyString())).thenReturn(discountCard);


        String actualResult = checkService.generateCheckRest(idsAndQuantities, discountCard.getNumber(), balanceDebitCard);


        verify(productService, times(1)).updateProductQuantities(idsAndQuantities);
        assertThat(actualResult)
                .contains(
                        products.get(0).getDescription(),
                        products.get(0).getPrice().toString(),
                        String.valueOf(products.get(0).getQuantity())
                )
                .contains(
                        products.get(1).getDescription(),
                        products.get(1).getPrice().toString(),
                        String.valueOf(products.get(1).getQuantity())
                )
                .contains(discountCard.getNumber(), String.valueOf(discountCard.getDiscount()))
                .contains("55.00$;4.45$;50.55$");
    }

    @Test
    void shouldThrowExceptionWhenGenerateCheckRestWithoutDebitCardMoney() throws SQLException, ProductNotFoundException {
        BigDecimal balanceDebitCard = BigDecimal.ZERO;

        when(productService.getProductsByIds(anyList())).thenReturn(products);

        assertThrows(
                NotEnoughMoneyException.class,
                () -> checkService.generateCheckRest(idsAndQuantities, null, balanceDebitCard)
        );
    }

    @Test
    void shouldGenerateExceptionCheck() {
        String message = "MESSAGE";
        String expectedMessage = "ERROR\n" + message;

        checkService.generateExceptionCheck(message);

        verify(checkRepository, times(1)).saveException(expectedMessage);
    }

    @Test
    void shouldPrintCheckToConsole() throws QuantityException {
        Product product = createProduct(1, 4, new BigDecimal("0.22"), "prod1", true);
        DiscountCard discountCard = createDiscountCard(12, "9999", 2);
        Map<Integer, Integer> idsAndQuantities = Map.of(1, 2);

        Check check = new Check(Collections.singletonList(product), discountCard, idsAndQuantities);
        var expectedDateTime = LocalDateTime.now();
        check.setDateTime(expectedDateTime);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        checkService.printCheckToConsole(check, new BigDecimal("100"));

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertThat(output)
                .contains(expectedDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm:ss")))
                .contains(String.format(
                                "| %3s | %-22s | %5.2f | %7.2f | %8.2f |",
                                product.getQuantity(),
                                product.getDescription(),
                                product.getPrice(),
                                check.getProductsDiscount().get(product.getId()),
                                check.getProductsTotal().get(product.getId())
                        )
                )
                .contains(String.format("%s;%s%%", discountCard.getNumber(), discountCard.getDiscount()))
                .contains(String.format("%s$; %s$; %s$", check.getTotalPrice(),
                        check.getTotalDiscount(), check.getTotalWithDiscount()));
    }


    private static Product createProduct(int id, int quantity, BigDecimal price, String description,
                                         boolean isWholesale) {
        return new ProductBuilder()
                .setId(id)
                .setQuantity(quantity)
                .setPrice(price)
                .setDescription(description)
                .setWholesale(isWholesale)
                .build();
    }

    private static DiscountCard createDiscountCard(int id, String number, int discount) {
        return new DiscountCardBuilder()
                .setId(id)
                .setNumber(number)
                .setDiscount(discount)
                .build();
    }
}