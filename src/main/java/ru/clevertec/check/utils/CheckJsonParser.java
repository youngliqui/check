package ru.clevertec.check.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CheckJsonParser {
    public static Map<Integer, Integer> getIdsAndQuantities(JsonObject requestBody) {
        return StreamSupport.stream(requestBody.getAsJsonArray("products").spliterator(), false)
                .map(JsonElement::getAsJsonObject)
                .collect(Collectors.toMap(
                        product -> product.get("id").getAsInt(),
                        product -> product.get("quantity").getAsInt(),
                        Integer::sum
                ));
    }
}
