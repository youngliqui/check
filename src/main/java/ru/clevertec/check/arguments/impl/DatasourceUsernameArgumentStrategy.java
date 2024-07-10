package ru.clevertec.check.arguments.impl;

import ru.clevertec.check.arguments.ArgumentStrategy;
import ru.clevertec.check.config.DataSource;
import ru.clevertec.check.exceptions.InvalidInputException;

import java.util.Map;

public class DatasourceUsernameArgumentStrategy implements ArgumentStrategy {
    @Override
    public void processArgument(String arg, Map<Integer, Integer> idsAndQuantities, Map<String, Object> context)
            throws InvalidInputException {
        if (arg.startsWith("datasource.username=")) {
            String value = arg.split("=")[1];
            context.put("datasource.username", value);
            DataSource.setUsername(value);
        } else {
            throw new InvalidInputException("Incorrect arg - " + arg);
        }
    }
}
