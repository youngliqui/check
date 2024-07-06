package ru.clevertec.check.arguments;

import ru.clevertec.check.exceptions.InvalidInputException;

import java.util.Map;

public interface ArgumentStrategy {
    void processArgument(String arg, Map<Integer, Integer> idsAndQuantities, Map<String, Object> context)
            throws InvalidInputException;
}
