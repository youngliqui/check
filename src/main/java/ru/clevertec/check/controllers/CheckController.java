package ru.clevertec.check.controllers;

import ru.clevertec.check.arguments.ArgumentStrategy;
import ru.clevertec.check.exceptions.InvalidInputException;
import ru.clevertec.check.services.CheckService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.clevertec.check.exceptions.ErrorMessage.BAD_REQUEST;
import static ru.clevertec.check.exceptions.ErrorMessage.INTERNAL_SERVER_ERROR;

public class CheckController {
    private final CheckService checkService;
    private final List<ArgumentStrategy> argumentStrategies;

    public CheckController(CheckService checkService, List<ArgumentStrategy> argumentStrategies) {
        this.checkService = checkService;
        this.argumentStrategies = argumentStrategies;
    }

    /**
     * Processing arguments and generating a receipt
     *
     * @param args - passed arguments at startup
     */
    public void processArgs(String[] args) {
        try {
            if (args.length < 2) {
                throw new InvalidInputException("Must be more than two arguments");
            }

            String discountCardNumber = null;
            BigDecimal balanceDebitCard = null;
            Map<Integer, Integer> idsAndQuantities = new HashMap<>();
            Map<String, Object> context = new HashMap<>();

            for (String arg : args) {
                boolean handled = false;
                for (ArgumentStrategy strategy : argumentStrategies) {
                    try {
                        strategy.processArgument(arg, idsAndQuantities, context);
                        handled = true;
                    } catch (InvalidInputException ignored) {
                    }
                }
                if (!handled) {
                    throw new InvalidInputException("Incorrect argument - " + arg);
                }
            }

            if (idsAndQuantities.isEmpty()) {
                throw new InvalidInputException("There must be at least one id-quantity bundle");
            }

            discountCardNumber = (String) context.get("discountCardNumber");
            balanceDebitCard = (BigDecimal) context.get("balanceDebitCard");

            if (balanceDebitCard == null) {
                throw new InvalidInputException("Debit balance must not be null");
            }

            checkService.generateCheck(idsAndQuantities, discountCardNumber, balanceDebitCard);

        } catch (InvalidInputException e) {
            System.out.println("INVALID INPUT ERROR: " + e.getMessage());
            checkService.generateExceptionCheck(BAD_REQUEST.getMessage());
        } catch (Exception e) {
            System.out.println("INTERNAL SERVER ERROR: " + e.getMessage());
            checkService.generateExceptionCheck(INTERNAL_SERVER_ERROR.getMessage());
        }
    }
}
