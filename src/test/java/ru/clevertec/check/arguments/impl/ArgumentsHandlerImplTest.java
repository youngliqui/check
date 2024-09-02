package ru.clevertec.check.arguments.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.arguments.ArgumentsHandler;
import ru.clevertec.check.arguments.factory.ArgumentsHandlerFactory;
import ru.clevertec.check.exceptions.InvalidInputException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ArgumentsHandlerImplTest {
    private ArgumentsHandler argumentsHandler;
    private Map<Integer, Integer> idsAndQuantities;
    private Map<String, Object> context;

    @BeforeEach
    void setUp() {
        argumentsHandler = ArgumentsHandlerFactory.createArgumentsHandler();
        idsAndQuantities = new HashMap<>();
        context = new HashMap<>();
    }

    @Test
    void testHandleArgumentsWithInsufficientArgs() {
        String[] args = {"arg1", "arg2", "arg3"};

        var result = assertThrows(InvalidInputException.class,
                () -> argumentsHandler.handleArguments(args, idsAndQuantities, context));
        assertThat(result.getMessage()).isEqualTo("Must be min 6 args");
    }

    @Test
    void testHandArgumentsWithUnhandledArgs() {
        String[] args = {"arg1", "arg2", "arg3", "arg4", "arg5", "arg6"};

        var result = assertThrows(InvalidInputException.class,
                () -> argumentsHandler.handleArguments(args, idsAndQuantities, context));
        assertThat(result.getMessage()).isEqualTo("Incorrect argument - arg1");
    }

    @Test
    void testHandleArgumentsWithCorrectArgs() {
        String[] args = {"saveToFile=file_path.csv", "datasource.url=db_url", "datasource.username=username",
                "datasource.password=password", "balanceDebitCard=100", "1-5", "discountCard=1111"};

        assertDoesNotThrow(() -> argumentsHandler.handleArguments(args, idsAndQuantities, context));
        assertAll(
                () -> assertThat(context.get("saveToFile")).isEqualTo("file_path.csv"),
                () -> assertThat(context.get("datasource.url")).isEqualTo("db_url"),
                () -> assertThat(context.get("datasource.username")).isEqualTo("username"),
                () -> assertThat(context.get("datasource.password")).isEqualTo("password"),
                () -> assertThat(context.get("balanceDebitCard")).isEqualTo(BigDecimal.valueOf(100)),
                () -> assertThat(context.get("discountCard")).isEqualTo("1111"),
                () -> assertThat(idsAndQuantities.get(1)).isEqualTo(5)
        );
    }

    @Test
    void testHandleArgumentsWithEmptyIdsAndQuantities() {
        String[] args = {"saveToFile=file_path.csv", "datasource.url=db_url", "datasource.username=username",
                "datasource.password=password", "balanceDebitCard=100", "discountCard=1111"};

        var result = assertThrows(InvalidInputException.class,
                () -> argumentsHandler.handleArguments(args, idsAndQuantities, context));
        assertThat(result.getMessage()).isEqualTo("There must be at least one id-quantity bundle");
    }

    @Test
    void testHandleArgumentsWithMissingRequiredKey() {
        String[] args = {"1-10", "datasource.url=db_url", "datasource.username=username",
                "datasource.password=password", "balanceDebitCard=100", "discountCard=1111"};

        var result = assertThrows(InvalidInputException.class,
                () -> argumentsHandler.handleArguments(args, idsAndQuantities, context));
        assertThat(result.getMessage()).isEqualTo("Missing arguments: saveToFile");
    }
}