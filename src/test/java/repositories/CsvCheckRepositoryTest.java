package repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.clevertec.check.config.AppConfig;
import ru.clevertec.check.exceptions.QuantityException;
import ru.clevertec.check.models.Check;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.builder.DiscountCardBuilder;
import ru.clevertec.check.models.builder.ProductBuilder;
import ru.clevertec.check.repositories.CheckRepository;
import ru.clevertec.check.repositories.impl.CsvCheckRepository;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("CsvCheckRepository tests")
public class CsvCheckRepositoryTest {
    private final CheckRepository checkRepository = new CsvCheckRepository();
    private static final Product TEST_PRODUCT = createProduct();
    private static final DiscountCard TEST_DISCOUNT_CARD = createDiscountCard();
    private static final String FILE_NAME = "test_check.csv";

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        AppConfig.setResultFilePath(tempDir.resolve(FILE_NAME).toString());
    }

    @Test
    @DisplayName("should be save to a file correctly")
    void shouldSaveToFile() {
        Check check;
        try {
            check = new Check(List.of(TEST_PRODUCT), TEST_DISCOUNT_CARD, Map.of(TEST_PRODUCT.getId(), 4));

            checkRepository.save(check);

            File checkFile = tempDir.resolve(FILE_NAME).toFile();
            assertThat(checkFile).exists();

            List<String> lines = Files.readAllLines(checkFile.toPath());
            assertAll(
                    () -> {
                        assertThat(lines).hasSize(11);
                        assertThat(lines.get(0)).isEqualTo("Date;Time");
                        assertThat(lines.get(1))
                                .matches("\\d{2}\\.\\d{2}\\.\\d{4};\\d{2}:\\d{2}:\\d{2}");
                        assertThat(lines.get(2)).isEqualTo("");
                        assertThat(lines.get(3)).isEqualTo("QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL");
                        assertThat(lines.get(4)).isEqualTo("4;Test Product;2.03$;0.49$;8.12$");
                        assertThat(lines.get(5)).isEqualTo("");
                        assertThat(lines.get(6)).isEqualTo("DISCOUNT CARD;DISCOUNT PERCENTAGE");
                        assertThat(lines.get(7)).isEqualTo("1488;6%");
                        assertThat(lines.get(8)).isEqualTo("");
                        assertThat(lines.get(9)).isEqualTo("TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT");
                        assertThat(lines.get(10)).isEqualTo("8.12$;0.49$;7.63$");
                    }
            );

        } catch (QuantityException | IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("should be save to a file exception message correctly")
    void shouldSaveExceptionMessage() {
        String exceptionMessage = "INTERNAL SERVER ERROR";
        checkRepository.saveException("ERROR\n" + exceptionMessage);

        File checkFile = tempDir.resolve("test_check.csv").toFile();
        assertThat(checkFile).exists();

        try {
            List<String> lines = Files.readAllLines(checkFile.toPath());

            assertAll(
                    () -> {
                        assertThat(lines.get(0)).isEqualTo("ERROR");
                        assertThat(lines.get(1)).isEqualTo(exceptionMessage);
                    }
            );
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }


    private static Product createProduct() {
        return new ProductBuilder()
                .setId(1)
                .setDescription("Test Product")
                .setQuantity(10)
                .setPrice(new BigDecimal("2.03"))
                .setWholesale(true)
                .build();
    }

    private static DiscountCard createDiscountCard() {
        return new DiscountCardBuilder()
                .setId(2)
                .setNumber("1488")
                .setDiscount(6)
                .build();
    }
}
