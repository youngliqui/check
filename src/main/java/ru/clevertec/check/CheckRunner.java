package ru.clevertec.check;


import ru.clevertec.check.arguments.impl.BalanceDebitCardArgumentStrategy;
import ru.clevertec.check.arguments.impl.DiscountCardArgumentStrategy;
import ru.clevertec.check.arguments.impl.ProductArgumentStrategy;
import ru.clevertec.check.controllers.CheckController;
import ru.clevertec.check.services.CheckService;
import ru.clevertec.check.services.factory.ServiceFactory;

import java.util.Arrays;

public class CheckRunner {
    public static void main(String[] args) {
        CheckService checkService = ServiceFactory.createCheckService();
        CheckController checkController = new CheckController(
                checkService,
                Arrays.asList(
                        new BalanceDebitCardArgumentStrategy(),
                        new DiscountCardArgumentStrategy(),
                        new ProductArgumentStrategy()
                )
        );

        checkController.processArgs(args);
    }
}