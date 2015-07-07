package kata.vending;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the vending machine.
 */
public class MachineTest {
    /**
     * A machine to set up for tests.
     */
    private final Machine machine = new Machine.Builder().build();

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
     * Test that when invalid coins (coins with no classification) are
     * inserted, they go into the coin return of the machine.
     *
     * Test that valid coins do not end up in the coin return.
     */
    @Test
    public final void whenInsertInvalidCoinsTheyGoIntoCoinReturn() {
        final Bank coinReturn = machine
                .insertCoin(penny)
                .insertCoin(nickel)
                .insertCoin(dime)
                .insertCoin(quarter)
                .insertCoin(penny)
                .getCoinReturn();
        Map<Currency, Integer> crCurrencies = coinReturn.getCurrencies();
        assertEquals((int) crCurrencies.get(Currency.UNKNOWN), 2);
    }

    /**
     * Test that when there are no coins inserted (0 balance), the machine
     * displays INSERT COIN.
     */
    @Test
    public final void whenMachineBalanceIsZeroDisplayInsertCoin() {
        assertEquals("INSERT COIN", machine.getDisplay());
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

        assertEquals("$1.00", filled.getDisplay());
    }

    /**
     * Test that the machine continues to display the correct balance after
     * successive valid inserts.
     */
    @Test
    public final void whenInsertValidCoinsSuccessivelyDisplayIsCorrect() {
        final Machine one = machine.insertCoin(quarter);
        assertEquals("$0.25", one.getDisplay());
        final Machine two = one.insertCoin(nickel);
        assertEquals("$0.30", two.getDisplay());
        final Machine three = two.insertCoin(dime);
        assertEquals("$0.40", three.getDisplay());
        final Machine four = three.insertCoin(dime);
        assertEquals("$0.50", four.getDisplay());
    }

    /**
     * Test that when a mix of coins is inserted, the valid ones create a valid
     * display.
     */
    @Test
    public final void whenInsertMixedValidInvalidCoinsDisplayCorrect() {
        final Machine filled = machine
                .insertCoin(penny)
                .insertCoin(quarter)
                .insertCoin(dime)
                .insertCoin(nickel)
                .insertCoin(penny);
        assertEquals("$0.40", filled.getDisplay());
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
        Bank coinReturn = returned.getCoinReturn();
        Map<Currency, Integer> currencies = coinReturn.getCurrencies();
        assertEquals(1, (int) currencies.get(Currency.UNKNOWN));
        assertEquals(1, (int) currencies.get(Currency.QUARTER));
        assertEquals(1, (int) currencies.get(Currency.DIME));
        assertEquals(1, (int) currencies.get(Currency.NICKEL));
    }
}
