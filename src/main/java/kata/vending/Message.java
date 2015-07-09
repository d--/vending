package kata.vending;

/**
 * An enum for the vending machine message strings.
 */
public enum Message {
    /**
     * Insert coin.
     */
    INSERT_COIN("INSERT COIN"),

    /**
     * Exact change only.
     */
    EXACT_CHANGE("EXACT CHANGE ONLY"),

    /**
     * Sold out.
     */
    SOLD_OUT("SOLD OUT"),

    /**
     * Thank you.
     */
    THANK_YOU("THANK YOU"),

    /**
     * Price.
     */
    PRICE("PRICE");

    /**
     * The text of this message.
     */
    private final String text;

    public String getText() {
        return text;
    }

    /**
     * Message enum constructor.
     *
     * @param text the text of the message
     */
    Message(final String text) {
        this.text = text;
    }
}
