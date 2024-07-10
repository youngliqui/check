package ru.clevertec.check.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class CheckJsonParser {
    public static Map<Integer, Integer> getIdsAndQuantities(JsonObject requestBody) {
        Map<Integer, Integer> idsAndQuantities = new HashMap<>();
        JsonArray productsArray = requestBody.getAsJsonArray("products");
        for (int i = 0; i < productsArray.size(); i++) {
            JsonObject product = productsArray.get(i).getAsJsonObject();
            int id = product.get("id").getAsInt();
            int quantity = product.get("quantity").getAsInt();
            idsAndQuantities.merge(id, quantity, Integer::sum);
        }

        return idsAndQuantities;
    }
}
