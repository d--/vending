package kata.vending;

import org.junit.Before;
import org.junit.Test;

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
     * An inventory to fill for tests.
     */
    private Inventory<Product> inventory;

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
    public final void setUpMachineAndInventory() {
        Bank machineBank = new Bank();
        inventory = new Inventory<>(Product.values());
        for (Currency currency : Currency.values()) {
            for (int i = 0; i < 10; i++) {
                machineBank = machineBank.deposit(currency);
            }
        }
        for (Product product : Product.values()) {
            for (int i = 0; i < 10; i++) {
                inventory = inventory.add(product);
            }
        }
        machine = new Machine
                .Builder()
                .machineBank(machineBank)
                .inventory(inventory)
                .build();
    }

    /**
     * Test that when invalid coins (coins with no classification) are
     * inserted, they go into the coin return of the machine.
     * <p/>
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
        assertEquals(2, coinReturn
                .getInventory()
                .quantity(Currency.UNKNOWN));
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
        Inventory<Currency> currencies = machine
                .insertCoin(penny)
                .insertCoin(quarter)
                .insertCoin(dime)
                .insertCoin(nickel)
                .returnCoins()
                .getCoinReturn()
                .getInventory();
        assertEquals(1, currencies.quantity(Currency.UNKNOWN));
        assertEquals(1, currencies.quantity(Currency.QUARTER));
        assertEquals(1, currencies.quantity(Currency.DIME));
        assertEquals(1, currencies.quantity(Currency.NICKEL));
    }

    /**
     * Test that "null" is treated as an unknown currency when inserted.
     */
    @Test
    public final void whenInsertNullCoinCoinReturnHasUnknown() {
        assertEquals(1, machine
                .insertCoin(null)
                .getCoinReturn()
                .getInventory()
                .quantity(Currency.UNKNOWN));
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
        assertEquals("PRICE $0.65", vendingDisplay);
        String displayAfter = nope.checkDisplay().getDisplay();
        assertEquals("$0.05", displayAfter);
    }


    /**
     * Test that when too much money is inserted, a vend makes correct change.
     */
    @Test
    public final void whenTooMuchMoneyVendMakesCorrectChange() {
        Bank coinReturn = new Machine
                .Builder()
                .machineBank(new Bank().deposit(Currency.DIME))
                .inventory(inventory)
                .build()
                .insertCoin(quarter)
                .insertCoin(quarter)
                .insertCoin(quarter)
                .vend(Product.CANDY)
                .getCoinReturn();
        assertEquals(Currency.DIME.getCents(), coinReturn.calculateBalance());
        assertEquals(1, coinReturn.getInventory().quantity(Currency.DIME));
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
        Machine blank = new Machine.Builder().inventory(inventory).build();
        assertEquals("EXACT CHANGE ONLY", blank.checkDisplay().getDisplay());
        Machine vended = blank
                .insertCoin(quarter)
                .insertCoin(quarter)
                .insertCoin(quarter)
                .vend(Product.CANDY);
        Bank coinReturn = vended.getCoinReturn();
        assertEquals(0, coinReturn.calculateBalance());
        assertEquals(0, vended.getCustomerBank().calculateBalance());
        assertEquals(75L, vended.getMachineBank().calculateBalance());
        assertEquals("THANK YOU", vended.getDisplay());
        assertEquals("EXACT CHANGE ONLY", vended.checkDisplay().getDisplay());
    }

    /**
     * Test that when a product is sold out, the display is updated correctly
     * and the customer is not charged, and the money remains in the machine to
     * be used for another purchase.
     */
    @Test
    public final void whenProductIsSoldOutDisplaySoldOutOnly() {
        Machine prepped = new Machine
                .Builder(machine)
                .inventory(null)
                .build()
                .insertCoin(nickel)
                .vend(Product.CANDY);

        assertEquals("SOLD OUT", prepped.getDisplay());
        assertEquals(0, prepped.getCoinReturn().calculateBalance());
        assertEquals(5L, prepped.getCustomerBank().calculateBalance());
    }

    /**
     * Test that a proper vend creates the proper inventory.
     */
    @Test
    public final void whenVendProductInventoryCorrect() {
        assertEquals(inventory.quantity(Product.CHIPS) - 1, machine
                .insertCoin(quarter)
                .insertCoin(quarter)
                .vend(Product.CHIPS)
                .getInventory()
                .quantity(Product.CHIPS));
    }

    /**
     * Test that attempting to vend null simply says "SOLD OUT".
     */
    @Test
    public final void whenVendNullSoldOut() {
        assertEquals("SOLD OUT", machine.vend(null).getDisplay());
    }
}
