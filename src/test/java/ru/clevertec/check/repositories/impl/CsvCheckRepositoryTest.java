package ru.clevertec.check.repositories.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.config.AppConfig;
import ru.clevertec.check.exceptions.QuantityException;
import ru.clevertec.check.models.Check;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.builder.DiscountCardBuilder;
import ru.clevertec.check.models.builder.ProductBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class CsvCheckRepositoryTest {
    private CsvCheckRepository repository;
    private Check check;

    private Product product;
    private DiscountCard discountCard;

    @BeforeEach
    void setUp() throws QuantityException {
        product = new ProductBuilder()
                .setId(1)
                .setQuantity(10)
                .setPrice(BigDecimal.valueOf(1.25))
                .setDescription("prod1")
                .setWholesale(true)
                .build();

        discountCard = new DiscountCardBuilder()
                .setId(1)
                .setNumber("1111")
                .setDiscount(3)
                .build();

        Map<Integer, Integer> idsAndQuantities = Map.of(1, 2);

        check = new Check(List.of(product), discountCard, idsAndQuantities);
        repository = new CsvCheckRepository();
        AppConfig.setResultFilePath("test_check.csv");
    }

    @Test
    void shouldSaveCheckToCsvFile() {
        repository.save(check);

        String resultFilePath = AppConfig.getResultFilePath();
        assertThat(new File(resultFilePath).exists()).isTrue();

        List<String> expectedLines = Arrays.asList(
                "Date;Time",
                "\\d{2}\\.\\d{2}\\.\\d{4};\\d{2}:\\d{2}:\\d{2}",
                "",
                "QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL",
                String.format("%s;%s;%s$;%s$;%s$",
                        check.getProducts().get(0).getQuantity(),
                        product.getDescription(),
                        product.getPrice(),
                        check.getProductsDiscount().get(product.getId()),
                        check.getProductsTotal().get(product.getId())),
                "",
                "DISCOUNT CARD;DISCOUNT PERCENTAGE",
                String.format("%s;%s%%", discountCard.getNumber(), discountCard.getDiscount()),
                "",
                "TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT",
                String.format("%s$;%s$;%s$",
                        check.getTotalPrice(), check.getTotalDiscount(), check.getTotalWithDiscount())
        );

        try (BufferedReader reader = new BufferedReader(new FileReader(resultFilePath))) {
            String line;
            for (int i = 0; i < expectedLines.size(); i++) {
                line = reader.readLine();

                if (i == 1) {
                    assertThat(line.matches(expectedLines.get(i))).isTrue();
                } else {
                    assertThat(line).isEqualTo(expectedLines.get(i));
                }
            }
        } catch (IOException e) {
            fail("Error reading file: " + e.getMessage());
        }
    }

    @Test
    void shouldSaveExceptionToCsvFile() {
        String errorMessage = "Error message";
        repository.saveException(errorMessage);

        String resultFilePath = AppConfig.getResultFilePath();
        assertThat(new File(resultFilePath).exists()).isTrue();

        try (BufferedReader reader = new BufferedReader(new FileReader(resultFilePath))) {
            String line = reader.readLine();

            assertThat(line).isEqualTo(errorMessage);
        } catch (IOException e) {
            fail("error reading file: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(AppConfig.getResultFilePath()));
        AppConfig.setResultFilePath(AppConfig.RESULT_FILE_DEFAULT_PATH);
    }
}