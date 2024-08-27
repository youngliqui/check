package ru.clevertec.check.arguments.impl;

import ru.clevertec.check.arguments.ArgumentStrategy;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public class BalanceDebitCardArgumentStrategy implements ArgumentStrategy {
    private final static String ARGUMENT_NAME = "balanceDebitCard";

    @Override
    public boolean processArgument(String arg, Map<Integer, Integer> idsAndQuantities, Map<String, Object> context) {
        return Optional.ofNullable(arg)
                .filter(a -> a.startsWith(ARGUMENT_NAME))
                .map(a -> a.split("=")[1])
                .map(BigDecimal::new)
                .map(balance -> {
                    context.put(ARGUMENT_NAME, balance);
                    return true;
                })
                .orElse(false);
    }
}
