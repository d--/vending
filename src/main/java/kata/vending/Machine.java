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
        this.coinReturn = builder.coinReturn;
        this.customerBank = builder.customerBank;
    }

    /**
     * Insert a coin into the vending machine.
     * @param coin the coin to insert
     * @return a vending machine with the coin inserted
     */
    public Machine insertCoin(final Coin coin) {
        final List<Coin> tmpCoinReturn = new ArrayList<>();
        final Map<Currency, Integer> currencies = new HashMap<>();
        tmpCoinReturn.addAll(this.coinReturn);
        currencies.putAll(customerBank.getCurrencies());

        final Currency coinCurrency = Currency.toCurrency(coin);
        if (coinCurrency == Currency.UNKNOWN) {
            tmpCoinReturn.add(coin);
        } else {
            Integer numOfCurrencyInCustomerBank = currencies.get(coinCurrency);
            if (numOfCurrencyInCustomerBank == null) {
                numOfCurrencyInCustomerBank = 0;
            }
            currencies.put(coinCurrency, numOfCurrencyInCustomerBank + 1);
        }

        final List<Coin> updatedCoinReturn =
                Collections.unmodifiableList(tmpCoinReturn);
        final Bank updatedCustomerBank = new Bank(currencies);
        return new Builder()
                .coinReturn(updatedCoinReturn)
                .customerBank(updatedCustomerBank)
                .build();
    }

    /**
     * Checks the display of the machine.
     * @return the current display
     */
    public String checkDisplay() {
        if (customerBank.calculateBalance() == 0L) {
            return "INSERT COIN";
        } else {
            return customerBank.balanceAsString();
        }
    }

    /**
     * Returns any coins that are in the customer bank to the coin return.
     * @return machine with appropriate coin return and customer bank
     */
    public Machine returnCoins() {
        final List<Coin> tmpCoinReturn = new ArrayList<>();
        tmpCoinReturn.addAll(this.coinReturn);

        final Map<Currency, Integer> bankMap = customerBank.getCurrencies();
        for (Map.Entry<Currency, Integer> entry : bankMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                tmpCoinReturn.add(entry.getKey().getCoin());
            }
        }

        final List<Coin> updatedCoinReturn =
            Collections.unmodifiableList(tmpCoinReturn);
        final Bank updatedCustomerBank =
            new Bank(Collections.<Currency, Integer>emptyMap());
        return new Builder()
            .coinReturn(updatedCoinReturn)
            .customerBank(updatedCustomerBank)
            .build();
    }
}
