package ru.clevertec.check.arguments.impl;

import ru.clevertec.check.arguments.ArgumentStrategy;
import ru.clevertec.check.config.DataSource;
import ru.clevertec.check.exceptions.InvalidInputException;

import java.util.Map;

public class DatasourceUrlArgumentStrategy implements ArgumentStrategy {
    @Override
    public void processArgument(String arg, Map<Integer, Integer> idsAndQuantities, Map<String, Object> context)
            throws InvalidInputException {
        if (arg.startsWith("datasource.url=")) {
            String value = arg.split("=")[1];
            context.put("datasource.url", value);
            DataSource.setUrl(value);
        } else {
            throw new InvalidInputException("Incorrect arg - " + arg);
        }
    }
}
