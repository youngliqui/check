package ru.clevertec.check.repositories.impl;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ru.clevertec.check.exceptions.ProductNotFoundException;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.builder.ProductBuilder;
import ru.clevertec.check.repositories.ProductRepository;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static ru.clevertec.check.config.AppConfig.CSV_DELIMITER;
import static ru.clevertec.check.config.AppConfig.getProductFilePath;
import static ru.clevertec.check.exceptions.ErrorMessage.INTERNAL_SERVER_ERROR;

/**
 * Implementation of the ProductRepository interface for working with products in a csv file
 */
public class CsvProductRepository implements ProductRepository {
    @Override
    public Optional<Product> getProductById(int id) throws IOException {
        try (CSVParser csvParser = new CSVParser(new FileReader(getProductFilePath()),
                CSVFormat.Builder.create()
                        .setHeader("id", "description", "price, $", "quantity in stock", "wholesale product")
                        .setDelimiter(CSV_DELIMITER)
                        .setSkipHeaderRecord(true)
                        .build())) {
            for (CSVRecord record : csvParser) {
                if (Integer.parseInt(record.get(0)) == id) {
                    return Optional.of(
                            new ProductBuilder()
                                    .setId(id)
                                    .setDescription(record.get(1))
                                    .setPrice(new BigDecimal(record.get(2)))
                                    .setQuantity(Integer.parseInt(record.get(3)))
                                    .setWholesale(Boolean.parseBoolean(record.get(4)))
                                    .build()
                    );
                }
            }
        } catch (IOException e) {
            System.out.println(INTERNAL_SERVER_ERROR.getMessage() + ": " + e.getMessage());
            throw new IOException(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Product> getProductsByIds(List<Integer> ids) throws ProductNotFoundException, IOException {
        List<Product> products = new ArrayList<>();

        try (CSVParser csvParser = new CSVParser(new FileReader(getProductFilePath()),
                CSVFormat.Builder.create()
                        .setHeader("id", "description", "price, $", "quantity in stock", "wholesale product")
                        .setDelimiter(CSV_DELIMITER)
                        .setSkipHeaderRecord(true)
                        .build())) {
            Map<Integer, CSVRecord> recordMap = new HashMap<>();

            for (CSVRecord record : csvParser) {
                recordMap.put(Integer.parseInt(record.get(0)), record);
            }

            for (int id : ids) {
                CSVRecord record = recordMap.get(id);
                if (record != null) {
                    products.add(
                            new ProductBuilder()
                                    .setId(id)
                                    .setDescription(record.get(1))
                                    .setPrice(new BigDecimal(record.get(2)))
                                    .setQuantity(Integer.parseInt(record.get(3)))
                                    .setWholesale(Boolean.parseBoolean(record.get(4)))
                                    .build()
                    );
                } else {
                    throw new ProductNotFoundException(id);
                }
            }
        } catch (IOException e) {
            System.out.println(INTERNAL_SERVER_ERROR.getMessage() + ": " + e.getMessage());
            throw new IOException(e.getMessage());
        }

        return products;
    }
}
