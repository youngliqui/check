package ru.clevertec.check.services.impl;

import ru.clevertec.check.exceptions.DiscountCardNotFoundException;
import ru.clevertec.check.exceptions.ProductNotFoundException;
import ru.clevertec.check.exceptions.QuantityException;
import ru.clevertec.check.models.Check;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.repositories.CheckRepository;
import ru.clevertec.check.services.CheckService;
import ru.clevertec.check.services.DiscountCardService;
import ru.clevertec.check.services.ProductService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.clevertec.check.exceptions.ErrorMessage.*;

public class CheckServiceImpl implements CheckService {
    private final CheckRepository checkRepository;
    private final ProductService productService;
    private final DiscountCardService discountCardService;

    public CheckServiceImpl(CheckRepository checkRepository, ProductService productService,
                            DiscountCardService discountCardService) {
        this.checkRepository = checkRepository;
        this.productService = productService;
        this.discountCardService = discountCardService;
    }


    @Override
    public void generateCheck(Map<Integer, Integer> idsAndQuantities, String discountCardNumber,
                              BigDecimal balanceDebitCard) {
        try {
            List<Integer> ids = new ArrayList<>(idsAndQuantities.keySet());
            List<Product> products = productService.getProductsByIds(ids);
            DiscountCard discountCard = null;

            if (discountCardNumber != null) {
                discountCard = discountCardService.getDiscountCardByNumber(discountCardNumber);
            }

            Check check = new Check(products, discountCard, idsAndQuantities);

            if (check.getTotalWithDiscount().compareTo(balanceDebitCard) > 0) {
                generateException(NOT_ENOUGH_MONEY.getMessage());
            } else {
                checkRepository.save(check);
                printCheckToConsole(check, balanceDebitCard);
            }

        } catch (DiscountCardNotFoundException | QuantityException | ProductNotFoundException e) {
            generateException(BAD_REQUEST.getMessage(), e.getMessage());
        } catch (SQLException e) {
            generateException(INTERNAL_SERVER_ERROR.getMessage(), e.getMessage());
        }
    }

    @Override
    public void generateExceptionCheck(String message) {
        checkRepository.saveException("ERROR\n" + message);
    }

    private void generateException(String error, String exceptionMessage) {
        System.out.println(error + ": " + exceptionMessage);
        generateExceptionCheck(error);
    }

    private void generateException(String error) {
        System.out.println("ERROR: " + error);
        generateExceptionCheck(error);
    }

    @Override
    public void printCheckToConsole(Check check, BigDecimal balanceDebitCard) {
        String dateTime = check.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm:ss"));

        System.out.println(
                "+-----------------------------------------------------------+\n" +
                        "|                          Check                            |\n" +
                        "+-----------------------------------------------------------+\n" +
                        dateTime + "\n" +
                        "+-----------------------------------------------------------+\n" +
                        "| QTY |       DESCRIPTION      | PRICE | DISCOUNT |  TOTAL  |"
        );

        for (Product product : check.getProducts()) {
            String formattedItem = String.format(
                    "| %3s | %-22s | %5.2f | %7.2f | %8.2f |",
                    product.getQuantity(), product.getDescription(), product.getPrice(),
                    check.getProductsDiscount().get(product.getId()).doubleValue(),
                    check.getProductsTotal().get(product.getId()).doubleValue()
            );
            System.out.println(formattedItem);
        }

        DiscountCard discountCard = check.getDiscountCard();
        if (discountCard != null) {
            System.out.println(
                    "+-----------------------------------------------------------+\n" +
                            "DISCOUNT CARD; DISCOUNT PERCENTAGE\n" +
                            discountCard.getNumber() + ";" + discountCard.getDiscount() + "%"
            );
        }

        System.out.println(
                "+-----------------------------------------------------------+\n" +
                        "TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n"
                        + check.getTotalPrice() + "$; " + check.getTotalDiscount() + "$; " + check.getTotalWithDiscount() + "$"
                        + "\nBALANCE DEBIT CARD = " + balanceDebitCard + "$"
                        + "\n+-----------------------------------------------------------+\n"
        );
    }
}
