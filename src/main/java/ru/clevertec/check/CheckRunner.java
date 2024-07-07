package ru.clevertec.check;


import ru.clevertec.check.arguments.factory.ArgumentsHandlerFactory;
import ru.clevertec.check.controllers.CheckController;
import ru.clevertec.check.exceptions.ErrorHandler;
import ru.clevertec.check.services.CheckService;
import ru.clevertec.check.services.factory.ServiceFactory;

public class CheckRunner {
    public static void main(String[] args) {
        CheckService checkService = ServiceFactory.createCheckService();
        CheckController checkController = new CheckController(
                checkService,
                new ErrorHandler(checkService),
                ArgumentsHandlerFactory.createArgumentsHandler()
        );
        checkController.generateCheck(args);
    }
}