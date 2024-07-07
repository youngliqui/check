package ru.clevertec.check.repositories.impl;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.builder.DiscountCardBuilder;
import ru.clevertec.check.repositories.DiscountCardRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import static ru.clevertec.check.config.AppConfig.CSV_DELIMITER;
import static ru.clevertec.check.config.AppConfig.DISCOUNT_CARD_FILE_PATH;

/**
 * Implementation of the Discount Card Repository interface for working with discount cards in a csv file
 */
public class CsvDiscountCardRepository implements DiscountCardRepository {
    @Override
    public Optional<DiscountCard> getDiscountCardByNumber(String number) throws IOException {
        try (CSVParser csvParser = new CSVParser(new FileReader(DISCOUNT_CARD_FILE_PATH),
                CSVFormat.Builder.create()
                        .setDelimiter(CSV_DELIMITER)
                        .setHeader("id", "description", "price, $", "quantity in stock", "wholesale product")
                        .setSkipHeaderRecord(true)
                        .build())) {

            for (CSVRecord record : csvParser) {
                if (record.get(1).equals(number)) {
                    return Optional.of(
                            new DiscountCardBuilder()
                                    .setId(Integer.parseInt(record.get(0)))
                                    .setNumber(record.get(1))
                                    .setDiscount(Integer.parseInt(record.get(2)))
                                    .build()
                    );
                }
            }

        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            throw new IOException(e.getMessage());
        }

        return Optional.empty();
    }
}
