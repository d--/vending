package kata.vending;

/**
 * Represents the various types of products that the machine can contain and
 * their prices.
 */
public enum Product {
    /**
     * Cola, 1 dollar.
     */
    COLA(100L),

    /**
     * Chips, 50 cents.
     */
    CHIPS(50L),

    /**
     * Candy, 65 cents.
     */
    CANDY(65L);

    /**
     * The price of this product.
     */
    private final long price;

    public long getPrice() {
        return price;
    }

    /**
     * Product enum constructor.
     *
     * @param price the price of the product
     */
    Product(final long price) {
        this.price = price;
    }
}