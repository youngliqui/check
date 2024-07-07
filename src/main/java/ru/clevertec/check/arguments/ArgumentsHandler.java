package ru.clevertec.check.arguments;

import ru.clevertec.check.exceptions.InvalidInputException;

import java.util.Map;

/**
 * The interface defines methods for processing arguments that are accepted when the program is started
 */
public interface ArgumentsHandler {
    void handleArguments(String[] args, Map<Integer, Integer> idsAndQuantities, Map<String, Object> context)
            throws InvalidInputException;
}