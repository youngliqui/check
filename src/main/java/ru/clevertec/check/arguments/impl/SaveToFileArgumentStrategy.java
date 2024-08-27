package ru.clevertec.check.arguments.impl;

import ru.clevertec.check.arguments.ArgumentStrategy;
import ru.clevertec.check.config.AppConfig;

import java.util.Map;
import java.util.Optional;

public class SaveToFileArgumentStrategy implements ArgumentStrategy {
    private static final String ARGUMENT_NAME = "saveToFile";

    @Override
    public boolean processArgument(String arg, Map<Integer, Integer> idsAndQuantities, Map<String, Object> context) {
        return Optional.ofNullable(arg)
                .filter(a -> a.startsWith(ARGUMENT_NAME))
                .map(a -> a.split("=")[1])
                .map(newFilePath -> {
                    context.put(ARGUMENT_NAME, newFilePath);
                    AppConfig.setResultFilePath(newFilePath);
                    return true;
                })
                .orElse(false);
    }
}
