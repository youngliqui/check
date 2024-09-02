package ru.clevertec.check.arguments.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BalanceDebitCardArgumentStrategyTest {
    private BalanceDebitCardArgumentStrategy strategy;
    private Map<String, Object> context;

    @BeforeEach
    void setUp() {
        strategy = new BalanceDebitCardArgumentStrategy();
        context = new HashMap<>();
    }

    @Test
    void shouldProcessArgumentAndReturnTrueIfStartsWithBalanceDebitCard() {
        String arg = "balanceDebitCard=100";

        boolean result = strategy.processArgument(arg, Collections.emptyMap(), context);

        assertThat(result).isTrue();
        assertThat(context.get("balanceDebitCard")).isEqualTo(BigDecimal.valueOf(100));
    }

    @Test
    void shouldReturnFalseIfIncorrectArg() {
        String arg = "dummy";

        boolean result = strategy.processArgument(arg, Collections.emptyMap(), context);

        assertThat(result).isFalse();
        assertThat(context.get("balanceDebitCard")).isNull();
    }
}