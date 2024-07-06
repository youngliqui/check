package ru.clevertec.check.arguments.impl;

import ru.clevertec.check.arguments.ArgumentStrategy;
import ru.clevertec.check.config.AppConfig;
import ru.clevertec.check.exceptions.InvalidInputException;

import java.util.Map;

public class PathToFileArgumentStrategy implements ArgumentStrategy {
    @Override
    public void processArgument(String arg, Map<Integer, Integer> idsAndQuantities, Map<String, Object> context)
            throws InvalidInputException {
        if (arg.startsWith("pathToFile")) {
            String newFilePath = arg.split("=")[1];
            context.put("pathToFile", newFilePath);
            AppConfig.setProductFilePath(newFilePath);
        } else {
            throw new InvalidInputException("Incorrect arg - " + arg);
        }
    }
}
