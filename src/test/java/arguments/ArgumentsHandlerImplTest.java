package arguments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.arguments.ArgumentsHandler;
import ru.clevertec.check.arguments.factory.ArgumentsHandlerFactory;
import ru.clevertec.check.exceptions.InvalidInputException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArgumentsHandlerImplTest {

    private ArgumentsHandler argumentsHandler;
    private Map<String, Object> context;

    private Map<Integer, Integer> idsAndQuantities;

    @BeforeEach
    void setUp() {
        argumentsHandler = ArgumentsHandlerFactory.createArgumentsHandler();
        context = new HashMap<>();
        idsAndQuantities = new HashMap<>();
    }

    @Test
    void testHandleArgumentsWhenIDsAreMissing() {
        String[] args = {"balanceDebitCard=100.00", "saveToFile=./test_file.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/check", "datasource.username=postgres",
                "datasource.password=postgres"};

        assertThrows(InvalidInputException.class,
                () -> argumentsHandler.handleArguments(args, idsAndQuantities, context));
    }

    @Test
    void testHandleArgumentsWhenDatasourceUsernameIsMissing() {
        String[] args = {"1-2", "2-3", "balanceDebitCard=100.00", "saveToFile=./test_file.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/check", "datasource.password=postgres"};

        assertThrows(InvalidInputException.class,
                () -> argumentsHandler.handleArguments(args, idsAndQuantities, context));
    }

    @Test
    void testHandleArgumentsWhenDatasourcePasswordIsMissing() {
        String[] args = {"1-2", "2-3", "balanceDebitCard=100.00", "saveToFile=./test_file.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/check", "datasource.username=postgres"};

        assertThrows(InvalidInputException.class,
                () -> argumentsHandler.handleArguments(args, idsAndQuantities, context));
    }


    @Test
    void testHandleArgumentsWhenDatasourceUrlIsMissing() {
        String[] args = {"1-2", "2-3", "balanceDebitCard=100.00", "discountCard=1111", "saveToFile=./test_file.csv",
                "datasource.username=postgres", "datasource.password=postgres"};

        assertThrows(InvalidInputException.class,
                () -> argumentsHandler.handleArguments(args, idsAndQuantities, context));
    }

    @Test
    void testHandleArgumentsWithValidInput() {
        String[] args = {"1-2", "2-3", "balanceDebitCard=100.00", "discountCard=1111", "saveToFile=./test_file.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/check", "datasource.username=postgres",
                "datasource.password=postgres"};

        Map<String, Object> expectedContext = Map.of(
                "balanceDebitCard", new BigDecimal("100.00"),
                "discountCard", "1111",
                "saveToFile", "./test_file.csv",
                "datasource.url", "jdbc:postgresql://localhost:5432/check",
                "datasource.username", "postgres",
                "datasource.password", "postgres"
        );

        Map<Integer, Integer> expectedIdsAndQuantities = Map.of(
                1, 2,
                2, 3
        );

        assertDoesNotThrow(() -> argumentsHandler.handleArguments(args, idsAndQuantities, context));
        assertThat(idsAndQuantities).containsExactlyInAnyOrderEntriesOf(expectedIdsAndQuantities);
        assertThat(context).containsExactlyInAnyOrderEntriesOf(expectedContext);
    }
}
