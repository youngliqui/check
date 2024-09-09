package ru.clevertec.check.arguments.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.config.DataSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DatasourcePasswordArgumentStrategyTest {
    private DatasourcePasswordArgumentStrategy strategy;
    private Map<String, Object> context;

    @BeforeEach
    void setUp() {
        strategy = new DatasourcePasswordArgumentStrategy();
        context = new HashMap<>();
    }

    @Test
    void shouldProcessArgumentAndReturnTrueIfStartsWithDatasourcePassword() {
        String arg = "datasource.password=1488";

        boolean result = strategy.processArgument(arg, Collections.emptyMap(), context);

        assertThat(result).isTrue();
        assertThat(context.get("datasource.password")).isEqualTo("1488");
        assertThat(DataSource.getPassword()).isEqualTo("1488");
    }

    @Test
    void shouldReturnFalseIfIncorrectArg() {
        String arg = "dummy";

        boolean result = strategy.processArgument(arg, Collections.emptyMap(), context);

        assertThat(result).isFalse();
        assertThat(context.get("datasource.password")).isNull();
    }
}