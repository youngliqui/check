package ru.clevertec.check.arguments.impl;

import ru.clevertec.check.arguments.ArgumentStrategy;
import ru.clevertec.check.exceptions.InvalidInputException;

import java.util.Map;

public class ProductArgumentStrategy implements ArgumentStrategy {
    @Override
    public void processArgument(String arg, Map<Integer, Integer> idsAndQuantities, Map<String, Object> context)
            throws InvalidInputException {
        if (arg.contains("-")) {
            String[] idQuantity = arg.split("-");
            int id = Integer.parseInt(idQuantity[0]);
            int quantity = Integer.parseInt(idQuantity[1]);

            if (quantity < 1) {
                throw new InvalidInputException("Incorrect argument - " + arg);
            }

            idsAndQuantities.merge(id, quantity, Integer::sum);
        } else {
            throw new InvalidInputException("Incorrect argument - " + arg);
        }
    }
}
