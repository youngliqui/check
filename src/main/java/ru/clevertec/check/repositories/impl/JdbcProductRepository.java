package ru.clevertec.check.repositories.impl;

import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.builder.ProductBuilder;
import ru.clevertec.check.repositories.ProductRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.clevertec.check.config.AppConfig.*;

/**
 * Implementation of the ProductRepository for working with the database
 */
public class JdbcProductRepository implements ProductRepository {
    @Override
    public Optional<Product> getProductById(int id) throws SQLException {
        String query = "SELECT * FROM public.product WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(
                getDatasourceUrl(), getDatasourceUsername(), getDatasourcePassword());
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(
                        new ProductBuilder()
                                .setId(resultSet.getInt("id"))
                                .setDescription(resultSet.getString("description"))
                                .setQuantity(resultSet.getInt("quantity_in_stock"))
                                .setPrice(resultSet.getBigDecimal("price"))
                                .setWholesale(resultSet.getBoolean("wholesale_product"))
                                .build()
                );
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Product> getProductsByIds(List<Integer> ids) throws SQLException {
        List<Product> products = new ArrayList<>();

        String queryBuilder = "SELECT * FROM public.product WHERE id IN (?" + ", ?".repeat(Math.max(0, ids.size() - 1)) +
                ")";

        try (Connection connection = DriverManager.getConnection(
                getDatasourceUrl(), getDatasourceUsername(), getDatasourcePassword());
             PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder)
        ) {
            for (int i = 0; i < ids.size(); i++) {
                preparedStatement.setInt(i + 1, ids.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                products.add(
                        new ProductBuilder()
                                .setId(resultSet.getInt("id"))
                                .setDescription(resultSet.getString("description"))
                                .setQuantity(resultSet.getInt("quantity_in_stock"))
                                .setPrice(resultSet.getBigDecimal("price"))
                                .setWholesale(resultSet.getBoolean("wholesale_product"))
                                .build()
                );
            }
        }

        return products;
    }
}
