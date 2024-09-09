package ru.clevertec.check.arguments.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountCardArgumentStrategyTest {
    private DiscountCardArgumentStrategy strategy;
    private Map<String, Object> context;

    @BeforeEach
    void setUp() {
        strategy = new DiscountCardArgumentStrategy();
        context = new HashMap<>();
    }

    @Test
    void shouldProcessArgumentAndReturnTrueIfStartsWithDiscountCard() {
        String arg = "discountCard=1111";

        boolean result = strategy.processArgument(arg, Collections.emptyMap(), context);

        assertThat(result).isTrue();
        assertThat(context.get("discountCard")).isEqualTo("1111");
    }

    @Test
    void shouldReturnFalseIfIncorrectArg() {
        String arg = "dummy";

        boolean result = strategy.processArgument(arg, Collections.emptyMap(), context);

        assertThat(result).isFalse();
        assertThat(context.get("discountCard")).isNull();
    }
}