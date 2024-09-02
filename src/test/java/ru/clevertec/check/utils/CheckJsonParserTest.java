package ru.clevertec.check.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CheckJsonParserTest {

    @Test
    public void getIdsAndQuantities() {
        JsonObject requestBody = new JsonObject();
        JsonArray products = Stream.of(
                createProduct(1, 5),
                createProduct(1, 10),
                createProduct(2, 15)
        ).collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
        requestBody.add("products", products);

        var actualResult = CheckJsonParser.getIdsAndQuantities(requestBody);

        assertThat(actualResult)
                .hasSize(2)
                .containsEntry(1, 15)
                .containsEntry(2, 15);
    }

    private static JsonObject createProduct(int id, int quantity) {
        JsonObject product = new JsonObject();
        product.addProperty("id", id);
        product.addProperty("quantity", quantity);
        return product;
    }
}