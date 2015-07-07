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
     * Test that depositing an invalid currency does not increase the balance.
     */
    @Test
    public final void whenDepositInvalidCurrencyBalanceIsZero() {
        assertEquals(0, (long) bank
                .deposit(Currency.UNKNOWN)
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
}
