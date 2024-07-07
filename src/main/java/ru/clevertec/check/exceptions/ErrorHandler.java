package ru.clevertec.check.exceptions;

import ru.clevertec.check.services.CheckService;

import static ru.clevertec.check.exceptions.ErrorMessage.*;

public class ErrorHandler {
    private final CheckService checkService;

    public ErrorHandler(CheckService checkService) {
        this.checkService = checkService;
    }

    public void handleInvalidInputError(Exception e) {
        String error = BAD_REQUEST.getMessage();
        handle(e, error);
    }

    public void handleInternalServerError(Exception e) {
        String error = INTERNAL_SERVER_ERROR.getMessage();
        handle(e, error);
    }

    public void handleNotEnoughMoneyError(Exception e) {
        String error = NOT_ENOUGH_MONEY.getMessage();
        handle(e, error);
    }

    private void handle(Exception e, String error) {
        System.out.println(error + ": " + e.getMessage());
        checkService.generateExceptionCheck(error);
    }
}
