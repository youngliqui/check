package ru.clevertec.check.arguments;

import ru.clevertec.check.exceptions.InvalidInputException;

import java.util.Map;

/**
 * The interface defines methods for handling a specific argument
 */
public interface ArgumentStrategy {
    void processArgument(String arg, Map<Integer, Integer> idsAndQuantities, Map<String, Object> context)
            throws InvalidInputException;
}