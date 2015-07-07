package kata.vending;

/**
 * Represents a vending machine and its behavior.
 * Immutable.
 */
public final class Machine {
    /**
     * Bank representing the coin return slot of the machine.
     */
    private final Bank coinReturn;
    public Bank getCoinReturn() {
        return coinReturn;
    }

    /**
     * The current bank of currencies inserted by the vending machine customer.
     */
    private final Bank customerBank;

    /**
     * The current display of the vending machine.
     */
    private final String display;
    public String getDisplay() {
        return display;
    }

    /**
     * Builder for the immutable Machine class.
     */
    public static class Builder {
        /** Builder coinReturn. */
        private Bank coinReturn;

        /** Builder customerBank. */
        private Bank customerBank;

        /** Builder display. */
        private String display;

        /**
         * Builder setter for coinReturn.
         * @param newCoinReturn coinReturn
         * @return this Builder
         */
        public final Builder coinReturn(final Bank newCoinReturn) {
            this.coinReturn = newCoinReturn;
            return this;
        }

        /**
         * Builder setter for customerBank.
         * @param newCustomerBank customerBank
         * @return this Builder
         */
        public final Builder customerBank(final Bank newCustomerBank) {
            this.customerBank = newCustomerBank;
            return this;
        }

        /**
         * Builder setter for display.
         * @param newDisplay display
         * @return this Builder
         */
        public final Builder display(final String newDisplay) {
            this.display = newDisplay;
            return this;
        }

        /**
         * Default constructor.
         */
        public Builder() {

        }

        /**
         * Machine copy constructor.
         * @param machine the machine to copy properties from
         */
        public Builder(final Machine machine) {
            this.coinReturn = machine.coinReturn;
            this.customerBank = machine.customerBank;
            this.display = machine.display;
        }

        /**
         * Build a new instance of Machine.
         * @return the new Machine instance
         */
        public final Machine build() {
            return new Machine(this);
        }
    }

    /**
     * Machine constructor.
     * Private to enforce creation through builder.
     * @param builder the machine builder
     */
    private Machine(final Builder builder) {
        if (builder.coinReturn == null) {
            this.coinReturn = new Bank();
        } else {
            this.coinReturn = builder.coinReturn;
        }

        if (builder.customerBank == null) {
            this.customerBank = new Bank();
        } else {
            this.customerBank = builder.customerBank;
        }

        if (builder.display == null) {
            this.display = "INSERT COIN";
        } else {
            this.display = builder.display;
        }
    }

    /**
     * Insert a coin into the vending machine.
     * @param coin the coin to insert
     * @return a vending machine with the coin inserted
     */
    public Machine insertCoin(final Coin coin) {
        Builder builder = new Builder(this);
        final Currency currency = Currency.toCurrency(coin);
        if (currency == Currency.UNKNOWN) {
            builder.coinReturn(new Bank()
                    .deposit(this.coinReturn)
                    .deposit(currency));
        } else {
            Bank updated = customerBank.deposit(currency);
            builder.customerBank(updated);
            builder.display(Bank.balanceAsString(updated.calculateBalance()));
        }

        return builder.build();
    }

    /**
     * Returns any coins that are in the customer bank to the coin return.
     * @return machine with appropriate coin return and customer bank
     */
    public Machine returnCoins() {
        Bank newCustomerBank = new Bank();
        return new Builder(this)
            .coinReturn(coinReturn.deposit(customerBank))
            .customerBank(newCustomerBank)
            .display(Bank.balanceAsString(
                    newCustomerBank.calculateBalance()
            ))
            .build();
    }
}
