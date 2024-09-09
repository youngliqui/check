package ru.clevertec.check.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.arguments.ArgumentsHandler;
import ru.clevertec.check.exceptions.ErrorHandler;
import ru.clevertec.check.exceptions.InvalidInputException;
import ru.clevertec.check.services.CheckService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckControllerTest {
    @InjectMocks
    private CheckController checkController;
    @Mock
    private CheckService checkService;
    @Mock
    private ErrorHandler errorHandler;
    @Mock
    private ArgumentsHandler argumentsHandler;

    private String[] args;
    private Map<Integer, Integer> idsAndQuantities;

    @BeforeEach
    void setUp() {
        args = new String[]{"arg1", "arg2", "arg3"};
        idsAndQuantities = new HashMap<>();
    }


    @Test
    void shouldGenerateCheckSuccessfully() throws InvalidInputException {
        String discountCardNumber = "1111";
        BigDecimal balanceDebitCard = new BigDecimal("100.00");
        idsAndQuantities.put(1, 2);

        doAnswer(invocation -> {
            Map<Integer, Integer> idsAndQuantities = invocation.getArgument(1);
            idsAndQuantities.put(1, 2);
            Map<String, Object> context = invocation.getArgument(2);
            context.put("discountCard", discountCardNumber);
            context.put("balanceDebitCard", balanceDebitCard);
            return null;
        }).when(argumentsHandler).handleArguments(any(String[].class), anyMap(), anyMap());

        checkController.generateCheck(args);

        verify(checkService, times(1))
                .generateCheck(idsAndQuantities, discountCardNumber, balanceDebitCard);
    }

    @Test
    void shouldHandleInvalidInputErrorIfBalanceIsNull() throws InvalidInputException {
        doAnswer(invocation -> {
            Map<Integer, Integer> idsAndQuantities = invocation.getArgument(1);
            idsAndQuantities.put(1, 2);
            Map<String, Object> context = invocation.getArgument(2);
            context.put("discountCard", "1111");
            return null;
        }).when(argumentsHandler).handleArguments(any(String[].class), anyMap(), anyMap());

        checkController.generateCheck(args);

        verify(errorHandler, times(1))
                .handleInvalidInputError(any(InvalidInputException.class));
    }

    @Test
    void shouldHandleInternalServerErrorWhenExceptionThrows() throws InvalidInputException {
        doThrow(new RuntimeException("Internal Error"))
                .when(argumentsHandler)
                .handleArguments(any(String[].class), anyMap(), anyMap());

        checkController.generateCheck(args);

        verify(errorHandler).handleInternalServerError(any(Exception.class));
    }
}