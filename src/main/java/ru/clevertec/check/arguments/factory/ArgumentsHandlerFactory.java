package ru.clevertec.check.arguments.factory;

import ru.clevertec.check.arguments.ArgumentsHandler;
import ru.clevertec.check.arguments.impl.ArgumentsHandlerImpl;
import ru.clevertec.check.arguments.impl.BalanceDebitCardArgumentStrategy;
import ru.clevertec.check.arguments.impl.DiscountCardArgumentStrategy;
import ru.clevertec.check.arguments.impl.ProductArgumentStrategy;

import java.util.Arrays;

public class ArgumentsHandlerFactory {
    public static ArgumentsHandler createArgumentsHandler() {
        return new ArgumentsHandlerImpl(Arrays.asList(
                new BalanceDebitCardArgumentStrategy(),
                new DiscountCardArgumentStrategy(),
                new ProductArgumentStrategy()
        ));
    }
}