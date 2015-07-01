package kata.vending;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for logic contained in the Currency enum.
 */
public class CurrencyTest {
    /**
     * Test that a known coin converts to the correct currency representation.
     */
    @Test
    public final void whenConvertValidCoinCoinBecomesCurrency() {
        Coin valid = Currency.QUARTER.getCoin();
        assertEquals(Currency.toCurrency(valid), Currency.QUARTER);
    }

    /**
     * Test that an unknown coin converts to an unknown currency.
     */
    @Test
    public final void whenConvertInvalidCoinCoinBecomesUnknownCurrency() {
        Coin invalid = new Coin(null, null);
        assertEquals(Currency.toCurrency(invalid), Currency.UNKNOWN);
    }
}
