package ru.clevertec.check.models;

import ru.clevertec.check.exceptions.QuantityException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.clevertec.check.config.AppConfig.WHOLESALE_COUNT;
import static ru.clevertec.check.config.AppConfig.WHOLESALE_PERCENT;

/**
 * The entity of the receipt
 */
public class Check {
    private LocalDateTime dateTime; // date and time of receipt creation
    private List<Product> products;
    private Map<Integer, BigDecimal> productsDiscount; // discount for each product
    private Map<Integer, BigDecimal> productsTotal; // total price for each product
    private DiscountCard discountCard;
    private BigDecimal totalPrice = BigDecimal.ZERO; // total price of all products without discounts
    private BigDecimal totalDiscount = BigDecimal.ZERO; // total discount of all products
    private BigDecimal totalWithDiscount = BigDecimal.ZERO; // total price of all products with discounts

    /**
     * The constructor for creating a receipt and calculating all its fields
     *
     * @throws QuantityException - when the number of products is less than required
     */
    public Check(List<Product> products, DiscountCard discountCard, Map<Integer, Integer> idsAndQuantity)
            throws QuantityException {

        this.products = products;
        this.discountCard = discountCard;
        productsDiscount = new HashMap<>();
        productsTotal = new HashMap<>();

        BigDecimal discountPercentage = BigDecimal.ZERO;
        if (discountCard != null) {
            discountPercentage = BigDecimal.valueOf(discountCard.getDiscount());
        }

        for (Product product : products) {
            int expectedQuantity = idsAndQuantity.get(product.getId());
            checkQuantityAndExchange(product, expectedQuantity);

            if (expectedQuantity >= WHOLESALE_COUNT && product.isWholesale()) {
                calculateProductTotalAndDiscount(product, WHOLESALE_PERCENT);
            } else {
                calculateProductTotalAndDiscount(product, discountPercentage);
            }
        }
    }

    /**
     * Calculation of all fields of the receipt and recording
     */
    private void calculateProductTotalAndDiscount(Product product, BigDecimal discountPercentage) {
        BigDecimal currentTotal = calculateProductTotal(product.getPrice(), product.getQuantity());
        productsTotal.put(product.getId(), currentTotal);

        BigDecimal currentDiscount = calculateProductDiscount(currentTotal, discountPercentage);
        productsDiscount.put(product.getId(), currentDiscount);

        BigDecimal currentTotalWithDiscount = calculateProductTotalWithDiscount(currentTotal, currentDiscount);

        totalDiscount = totalDiscount.add(currentDiscount);
        totalPrice = totalPrice.add(currentTotal);
        totalWithDiscount = totalWithDiscount.add(currentTotalWithDiscount);
    }

    private BigDecimal calculateProductTotal(BigDecimal productPrice, int quantity) {
        return productPrice.multiply(BigDecimal.valueOf(quantity));
    }

    private BigDecimal calculateProductDiscount(BigDecimal productTotal, BigDecimal discountPercentage) {
        //you need to use RoundingMode.HALF_UP, but idea 2022 doesn't see it
        return productTotal.multiply(discountPercentage).divide(BigDecimal.valueOf(100), 2, 4);
    }

    private BigDecimal calculateProductTotalWithDiscount(BigDecimal productTotal, BigDecimal productDiscount) {
        return productTotal.subtract(productDiscount);
    }

    /**
     * Checking the quantity and exchanging the value of the total quantity of product for the required one
     */
    private void checkQuantityAndExchange(Product product, int expectedQuantity) throws QuantityException {
        if (product.getQuantity() < expectedQuantity) {
            throw new QuantityException("The quantity of the product id - " + product.getId()
                    + " is less than requested");
        } else {
            product.setQuantity(expectedQuantity);
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    public Map<Integer, BigDecimal> getProductsDiscount() {
        return productsDiscount;
    }

    public Map<Integer, BigDecimal> getProductsTotal() {
        return productsTotal;
    }

    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public BigDecimal getTotalWithDiscount() {
        return totalWithDiscount;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
