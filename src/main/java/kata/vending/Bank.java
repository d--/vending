package kata.vending;

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
        return currencies;
    }

    /**
     * Calculate the balance (in cents) of the currently held currencies.
     * @return the current balance (in cents)
     */
    public Long calculateBalance() {
        Long balance = 0L;
        for (Map.Entry<Currency, Integer> entry : currencies.entrySet()) {
            balance += entry.getKey().getCents() * entry.getValue();
        }
        return balance;
    }

    /**
     * Bank constructor.
     * @param currencies the currencies to be held in the bank
     */
    public Bank(final Map<Currency, Integer> currencies) {
        this.currencies = currencies;
    }

    /**
     * Calculate the current balance of this bank and return it as a string.
     * @return the current balance string with a dollar sign
     */
    public String balanceAsString() {
        final int oneDollar = 100;
        final int tenCents = 10;
        final Long balance = this.calculateBalance();
        final Long dollars = balance / oneDollar;
        final Long cents = balance % oneDollar;
        String lower = cents.toString();
        if (cents < tenCents) {
            lower = '0' + lower;
        }
        return "$" + dollars.toString() + '.' + lower;
    }
}