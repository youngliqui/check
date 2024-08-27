package ru.clevertec.check.arguments.impl;

import ru.clevertec.check.arguments.ArgumentStrategy;

import java.util.Map;
import java.util.Optional;

public class ProductArgumentStrategy implements ArgumentStrategy {
    @Override
    public boolean processArgument(String arg, Map<Integer, Integer> idsAndQuantities, Map<String, Object> context) {
        return Optional.ofNullable(arg)
                .filter(a -> a.contains("-"))
                .map(a -> a.split("-"))
                .filter(parts -> parts.length == 2)
                .map(parts -> new int[]{
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1])
                })
                .filter(parts -> parts[0] >= 0 && parts[1] >= 1)
                .map(parts -> {
                    idsAndQuantities.merge(parts[0], parts[1], Integer::sum);
                    return true;
                })
                .orElse(false);
    }
}
