package ru.clevertec.check.services.factory;

import ru.clevertec.check.services.CheckService;
import ru.clevertec.check.services.DiscountCardService;
import ru.clevertec.check.services.ProductService;
import ru.clevertec.check.services.impl.CheckServiceImpl;
import ru.clevertec.check.services.impl.DiscountCardServiceImpl;
import ru.clevertec.check.services.impl.ProductServiceImpl;

import static ru.clevertec.check.repositories.factory.RepositoryFactory.*;

public class ServiceFactory {
    public static CheckService createCheckService() {
        return new CheckServiceImpl(
                createCheckRepository(),
                createProductService(),
                createDiscountCardService()
        );
    }

    public static DiscountCardService createDiscountCardService() {
        return new DiscountCardServiceImpl(createDiscountCardRepository());
    }

    public static ProductService createProductService() {
        return new ProductServiceImpl(createProductRepository());
    }
}
