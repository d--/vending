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
        return coinReturn;
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

        return new Builder()
                .coinReturn(Collections.unmodifiableList(tmpCoinReturn))
                .customerBank(new Bank(currencies))
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
}
