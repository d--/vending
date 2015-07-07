package kata.vending;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds currencies and numbers of each, and can calculate current balance.
 * Immutable.
 */
public final class Bank {
    /**
     * The current currencies and their quantities.
     */
    private final Map<Currency, Integer> currencies;
    public Map<Currency, Integer> getCurrencies() {
        return Collections.unmodifiableMap(currencies);
    }

    /**
     * Calculate the balance (in cents) of the currently held currencies.
     * @return the current balance (in cents)
     */
    public Long calculateBalance() {
        Long balance = 0L;
        for (Map.Entry<Currency, Integer> entry : currencies.entrySet()) {
            Currency currencyType = entry.getKey();
            Integer quantity = entry.getValue();
            balance += currencyType.getCents() * quantity;
        }
        return balance;
    }

    /**
     * Bank constructor.  Fills out a new bank with 0 amounts for each currency.
     */
    public Bank() {
        final Map<Currency, Integer> blankMap = new HashMap<>();
        for (Currency currency : Currency.values()) {
            blankMap.put(currency, 0);
        }
        this.currencies = Collections.unmodifiableMap(blankMap);
    }

    /**
     * Private bank constructor.  Used for returning new, modified instances.
     * @param currencies the currencies to instantiate with
     */
    private Bank(final Map<Currency, Integer> currencies) {
        this.currencies = currencies;
    }

    /**
     * Deposit the currencies in the incoming bank into this bank.
     * @param bank incoming bank
     * @return a new bank with all its own currencies and all from the incoming
     */
    public Bank deposit(final Bank bank) {
        final Map<Currency, Integer> blankMap = new HashMap<>();
        for (Map.Entry<Currency, Integer> entry
                : bank.getCurrencies().entrySet()) {
            final Currency currencyType = entry.getKey();
            final int numIncoming = entry.getValue();
            final int numInBank = this.currencies.get(currencyType);
            blankMap.put(currencyType, numInBank + numIncoming);
        }
        return new Bank(Collections.unmodifiableMap(blankMap));
    }

    /**
     * Deposit the currency incoming into this bank.
     * @param currency incoming currency
     * @return a new bank with all its own currencies and the incoming
     */
    public Bank deposit(final Currency currency) {
        final Map<Currency, Integer> blankMap = new HashMap<>();
        for (Map.Entry<Currency, Integer> entry : currencies.entrySet()) {
            final Currency currencyType = entry.getKey();
            final Integer quantity = entry.getValue();
            if (currencyType.equals(currency)) {
                blankMap.put(currencyType, quantity + 1);
            } else {
                blankMap.put(currencyType, quantity);
            }
        }
        return new Bank(Collections.unmodifiableMap(blankMap));
    }

    /**
     * Calculate the current balance of this bank and return it as a string.
     *
     * @param balance the balance to turn into a string
     * @return the current balance string with a dollar sign
     */
    public static String balanceAsString(final Long balance) {
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
}