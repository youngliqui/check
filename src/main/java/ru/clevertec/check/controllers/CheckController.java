package ru.clevertec.check.controllers;

import jakarta.servlet.http.HttpServlet;
import ru.clevertec.check.arguments.ArgumentsHandler;
import ru.clevertec.check.exceptions.ErrorHandler;
import ru.clevertec.check.exceptions.InvalidInputException;
import ru.clevertec.check.services.CheckService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class CheckController extends HttpServlet {
    private final CheckService checkService;
    private final ErrorHandler errorHandler;
    private final ArgumentsHandler argumentsHandler;

    public CheckController(CheckService checkService, ErrorHandler errorHandler, ArgumentsHandler argumentsHandler) {
        this.checkService = checkService;
        this.errorHandler = errorHandler;
        this.argumentsHandler = argumentsHandler;
    }

    /**
     * Processing arguments and generating a receipt
     *
     * @param args - passed arguments at startup
     */
    public void generateCheck(String[] args) {
        try {
            Map<Integer, Integer> idsAndQuantities = new HashMap<>();
            Map<String, Object> context = new HashMap<>();

            argumentsHandler.handleArguments(args, idsAndQuantities, context);

            String discountCardNumber = (String) context.get("discountCardNumber");
            BigDecimal balanceDebitCard = (BigDecimal) context.get("balanceDebitCard");

            if (balanceDebitCard == null) {
                throw new InvalidInputException("Debit balance must not be null");
            }

            checkService.generateCheck(idsAndQuantities, discountCardNumber, balanceDebitCard);

        } catch (InvalidInputException e) {
            errorHandler.handleInvalidInputError(e);
        } catch (Exception e) {
            errorHandler.handleInternalServerError(e);
        }
    }
}
