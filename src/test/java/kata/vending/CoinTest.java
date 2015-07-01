package kata.vending;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the coin class.
 */
public class CoinTest {
    /**
     * Test equals override with wrong object instance.
     */
    @Test
    public final void whenWrongObjectInstanceTypeEqualsReturnsFalse() {
        String incorrect = "wrong";
        Coin coin = new Coin(Coin.Size.LARGE, Coin.Weight.HEAVY);
        assertFalse(coin.equals(incorrect));
    }

    /**
     * Test equals with similar coins.
     */
    @Test
    public final void whenSimilarCoinsEqualsReturnsTrue() {
        Coin a = new Coin(Coin.Size.LARGE, Coin.Weight.HEAVY);
        Coin b = new Coin(Coin.Size.LARGE, Coin.Weight.HEAVY);
        assertTrue(a.equals(b) && b.equals(a));
    }

    /**
     * Test equals with dissimilar coins.
     */
    @Test
    public final void whenDissimilarCoinsEqualsReturnsFalse() {
        Coin a = new Coin(Coin.Size.LARGE, Coin.Weight.HEAVY);
        Coin b = new Coin(Coin.Size.LARGE, Coin.Weight.LIGHT);
        assertFalse(a.equals(b) || b.equals(a));
    }

    /**
     * Test hashcode gives the same hash for similar items.
     */
    @Test
    public final void whenSimilarItemsHashcodeIsSame() {
        Coin a = new Coin(Coin.Size.LARGE, Coin.Weight.HEAVY);
        Coin b = new Coin(Coin.Size.LARGE, Coin.Weight.HEAVY);
        assertEquals(a.hashCode(), b.hashCode());
    }

    /**
     * Test that equals doesn't blow up when a coin has null properties.
     */
    @Test
    public final void whenCoinHasNullPropertiesEqualsSucceeds() {
        Coin a = new Coin(Coin.Size.LARGE, null);
        Coin b = new Coin(null, Coin.Weight.HEAVY);
        assertFalse(a.equals(b) || b.equals(a));
    }

    /**
     * Test hashcode gives different hash for different items.
     */
    @Test
    public final void whenDifferentItemsHashcodeIsDifferent() {
        Coin a = new Coin(Coin.Size.SMALL, Coin.Weight.HEAVY);
        Coin b = new Coin(Coin.Size.SMALL, Coin.Weight.LIGHT);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    /**
     * Test that hashcode doesn't blow up on null properties.
     */
    @Test
    public final void whenCoinHasNullPropertiesHashcodeSucceeds() {
        Coin coin = new Coin(null, null);
        assertNotEquals(0, coin.hashCode());
    }
}
