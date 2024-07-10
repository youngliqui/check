package ru.clevertec.check.repositories.impl;

import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.builder.DiscountCardBuilder;
import ru.clevertec.check.repositories.DiscountCardRepository;

import java.sql.*;
import java.util.Optional;

import static ru.clevertec.check.config.AppConfig.*;

/**
 * Implementation of the DiscountCardRepository for working with the database
 */
public class JdbcDiscountCardRepository implements DiscountCardRepository {
    @Override
    public Optional<DiscountCard> getDiscountCardByNumber(String number) throws SQLException {
        String query = "SELECT * FROM public.discount_card WHERE number = ?";

        try (Connection connection = DriverManager.getConnection(
                getDatasourceUrl(), getDatasourceUsername(), getDatasourcePassword());
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, Integer.parseInt(number));

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(
                        new DiscountCardBuilder()
                                .setId(resultSet.getInt("id"))
                                .setNumber(resultSet.getString("number"))
                                .setDiscount(resultSet.getInt("amount"))
                                .build()
                );
            }
        }

        return Optional.empty();
    }
}
