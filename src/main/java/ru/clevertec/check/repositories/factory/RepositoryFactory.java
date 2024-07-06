package ru.clevertec.check.repositories.factory;

import ru.clevertec.check.repositories.CheckRepository;
import ru.clevertec.check.repositories.DiscountCardRepository;
import ru.clevertec.check.repositories.ProductRepository;
import ru.clevertec.check.repositories.impl.CsvCheckRepository;
import ru.clevertec.check.repositories.impl.CsvDiscountCardRepository;
import ru.clevertec.check.repositories.impl.CsvProductRepository;

public class RepositoryFactory {
    public static CheckRepository createCheckRepository() {
        return new CsvCheckRepository();
    }

    public static DiscountCardRepository createDiscountCardRepository() {
        return new CsvDiscountCardRepository();
    }

    public static ProductRepository createProductRepository() {
        return new CsvProductRepository();
    }
}
