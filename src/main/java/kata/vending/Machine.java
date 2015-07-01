package kata.vending;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a vending machine and its behavior.
 * Immutable.
 */
public final class Machine {
    /**
     * List of coins representing the coin return slot of the machine.
     */
    private final List<Coin> coinReturn;
    public List<Coin> getCoinReturn() {
        return Collections.unmodifiableList(coinReturn);
    }

    /**
     * The current bank of currencies of the vending machine customer.
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
        /**
         * Builder coinReturn.
         */
        private List<Coin> coinReturn;

        /**
         * Builder customerBank.
         */
        private Bank customerBank;

        /**
         * Builder display.
         */
        private String display;

        /**
         * Builder setter for coinReturn.
         * @param newCoinReturn coinReturn
         * @return this Builder
         */
        public final Builder coinReturn(final List<Coin> newCoinReturn) {
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
            this.coinReturn = Collections.emptyList();
        } else {
            this.coinReturn = builder.coinReturn;
        }
        if (builder.customerBank == null) {
            this.customerBank =
                new Bank(Collections.<Currency, Integer>emptyMap());
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
        final List<Coin> coinReturn = new ArrayList<>();
        coinReturn.addAll(this.coinReturn);
        final Map<Currency, Integer> customerCash = new HashMap<>();
        customerCash.putAll(customerBank.getCurrencies());

        final Currency currency = Currency.toCurrency(coin);
        if (currency == Currency.UNKNOWN) {
            coinReturn.add(coin);
        } else {
            if (customerCash.containsKey(currency)) {
                customerCash.put(currency, customerCash.get(currency) + 1);
            } else {
                customerCash.put(currency, 1);
            }
        }

        final List<Coin> updatedCoinReturn =
                Collections.unmodifiableList(coinReturn);
        final Bank updatedCustomerBank = new Bank(customerCash);
        return new Builder()
                .coinReturn(updatedCoinReturn)
                .customerBank(updatedCustomerBank)
                .display(updatedCustomerBank.balanceAsString())
                .build();
    }

    /**
     * Returns any coins that are in the customer bank to the coin return.
     * @return machine with appropriate coin return and customer bank
     */
    public Machine returnCoins() {
        final List<Coin> currentCoinReturn = new ArrayList<>();
        currentCoinReturn.addAll(this.coinReturn);

        final Map<Currency, Integer> cash = customerBank.getCurrencies();
        for (Map.Entry<Currency, Integer> currencyQuantity : cash.entrySet()) {
            for (int i = 0; i < currencyQuantity.getValue(); i++) {
                currentCoinReturn.add(currencyQuantity.getKey().getCoin());
            }
        }

        final List<Coin> updatedCoinReturn =
            Collections.unmodifiableList(currentCoinReturn);
        final Bank updatedCustomerBank =
            new Bank(Collections.<Currency, Integer>emptyMap());
        return new Builder()
            .coinReturn(updatedCoinReturn)
            .display(updatedCustomerBank.balanceAsString())
            .build();
    }
}
