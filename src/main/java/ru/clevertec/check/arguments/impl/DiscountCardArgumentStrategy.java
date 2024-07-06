package ru.clevertec.check.arguments.impl;

import ru.clevertec.check.arguments.ArgumentStrategy;
import ru.clevertec.check.exceptions.InvalidInputException;

import java.util.Map;

public class DiscountCardArgumentStrategy implements ArgumentStrategy {
    @Override
    public void processArgument(String arg, Map<Integer, Integer> idsAndQuantities, Map<String, Object> context)
            throws InvalidInputException {
        if (arg.startsWith("discountCard=")) {
            context.put("discountCardNumber", arg.split("=")[1]);
        } else {
            throw new InvalidInputException("Incorrect argument - " + arg);
        }
    }
}
