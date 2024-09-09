package ru.clevertec.check.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.services.CheckService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {
    @InjectMocks
    private ErrorHandler errorHandler;
    @Mock
    private CheckService checkService;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler(checkService);
    }

    @Test
    void shouldHandleInvalidInputError() {
        Exception exception = new RuntimeException("Invalid input error");

        errorHandler.handleInvalidInputError(exception);

        verify(checkService, times(1))
                .generateExceptionCheck(ErrorMessage.BAD_REQUEST.getMessage());
    }

    @Test
    void shouldHandleInternalServerError() {
        Exception exception = new RuntimeException("Internal server error");

        errorHandler.handleInternalServerError(exception);

        verify(checkService, times(1))
                .generateExceptionCheck(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void shouldHandleNotEnoughMoneyError() {
        Exception exception = new RuntimeException("Not enough money  error");

        errorHandler.handleNotEnoughMoneyError(exception);

        verify(checkService, times(1))
                .generateExceptionCheck(ErrorMessage.NOT_ENOUGH_MONEY.getMessage());
    }
}