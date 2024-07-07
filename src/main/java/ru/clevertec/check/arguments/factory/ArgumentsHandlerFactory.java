package ru.clevertec.check.arguments.factory;

import ru.clevertec.check.arguments.ArgumentsHandler;
import ru.clevertec.check.arguments.impl.*;

import java.util.Arrays;

public class ArgumentsHandlerFactory {
    public static ArgumentsHandler createArgumentsHandler() {
        return new ArgumentsHandlerImpl(Arrays.asList(
                new BalanceDebitCardArgumentStrategy(),
                new DiscountCardArgumentStrategy(),
                new SaveToFileArgumentStrategy(),
                new DatasourceUrlArgumentStrategy(),
                new DatasourceUsernameArgumentStrategy(),
                new DatasourcePasswordArgumentStrategy(),
                new ProductArgumentStrategy()
        ));
    }
}
