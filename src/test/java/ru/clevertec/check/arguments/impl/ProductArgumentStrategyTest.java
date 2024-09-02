package ru.clevertec.check.arguments.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ProductArgumentStrategyTest {
    private ProductArgumentStrategy strategy;
    private Map<Integer, Integer> idsAndQuantities;

    @BeforeEach
    void setUp() {
        strategy = new ProductArgumentStrategy();
        idsAndQuantities = new HashMap<>();
    }

    @Test
    void shouldProcessArgumentAndReturnTrueIfArgContainsDash() {
        String arg = "1-10";

        boolean result = strategy.processArgument(arg, idsAndQuantities, Collections.emptyMap());

        assertThat(result).isTrue();
        assertThat(idsAndQuantities.get(1)).isEqualTo(10);
    }

    @Test
    void shouldReturnFalseIfIncorrectArg() {
        String arg = "dummy";

        boolean result = strategy.processArgument(arg, idsAndQuantities, Collections.emptyMap());

        assertThat(result).isFalse();
        assertThat(idsAndQuantities.get(1)).isNull();
    }
}