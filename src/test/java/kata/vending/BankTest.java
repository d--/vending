package kata.vending;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Bank class.
 */
public class BankTest {
    /**
     * A Bank to set up for tests.
     */
    private final Bank bank = new Bank();

    /**
     * Test that a new Bank's balance calculates to zero.
     */
    @Test
    public final void whenCreateNewBankBalanceIsZero() {
        assertEquals(0, (long) bank.calculateBalance());
    }

    /**
     * Test that depositing one new bank into another has a balance of zero.
     */
    @Test
    public final void whenDepositBlankBankIntoBlankBankBalanceIsZero() {
        assertEquals(0, (long) bank
                .deposit(new Bank())
                .calculateBalance());
    }

    /**
     * Test that depositing a null bank into another has a balance of zero.
     */
    @Test
    public final void whenDepositNullBankBalanceIsZero() {
        assertEquals(0, (long) bank
                .deposit((Bank) null)
                .calculateBalance());
    }

    /**
     * Test that depositing an invalid currency does not increase the balance.
     */
    @Test
    public final void whenDepositInvalidCurrencyBalanceIsZero() {
        assertEquals(0, (long) bank
                .deposit(Currency.UNKNOWN)
                .calculateBalance());
    }

    /**
     * Test that depositing a null currency doesn't break things.
     */
    @Test
    public final void whenDepositNullCurrencyBalanceIsZero() {
        assertEquals(0, (long) bank
                .deposit((Currency) null)
                .calculateBalance());
    }

    /**
     * Test that depositing valid currency has the appropriate balance.
     */
    @Test
    public final void whenDepositValidCurrencyBalanceIsAppropriate() {
        final Long value = Currency.QUARTER.getCents()
                + Currency.NICKEL.getCents();
        assertEquals(value, bank
                .deposit(Currency.QUARTER)
                .deposit(Currency.NICKEL)
                .calculateBalance());
    }

    /**
     * Test that depositing a bank with a positive balance merges the two.
     */
    @Test
    public final void whenDepositFilledBankBalanceIsAppropriate() {
        final Long value = Currency.QUARTER.getCents()
                + Currency.DIME.getCents();
        final Bank one = bank.deposit(Currency.QUARTER);
        final Bank two = bank.deposit(Currency.DIME);
        assertEquals(value, one.deposit(two).calculateBalance());
    }

    /**
     * Test that making zero change works.
     */
    @Test
    public final void whenMakeChangeOfZeroGetEmptyBank() {
        assertEquals(0, (long) bank.makeChange(0L).calculateBalance());
    }

    /**
     * Test that failing to make change works.
     */
    @Test
    public final void whenFailureToMakeChangeDoBestFit() {
        assertEquals(50L, (long) bank
                .deposit(Currency.QUARTER)
                .deposit(Currency.QUARTER)
                .deposit(Currency.DIME)
                .makeChange(55L)
                .calculateBalance());
    }

    /**
     * Test that failing to make any change at all works.
     */
    @Test
    public final void whenFailureToMakeAnyChangeDoNothing() {
        assertEquals(0L, (long) bank
                .deposit(Currency.QUARTER)
                .makeChange(10L)
                .calculateBalance());
    }

    /**
     * Test that change is made correctly even with multiple small currencies.
     */
    @Test
    public final void whenNotEnoughQuartersUseNickels() {
        Bank nickels = bank
                .deposit(Currency.QUARTER)
                .deposit(Currency.NICKEL)
                .deposit(Currency.NICKEL)
                .deposit(Currency.NICKEL)
                .deposit(Currency.NICKEL)
                .deposit(Currency.NICKEL);
        assertEquals(50L, (long) nickels
                .makeChange(50L)
                .calculateBalance());
        assertEquals(5, (int) nickels.getCurrencies().get(Currency.NICKEL));
    }
}
