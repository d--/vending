package kata.vending;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

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
        for (Currency currency : Currency.values()) {
            balance += currency.getCents() * currencies.get(currency);
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
        this.currencies = Collections.unmodifiableMap(currencies);
    }

    /**
     * Deposit the currencies in the incoming bank into this bank.
     * @param bank incoming bank
     * @return a new bank with all its own currencies and all from the incoming
     */
    public Bank deposit(final Bank bank) {
        if (bank == null) {
            return this;
        }
        final HashMap<Currency, Integer> freshMap = new HashMap<>();
        for (Currency currency : Currency.values()) {
            freshMap.put(currency,
                currencies.get(currency) + bank.getCurrencies().get(currency));
        }
        return new Bank(Collections.unmodifiableMap(freshMap));
    }

    /**
     * Deposit the currency incoming into this bank.
     * @param currency incoming currency
     * @return a new bank with all its own currencies and the incoming
     */
    public Bank deposit(final Currency currency) {
        if (currency == null) {
            return this;
        }
        final HashMap<Currency, Integer> freshMap = new HashMap<>(currencies);
        freshMap.put(currency, currencies.get(currency) + 1);
        return new Bank(Collections.unmodifiableMap(freshMap));
    }

    /**
     * Create a bank made up of currencies from this bank that add up as close
     * as possible to the change amount requested.
     * @param amount the amount of change needed
     * @return a bank of change made from as much currency that could be taken
     */
    public Bank makeChange(final Long amount) {
        Long change = amount;
        Bank changeBank = new Bank();
        final SortedSet<Currency> descending = Currency.descending();
        for (Currency largest : descending) {
            Integer quantity = currencies.get(largest);
            final Long cents = largest.getCents();
            while (quantity > 0 && change >= cents) {
                change = change - cents;
                quantity = quantity - 1;
                changeBank = changeBank.deposit(largest);
            }
        }
        return changeBank;
    }

    /**
     * Withdraws the currencies from the passed in Bank from this Bank.  If
     * there aren't enough of any particular currency, set the quantity to 0.
     * @param bank the bank of currencies to withdraw
     * @return a bank with the currencies of the passed bank withdrawn
     */
    public Bank withdraw(final Bank bank) {
        final HashMap<Currency, Integer> freshMap = new HashMap<>();
        final Map<Currency, Integer> toWithdraw = bank.getCurrencies();
        for (Currency currency : Currency.values()) {
            final Integer quantity = currencies.get(currency);
            final Integer subtract = toWithdraw.get(currency);
            if (subtract >= quantity) {
                freshMap.put(currency, 0);
            } else {
                freshMap.put(currency, quantity - subtract);
            }
        }
        return new Bank(Collections.unmodifiableMap(freshMap));
    }
}