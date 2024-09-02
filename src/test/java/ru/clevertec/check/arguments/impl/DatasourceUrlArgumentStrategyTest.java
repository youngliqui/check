package ru.clevertec.check.arguments.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.config.DataSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DatasourceUrlArgumentStrategyTest {
    private DatasourceUrlArgumentStrategy strategy;
    private Map<String, Object> context;

    @BeforeEach
    void setUp() {
        strategy = new DatasourceUrlArgumentStrategy();
        context = new HashMap<>();
    }

    @Test
    void shouldProcessArgumentAndReturnTrueIfStartsWithDatasourceUrl() {
        String arg = "datasource.url=db_url";

        boolean result = strategy.processArgument(arg, Collections.emptyMap(), context);

        assertThat(result).isTrue();
        assertThat(context.get("datasource.url")).isEqualTo("db_url");
        assertThat(DataSource.getUrl()).isEqualTo("db_url");
    }

    @Test
    void shouldReturnFalseIfIncorrectArg() {
        String arg = "dummy";

        boolean result = strategy.processArgument(arg, Collections.emptyMap(), context);

        assertThat(result).isFalse();
        assertThat(context.get("datasource.url")).isNull();
    }
}