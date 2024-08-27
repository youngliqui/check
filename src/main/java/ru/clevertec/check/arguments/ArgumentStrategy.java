package ru.clevertec.check.arguments;

import java.util.Map;

/**
 * The interface defines methods for handling a specific argument
 */
public interface ArgumentStrategy {
    boolean processArgument(String arg, Map<Integer, Integer> idsAndQuantities, Map<String, Object> context);
}
