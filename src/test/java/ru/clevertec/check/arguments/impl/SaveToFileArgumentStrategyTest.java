package ru.clevertec.check.arguments.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.config.AppConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SaveToFileArgumentStrategyTest {
    private SaveToFileArgumentStrategy strategy;
    private Map<String, Object> context;

    @BeforeEach
    void setUp() {
        strategy = new SaveToFileArgumentStrategy();
        context = new HashMap<>();
    }

    @Test
    void shouldProcessArgumentAndReturnTrueIfStartsWithSaveToFile() {
        String arg = "saveToFile=file_path.csv";

        boolean result = strategy.processArgument(arg, Collections.emptyMap(), context);

        assertThat(result).isTrue();
        assertThat(context.get("saveToFile")).isEqualTo("file_path.csv");
        assertThat(AppConfig.getResultFilePath()).isEqualTo("file_path.csv");
    }

    @Test
    void shouldReturnFalseIfIncorrectArg() {
        String arg = "dummy";

        boolean result = strategy.processArgument(arg, Collections.emptyMap(), context);

        assertThat(result).isFalse();
        assertThat(context.get("saveToFile")).isNull();
    }
}