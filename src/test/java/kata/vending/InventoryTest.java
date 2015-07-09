package kata.vending;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Inventory class.
 */
public class InventoryTest {
    /**
     * A simple enum to throw in the Inventory generic.
     */
    public enum Fruit {
        /**
         * Delicious.
         */
        APPLE,

        /**
         * Also good.
         */
        BANANA,

        /**
         * White means green.
         */
        GRAPE
    }

    /**
     * Empty enum.
     */
    public enum Blank {

    }

    /**
     * Empty fruit inventory.
     */
    private Inventory<Fruit> empty = new Inventory<>(Fruit.values());

    /**
     * Test that when a blank enum is introduced, nothing breaks.
     */
    @Test
    public final void whenBlankEnumNothingBreaks() {
        Inventory<Blank> inventory = new Inventory<>(Blank.values());
        assertEquals(0, inventory.quantity(null));
    }

    /**
     * Test a simple construction.
     */
    @Test
    public final void whenConstructorCalledQuantitiesAreZero() {
        assertEquals(0, empty.quantity(Fruit.APPLE)
                + empty.quantity(Fruit.BANANA)
                + empty.quantity(Fruit.GRAPE));
    }

    /**
     * Test a simple add.
     */
    @Test
    public final void whenAddItemItemQuantityIncreases() {
        Inventory<Fruit> fruits = empty
                .add(Fruit.APPLE)
                .add(Fruit.BANANA)
                .add(Fruit.BANANA);
        assertEquals(1, fruits.quantity(Fruit.APPLE));
        assertEquals(2, fruits.quantity(Fruit.BANANA));
    }

    /**
     * Test a full inventory add.
     */
    @Test
    public final void whenAddInventoryItemQuantitiesCorrect() {
        Inventory<Fruit> apples = empty.add(Fruit.APPLE);
        Inventory<Fruit> bananas = empty.add(Fruit.BANANA);
        Inventory<Fruit> merged = apples.add(bananas);
        assertEquals(1, merged.quantity(Fruit.APPLE));
        assertEquals(1, merged.quantity(Fruit.BANANA));
    }

    /**
     * Test a simple item subtract.
     */
    @Test
    public final void whenSubtractInventoryItemQuantitiesCorrect() {
        Inventory<Fruit> three = empty
                .add(Fruit.GRAPE)
                .add(Fruit.GRAPE)
                .add(Fruit.GRAPE);
        assertEquals(2, three
                .subtract(Fruit.GRAPE)
                .quantity(Fruit.GRAPE));
    }

    /**
     * Test a simple subtract when empty inventory.
     */
    @Test
    public final void whenSubtractEmptyInventoryJustGetZero() {
        assertEquals(0, empty
                .subtract(Fruit.GRAPE)
                .subtract(Fruit.GRAPE)
                .quantity(Fruit.GRAPE));
    }

    /**
     * Test an inventory subtract.
     */
    @Test
    public final void whenSubtractInventoryQuantitiesCorrect() {
        Inventory<Fruit> three = empty
                .add(Fruit.APPLE)
                .add(Fruit.BANANA)
                .add(Fruit.GRAPE);
        Inventory<Fruit> notBanana = empty
                .add(Fruit.APPLE)
                .add(Fruit.GRAPE);
        Inventory<Fruit> justBanana = three.subtract(notBanana);
        assertEquals(0, justBanana.quantity(Fruit.APPLE));
        assertEquals(0, justBanana.quantity(Fruit.GRAPE));
        assertEquals(1, justBanana.quantity(Fruit.BANANA));
    }

    /**
     * Test a larger inventory subtracted from a smaller one.
     */
    @Test
    public final void whenSubtractLargerFromSmallerJustZero() {
        Inventory<Fruit> larger = empty.add(Fruit.APPLE);
        assertEquals(0, empty.subtract(larger).quantity(Fruit.APPLE));
    }

    /**
     * Test that null operations return the same inventory.
     */
    @Test
    public final void whenAddSubtractNullInventoryIsSame() {
        assertEquals(empty, empty.add((Fruit) null));
        assertEquals(empty, empty.add((Inventory<Fruit>) null));
        assertEquals(empty, empty.subtract((Fruit) null));
        assertEquals(empty, empty.subtract((Inventory<Fruit>) null));
    }

    /**
     * Test that the quantity of null is zero.
     */
    @Test
    public final void whenQuantifyNullGetZero() {
        assertEquals(0, empty.quantity(null));
    }
}
