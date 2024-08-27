package ru.clevertec.check.arguments.impl;

import ru.clevertec.check.arguments.ArgumentStrategy;
import ru.clevertec.check.arguments.ArgumentsHandler;
import ru.clevertec.check.exceptions.InvalidInputException;

import java.util.*;
import java.util.stream.Stream;

public class ArgumentsHandlerImpl implements ArgumentsHandler {
    private final List<ArgumentStrategy> strategyList;
    private final List<String> requiredKeys;


    public ArgumentsHandlerImpl(List<ArgumentStrategy> strategyList) {
        this.strategyList = strategyList;
        requiredKeys = Arrays.asList(
                "datasource.url",
                "datasource.username",
                "datasource.password",
                "saveToFile"
        );
    }

    @Override
    public void handleArguments(String[] args, Map<Integer, Integer> idsAndQuantities, Map<String, Object> context)
            throws InvalidInputException {
        if (args.length < 6) {
            throw new InvalidInputException("Must be min 6 args");
        }

        Optional<String> unhandledArg = Stream.of(args).filter(arg ->
                Optional.ofNullable(strategyList)
                        .stream()
                        .flatMap(Collection::stream)
                        .noneMatch(strategy -> strategy.processArgument(arg, idsAndQuantities, context))
        ).findFirst();

        if (unhandledArg.isPresent()) {
            throw new InvalidInputException("Incorrect argument - " + unhandledArg.get());
        }

        if (idsAndQuantities.isEmpty()) {
            throw new InvalidInputException("There must be at least one id-quantity bundle");
        }


        checkContextArgs(context);
    }

    private void checkContextArgs(Map<String, Object> context) throws InvalidInputException {
        Optional.ofNullable(context)
                .orElseThrow(() -> new InvalidInputException("Context is null"));

        List<String> missingKeys = requiredKeys.stream()
                .filter(key -> !context.containsKey(key))
                .toList();

        if (!missingKeys.isEmpty()) {
            throw new InvalidInputException("Missing arguments: " + String.join(", ", missingKeys));
        }
    }
}
