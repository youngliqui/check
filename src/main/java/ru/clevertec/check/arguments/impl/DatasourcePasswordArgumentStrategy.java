package ru.clevertec.check.arguments.impl;

import ru.clevertec.check.arguments.ArgumentStrategy;
import ru.clevertec.check.config.DataSource;

import java.util.Map;
import java.util.Optional;

public class DatasourcePasswordArgumentStrategy implements ArgumentStrategy {
    private static final String ARGUMENT_NAME = "datasource.password";

    @Override
    public boolean processArgument(String arg, Map<Integer, Integer> idsAndQuantities, Map<String, Object> context) {
        return Optional.ofNullable(arg)
                .filter(a -> a.startsWith(ARGUMENT_NAME))
                .map(a -> a.split("=")[1])
                .map(value -> {
                    context.put(ARGUMENT_NAME, value);
                    DataSource.setPassword(value);
                    return true;
                })
                .orElse(false);
    }
}
