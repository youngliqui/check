package ru.clevertec.check.services.impl;

import ru.clevertec.check.exceptions.DiscountCardNotFoundException;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.repositories.DiscountCardRepository;
import ru.clevertec.check.services.DiscountCardService;

import java.io.IOException;

public class DiscountCardServiceImpl implements DiscountCardService {
    private final DiscountCardRepository discountCardRepository;

    public DiscountCardServiceImpl(DiscountCardRepository discountCardRepository) {
        this.discountCardRepository = discountCardRepository;
    }

    @Override
    public DiscountCard getDiscountCardByNumber(String number) throws DiscountCardNotFoundException, IOException {
        return discountCardRepository.getDiscountCardByNumber(number).orElseThrow(
                () -> new DiscountCardNotFoundException(number)
        );
    }
}