package kata.vending;

import java.util.SortedSet;

/**
 * Holds inventory of currency, calculates balance, makes change.
 * Immutable.
 */
public final class Bank {
    /**
     * Inventory of currencies for the bank.
     */
    private final Inventory<Currency> inventory;
    public Inventory<Currency> getInventory() {
        return inventory;
    }

    /**
     * Calculate the balance (in cents) of the currently held currencies.
     * @return the current balance (in cents)
     */
    public long calculateBalance() {
        long balance = 0L;
        for (Currency currency : Currency.values()) {
            balance += currency.getCents() * inventory.quantity(currency);
        }
        return balance;
    }

    /**
     * Create a bank made up of currencies from this bank that add up as close
     * as possible to the change amount requested.
     * @param amount the amount of change needed
     * @return a bank of change made from as much currency that could be taken
     */
    public Bank makeChange(final long amount) {
        long change = amount;
        Bank changeBank = new Bank();
        final SortedSet<Currency> descending = Currency.descending();
        for (Currency largest : descending) {
            int quantity = inventory.quantity(largest);
            final long cents = largest.getCents();
            while (quantity > 0 && change >= cents) {
                change = change - cents;
                quantity = quantity - 1;
                changeBank = changeBank.deposit(largest);
            }
        }
        return changeBank;
    }

    /**
     * Bank constructor.  Fills out a new bank with 0 amounts for each currency.
     */
    public Bank() {
        this.inventory = new Inventory<>(Currency.values());
    }

    /**
     * Private bank constructor.  Used for returning new, modified instances.
     * @param inventory the new inventory
     */
    private Bank(final Inventory<Currency> inventory) {
        this.inventory = new Inventory<>(Currency.values()).add(inventory);
    }

    /**
     * Deposit the currencies from the incoming bank into this bank.
     * @param bank incoming bank
     * @return new bank with this bank's currencies, incoming bank's currencies
     */
    public Bank deposit(final Bank bank) {
        if (bank == null) {
            return this;
        }
        return new Bank(inventory.add(bank.getInventory()));
    }

    /**
     * Deposit the currency incoming into this bank.
     * @param currency incoming currency
     * @return new bank with this bank's currencies, incoming currency
     */
    public Bank deposit(final Currency currency) {
        if (currency == null) {
            return this;
        }
        return new Bank(inventory.add(currency));
    }

    /**
     * Withdraws the currencies from the passed in Bank from this Bank.  If
     * there aren't enough of any particular currency, set the quantity to 0.
     * @param bank the bank of currencies to withdraw
     * @return a bank with the currencies of the passed bank withdrawn
     */
    public Bank withdraw(final Bank bank) {
        if (bank == null) {
            return this;
        }
        return new Bank(inventory.subtract(bank.getInventory()));
    }
}