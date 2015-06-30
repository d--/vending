package kata.vending;

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
     * Test that when invalid coins (coins with no classification) are
     * inserted, they go into the coin return of the machine.
     *
     * Test that valid coins do not end up with them.
     */
    @Test
    public final void whenInsertInvalidCoinsTheyGoIntoCoinReturn() {
        Machine machine = new Machine(Collections.<Coin>emptyList());
        Coin lead = new Coin(Coin.Size.SMALL, Coin.Weight.HEAVY);
        Coin nickel = new Coin(Coin.Size.MEDIUM, Coin.Weight.HEAVY);
        Coin dime = new Coin(Coin.Size.SMALL, Coin.Weight.LIGHT);
        Coin quarter = new Coin(Coin.Size.LARGE, Coin.Weight.HEAVY);
        Coin penny = new Coin(Coin.Size.MEDIUM, Coin.Weight.LIGHT);
        List<Coin> coinReturn = machine
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
}
