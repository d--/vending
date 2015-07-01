package kata.vending;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the vending machine.
 */
public class MachineTest {
    /**
     * A machine to set up for tests.
     */
    private Machine machine;

    /**
     * Penny coin; not a currency.
     */
    private Coin penny = Currency.UNKNOWN.getCoin();

    /**
     * Nickel coin.
     */
    private Coin nickel = Currency.NICKEL.getCoin();

    /**
     * Dime coin.
     */
    private Coin dime = Currency.DIME.getCoin();

    /**
     * Quarter coin.
     */
    private Coin quarter = Currency.QUARTER.getCoin();

    /**
     * Set up a blank machine before each test.
     */
    @Before
    public final void setUpBlankMachine() {
        final List<Coin> emptyCoinReturn = Collections.emptyList();
        final Bank emptyBank =
                new Bank(Collections.<Currency, Integer>emptyMap());
        machine = new Machine.Builder()
                .customerBank(emptyBank)
                .coinReturn(emptyCoinReturn)
                .build();
    }

    /**
     * Test that when invalid coins (coins with no classification) are
     * inserted, they go into the coin return of the machine.
     *
     * Test that valid coins do not end up in the coin return.
     */
    @Test
    public final void whenInsertInvalidCoinsTheyGoIntoCoinReturn() {
        final List<Coin> coinReturn = machine
                .insertCoin(penny)
                .insertCoin(nickel)
                .insertCoin(dime)
                .insertCoin(quarter)
                .insertCoin(penny)
                .getCoinReturn();
        assertEquals(2, coinReturn.size());
        assertTrue(coinReturn.contains(penny));
    }

    /**
     * Test that when there are no coins inserted (0 balance), the machine
     * displays INSERT COIN.
     */
    @Test
    public final void whenMachineBalanceIsZeroDisplayInsertCoin() {
        assertEquals("INSERT COIN", machine.checkDisplay());
    }

    /**
     * Test that when valid coins are inserted, the machine displays the
     * current balance.
     */
    @Test
    public final void whenInsertValidCoinsMachineDisplaysCorrectBalance() {
        final Machine filled = machine
                .insertCoin(quarter)
                .insertCoin(quarter)
                .insertCoin(quarter)
                .insertCoin(dime)
                .insertCoin(dime)
                .insertCoin(nickel);

        assertEquals("$1.00", filled.checkDisplay());
    }

    /**
     * Test that the machine continues to display the correct balance after
     * successive valid inserts.
     */
    @Test
    public final void whenInsertValidCoinsSuccessivelyDisplayIsCorrect() {
        final Machine one = machine.insertCoin(quarter);
        assertEquals("$0.25", one.checkDisplay());
        final Machine two = one.insertCoin(nickel);
        assertEquals("$0.30", two.checkDisplay());
        final Machine three = two.insertCoin(dime);
        assertEquals("$0.40", three.checkDisplay());
        final Machine four = three.insertCoin(dime);
        assertEquals("$0.50", four.checkDisplay());
    }

    /**
     * Test that when a mix of coins is inserted, the valid ones create a valid
     * display.
     */
    @Test
    public final void whenInsertMixedValidInvalidCoinsDisplayCorrect() {
        final String display = machine
                .insertCoin(penny)
                .insertCoin(quarter)
                .insertCoin(dime)
                .insertCoin(nickel)
                .insertCoin(penny)
                .checkDisplay();
        assertEquals("$0.40", display);
    }

    /**
     * Test that when the coin return is pressed, the customer coins are
     * returned.
     */
    @Test
    public final void whenInsertCoinsCoinReturnReturnsCoins() {
        Machine filled = machine
            .insertCoin(penny)
            .insertCoin(quarter)
            .insertCoin(dime)
            .insertCoin(nickel);
        Machine returned = filled.returnCoins();
        List<Coin> coinReturn = returned.getCoinReturn();
        assertTrue(coinReturn.contains(penny));
        assertTrue(coinReturn.contains(quarter));
        assertTrue(coinReturn.contains(dime));
        assertTrue(coinReturn.contains(nickel));
    }
}
