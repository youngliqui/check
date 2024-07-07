package ru.clevertec.check.repositories.impl;

import ru.clevertec.check.models.Check;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.repositories.CheckRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.clevertec.check.config.AppConfig.getResultFilePath;

/**
 * Implementation of the CheckRepository interface for working with receipts in a csv file
 */
public class CsvCheckRepository implements CheckRepository {

    @Override
    public void save(Check check) {
        try (FileWriter writer = new FileWriter(getResultFilePath())) {

            LocalDateTime dateTime = LocalDateTime.now();
            check.setDateTime(dateTime);

            writer.write("Date;Time\n"
                    + dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm:ss")) + "\n\n"
                    + "QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n"
            );

            for (Product product : check.getProducts()) {
                String currentDiscount = check.getProductsDiscount().get(product.getId()).toString();
                String currentTotal = check.getProductsTotal().get(product.getId()).toString();

                writer.write(
                        product.getQuantity() + ";"
                                + product.getDescription() + ";"
                                + product.getPrice() + "$;"
                                + currentDiscount + "$;"
                                + currentTotal + "$"
                                + "\n"
                );
            }

            DiscountCard discountCard = check.getDiscountCard();
            if (discountCard != null) {
                writer.write(
                        "\nDISCOUNT CARD;DISCOUNT PERCENTAGE\n"
                                + discountCard.getNumber() + ";"
                                + discountCard.getDiscount() + "%;"
                                + "\n"
                );
            }

            writer.write(
                    "\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n"
                            + check.getTotalPrice() + "$;"
                            + check.getTotalDiscount() + "$;"
                            + check.getTotalWithDiscount() + "$"
            );

        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            saveException("ERROR\nINTERNAL SERVER ERROR");
        }
    }

    @Override
    public void saveException(String message) {
        try (FileWriter writer = new FileWriter(getResultFilePath())) {

            writer.write(message);

        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
