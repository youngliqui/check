package ru.clevertec.check.repositories;

import ru.clevertec.check.models.Check;

/**
 * The interface defines operations for working with receipts
 */
public interface CheckRepository {
    /**
     * Saves the receipt
     *
     * @param check - a saved receipt
     */
    void save(Check check);

    /**
     * Saves an exception when it is impossible to print the receipt correctly
     *
     * @param message - error message
     */
    void saveException(String message);
}
