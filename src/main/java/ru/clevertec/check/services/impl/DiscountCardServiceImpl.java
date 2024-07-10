package ru.clevertec.check.services.impl;

import ru.clevertec.check.exceptions.DiscountCardNotFoundException;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.repositories.DiscountCardRepository;
import ru.clevertec.check.services.DiscountCardService;

import java.sql.SQLException;

public class DiscountCardServiceImpl implements DiscountCardService {
    private final DiscountCardRepository discountCardRepository;

    public DiscountCardServiceImpl(DiscountCardRepository discountCardRepository) {
        this.discountCardRepository = discountCardRepository;
    }

    @Override
    public DiscountCard getDiscountCardByNumber(String number) throws SQLException, DiscountCardNotFoundException {
        return discountCardRepository.getDiscountCardByNumber(number).orElseThrow(
                () -> new DiscountCardNotFoundException(number)
        );
    }

    @Override
    public DiscountCard getDiscountCardById(int id) throws SQLException, DiscountCardNotFoundException {
        return discountCardRepository.getDiscountCardById(id).orElseThrow(
                () -> new DiscountCardNotFoundException("ID - " + id)
        );
    }

    @Override
    public void addDiscountCard(DiscountCard discountCard) throws SQLException {
        discountCardRepository.addDiscountCard(discountCard);
    }

    @Override
    public void updateDiscountCardById(int id, DiscountCard discountCard) throws SQLException {
        discountCardRepository.updateDiscountCard(id, discountCard);
    }

    @Override
    public void deleteDiscountCardById(int id) throws SQLException {
        discountCardRepository.deleteDiscountCard(id);
    }
}
