package ru.clevertec.check.arguments.impl;

import ru.clevertec.check.arguments.ArgumentStrategy;
import ru.clevertec.check.arguments.ArgumentsHandler;
import ru.clevertec.check.exceptions.InvalidInputException;

import java.util.List;
import java.util.Map;

public class ArgumentsHandlerImpl implements ArgumentsHandler {
    private final List<ArgumentStrategy> strategyList;

    public ArgumentsHandlerImpl(List<ArgumentStrategy> strategyList) {
        this.strategyList = strategyList;
    }

    @Override
    public void handleArguments(String[] args, Map<Integer, Integer> idsAndQuantities, Map<String, Object> context)
            throws InvalidInputException {
        if (args.length < 2) {
            throw new InvalidInputException("Must be more than two arguments");
        }

        for (String arg : args) {
            boolean handled = false;
            for (ArgumentStrategy strategy : strategyList) {
                try {
                    strategy.processArgument(arg, idsAndQuantities, context);
                    handled = true;
                    break;
                } catch (InvalidInputException ignored) {
                }
            }
            if (!handled) {
                throw new InvalidInputException("Incorrect argument - " + arg);
            }
        }

        if (idsAndQuantities.isEmpty()) {
            throw new InvalidInputException("There must be at least one id-quantity bundle");
        }
    }
}