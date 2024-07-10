package ru.clevertec.check.repositories.impl;

import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.builder.DiscountCardBuilder;
import ru.clevertec.check.repositories.DiscountCardRepository;

import java.sql.*;
import java.util.Optional;

import static ru.clevertec.check.config.DataSource.*;

/**
 * Implementation of the DiscountCardRepository for working with the database
 */
public class JdbcDiscountCardRepository implements DiscountCardRepository {
    @Override
    public Optional<DiscountCard> getDiscountCardByNumber(String number) throws SQLException {
        String query = "SELECT * FROM public.discount_card WHERE number = ?";

        try (Connection connection = DriverManager.getConnection(
                getUrl(), getUsername(), getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, Integer.parseInt(number));

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return createDiscountCardOptional(resultSet);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<DiscountCard> getDiscountCardById(int id) throws SQLException {
        String query = "SELECT * FROM public.discount_card WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(
                getUrl(), getUsername(), getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return createDiscountCardOptional(resultSet);
            }
        }
        return Optional.empty();
    }

    @Override
    public void addDiscountCard(DiscountCard discountCard) throws SQLException {
        String query = "INSERT INTO public.discount_card(number, amount) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(
                getUrl(), getUsername(), getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, Integer.parseInt(discountCard.getNumber()));
            preparedStatement.setInt(2, discountCard.getDiscount());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateDiscountCard(int id, DiscountCard discountCard) throws SQLException {
        String query = "UPDATE public.discount_card SET number = ?, amount = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(
                getUrl(), getUsername(), getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, Integer.parseInt(discountCard.getNumber()));
            preparedStatement.setInt(2, discountCard.getDiscount());
            preparedStatement.setInt(3, id);

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void deleteDiscountCard(int id) throws SQLException {
        String query = "DELETE FROM public.discount_card WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(
                getUrl(), getUsername(), getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        }
    }

    private static Optional<DiscountCard> createDiscountCardOptional(ResultSet resultSet) throws SQLException {
        return Optional.of(
                new DiscountCardBuilder()
                        .setId(resultSet.getInt("id"))
                        .setNumber(resultSet.getString("number"))
                        .setDiscount(resultSet.getInt("amount"))
                        .build()
        );
    }
}
