package kata.vending;

import org.junit.Before;
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
     * Set up a machine with a filled bank and inventory.
     */
    @Before
    public final void setUpMachine() {
        Bank machineBank = new Bank();
        for (Currency currency : Currency.values()) {
            for (int i = 0; i < 10; i++) {
                machineBank = machineBank.deposit(currency);
            }
        }
        machine = new Machine
            .Builder()
            .machineBank(machineBank)
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
    public final void whenCustomerBankIsZeroDisplayInsertCoin() {
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

    /**
     * Test that when enough money is inserted, a vend displays the correct
     * messages.
     */
    @Test
    public final void whenEnoughMoneyVendDisplayIsCorrect() {
        Machine vended = machine
            .insertCoin(quarter)
            .insertCoin(quarter)
            .vend(Product.CHIPS);
        String vendingDisplay = vended.getDisplay();
        assertEquals("THANK YOU", vendingDisplay);
        String displayAfter = vended.checkDisplay().getDisplay();
        assertEquals("INSERT COIN", displayAfter);
    }

    /**
     * Test that when not enough money is inserted, a vend displays the price,
     * then displays the amount inserted.
     */
    @Test
    public final void whenNotEnoughMoneyVendDisplayIsCorrect() {
        Machine nope = machine
            .insertCoin(nickel)
            .vend(Product.CANDY);
        String vendingDisplay = nope.getDisplay();
        assertEquals(vendingDisplay, "PRICE $0.65");
        String displayAfter = nope.checkDisplay().getDisplay();
        assertEquals("$0.05", displayAfter);
    }


    /**
     * Test that when too much money is inserted, a vend makes correct change.
     */
    @Test
    public final void whenTooMuchMoneyVendMakesCorrectChange() {
        Bank bankWithDime = new Bank().deposit(Currency.DIME);
        Machine machineWithDime = new Machine
            .Builder()
            .machineBank(bankWithDime)
            .build();
        Machine vended = machineWithDime
            .insertCoin(quarter)
            .insertCoin(quarter)
            .insertCoin(quarter)
            .vend(Product.CANDY);
        Bank coinReturn = vended.getCoinReturn();
        assertEquals(Currency.DIME.getCents(), coinReturn.calculateBalance());
        assertEquals(1, (long) coinReturn.getCurrencies().get(Currency.DIME));
    }

    /**
     * Test that when the display is checked, an inability to make correct
     * change is met with "EXACT CHANGE ONLY".
     */
    @Test
    public final void whenNotEnoughChangeDisplayExactChangeOnly() {
        Machine machineWithBank = new Machine.Builder().build();
        assertEquals("EXACT CHANGE ONLY", machineWithBank
            .checkDisplay()
            .getDisplay());
    }

    /**
     * Test that banks are in the appropriate state when the customer ignores
     * EXACT CHANGE ONLY.
     */
    @Test
    public final void whenNotEnoughChangeButNeedChangeAnyway() {
        Machine blank = new Machine.Builder().build();
        assertEquals("EXACT CHANGE ONLY", blank.checkDisplay().getDisplay());
        Machine vended = blank
                .insertCoin(quarter)
                .insertCoin(quarter)
                .insertCoin(quarter)
                .vend(Product.CANDY);
        Bank coinReturn = vended.getCoinReturn();
        assertEquals(0, (long) coinReturn.calculateBalance());
        assertEquals(0, (long) vended.getCustomerBank().calculateBalance());
        assertEquals(75L, (long) vended.getMachineBank().calculateBalance());
        assertEquals("THANK YOU", vended.getDisplay());
        assertEquals("EXACT CHANGE ONLY", vended.checkDisplay().getDisplay());
    }
}
