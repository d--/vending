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
     * Lead coin; not a currency.
     */
    private Coin lead = new Coin(Coin.Size.SMALL, Coin.Weight.HEAVY);

    /**
     * Penny coin; not a currency.
     */
    private Coin penny = new Coin(Coin.Size.MEDIUM, Coin.Weight.LIGHT);

    /**
     * Nickel coin.
     */
    private Coin nickel = new Coin(Coin.Size.MEDIUM, Coin.Weight.HEAVY);

    /**
     * Dime coin.
     */
    private Coin dime = new Coin(Coin.Size.SMALL, Coin.Weight.LIGHT);

    /**
     * Quarter coin.
     */
    private Coin quarter = new Coin(Coin.Size.LARGE, Coin.Weight.HEAVY);

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
                .insertCoin(lead)
                .insertCoin(nickel)
                .insertCoin(dime)
                .insertCoin(quarter)
                .insertCoin(penny)
                .getCoinReturn();
        assertEquals(2, coinReturn.size());
        assertTrue(coinReturn.contains(penny));
        assertTrue(coinReturn.contains(lead));
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
}
