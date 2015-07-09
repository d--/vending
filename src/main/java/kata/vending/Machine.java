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
    public Bank getCustomerBank() {
        return customerBank;
    }

    /**
     * The current bank of currencies stocked in the machine.
     */
    private final Bank machineBank;
    public Bank getMachineBank() {
        return machineBank;
    }

    /**
     * The current inventory of products.
     */
    private final Inventory<Product> inventory;
    public Inventory<Product> getInventory() {
        return inventory;
    }

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

        /** Builder machineBank. */
        private Bank machineBank;

        /** Builder inventory. */
        private Inventory<Product> inventory;

        /** Builder display. */
        private String display;

        /**
         * Builder setter for coinReturn.
         * @param coinReturn coinReturn
         * @return this Builder
         */
        public final Builder coinReturn(final Bank coinReturn) {
            this.coinReturn = coinReturn;
            return this;
        }

        /**
         * Builder setter for customerBank.
         * @param customerBank customerBank
         * @return this Builder
         */
        public final Builder customerBank(final Bank customerBank) {
            this.customerBank = customerBank;
            return this;
        }

        /**
         * Builder setter for machineBank.
         * @param machineBank machineBank
         * @return this Builder
         */
        public final Builder machineBank(final Bank machineBank) {
            this.machineBank = machineBank;
            return this;
        }

        /**
         * Builder setter for inventory.
         * @param inventory inventory
         * @return this Builder
         */
        public final Builder inventory(final Inventory<Product> inventory) {
            this.inventory = inventory;
            return this;
        }

        /**
         * Builder setter for display.
         * @param display display
         * @return this Builder
         */
        public final Builder display(final String display) {
            this.display = display;
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
            this.machineBank = machine.machineBank;
            this.inventory = machine.inventory;
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
        this.coinReturn = new Bank().deposit(builder.coinReturn);
        this.customerBank = new Bank().deposit(builder.customerBank);
        this.machineBank = new Bank().deposit(builder.machineBank);
        this.inventory = new Inventory<>(Product.values())
                .add(builder.inventory);
        if (builder.display == null) {
            this.display = "INSERT COIN";
        } else {
            this.display = builder.display;
        }
    }

    /**
     * Return the passed in balance as a formatted currency string.
     * @param balance the balance to turn into a string
     * @return the current balance string with a dollar sign
     */
    private String balanceAsString(final Long balance) {
        final int oneDollar = 100;
        final int tenCents = 10;
        final Long dollars = balance / oneDollar;
        final Long cents = balance % oneDollar;
        String lower = cents.toString();
        if (cents < tenCents) {
            lower = '0' + lower;
        }
        return "$" + dollars.toString() + '.' + lower;
    }

    /**
     * Insert a coin into the vending machine.
     * @param coin the coin to insert
     * @return a vending machine with the coin inserted
     */
    public Machine insertCoin(final Coin coin) {
        final Builder builder = new Builder(this);
        final Currency currency = Currency.toCurrency(coin);
        if (currency == Currency.UNKNOWN) {
            builder.coinReturn(coinReturn.deposit(currency));
        } else {
            final Bank updated = customerBank.deposit(currency);
            builder.customerBank(updated);
            builder.display(balanceAsString(updated.calculateBalance()));
        }
        return builder.build();
    }

    /**
     * Returns any coins that are in the customer bank to the coin return.
     * @return machine with appropriate coin return and customer bank
     */
    public Machine returnCoins() {
        final Bank newCustomerBank = new Bank();
        return new Builder(this)
            .coinReturn(coinReturn.deposit(customerBank))
            .customerBank(newCustomerBank)
            .display(balanceAsString(newCustomerBank.calculateBalance()))
            .build();
    }

    /**
     * Calculate whether the machine can make change for each of the products.
     * @return true if the machine can make change, false if it can't
     */
    private boolean canMakeChange() {
        for (Product product : Product.values()) {
            final Long price = product.getPrice();
            final Bank changeAttempt = machineBank.makeChange(price);
            if (!changeAttempt.calculateBalance().equals(price)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks the display of the machine.  This is an action that recalculates
     * the display state.  This does not return the display.  This returns a
     * machine with the display having been checked.
     * @return the machine with the display having been checked
     */
    public Machine checkDisplay() {
        final Builder builder = new Builder(this);
        final Long balance = customerBank.calculateBalance();
        if (balance == 0) {
            builder.display("INSERT COIN");
            if (!canMakeChange()) {
                builder.display("EXACT CHANGE ONLY");
            }
        } else {
            builder.display(balanceAsString(balance));
        }
        return builder.build();
    }

    /**
     * Vends a product from the machine if customer funds are sufficient.
     *
     * @param product the product to dispense
     * @return a new machine with properties appropriately adjusted
     */
    public Machine vend(final Product product) {
        final Builder builder = new Builder(this);
        if (inventory.quantity(product) <= 0) {
            builder.display("SOLD OUT");
            return builder.build();
        }
        final Long balance = customerBank.calculateBalance();
        final Long price = product.getPrice();
        if (balance >= price) {
            final Long change = balance - price;
            final Bank combined = machineBank.deposit(customerBank);
            final Bank changeBank = combined.makeChange(change);
            final Bank withdrawn = combined.withdraw(changeBank);
            builder
                .machineBank(withdrawn)
                .customerBank(new Bank())
                .coinReturn(changeBank)
                .inventory(inventory.subtract(product))
                .display("THANK YOU");
        } else {
            builder.display("PRICE " + balanceAsString(product.getPrice()));
        }
        return builder.build();
    }
}
