package kata.vending;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A generic inventory class that holds quantities of enums.
 * Immutable.
 * @param <T> an enum that you want to keep quantities of
 */
public final class Inventory<T extends Enum> {
    /**
     * Holds the quantities for each of the items.
     */
    private final Map<T, Integer> items;
    public Map<T, Integer> getItems() {
        return items;
    }

    /**
     * The enum items to keep quantities of.
     */
    private final T[] enums;

    /**
     * Constructor to be called with enum.values().  Zeroes each quantity.
     * @param enums enum values to keep quantities of
     */
    public Inventory(final T[] enums) {
        final Map<T, Integer> blankMap = new HashMap<>();
        for (T t : enums) {
            blankMap.put(t, 0);
        }
        this.items = Collections.unmodifiableMap(blankMap);
        this.enums = Arrays.copyOf(enums, enums.length);
    }

    /**
     * Private constructor for creating a whole new Inventory from a raw map.
     * @param enums enum values to keep quantities of
     * @param items the internal representation of the inventory
     */
    private Inventory(final T[] enums, final Map<T, Integer> items) {
        this.items = Collections.unmodifiableMap(items);
        this.enums = Arrays.copyOf(enums, enums.length);
    }

    /**
     * Add an item to the inventory.
     * @param item the item to add
     * @return an inventory with the item added
     */
    public Inventory<T> add(final T item) {
        if (item == null) {
            return this;
        }
        final HashMap<T, Integer> freshMap = new HashMap<>(items);
        freshMap.put(item, items.get(item) + 1);
        return new Inventory<T>(enums, Collections.unmodifiableMap(freshMap));
    }

    /**
     * Add the entirety of an inventory to this inventory.
     * @param inventory the inventory to add
     * @return an inventory with the inventory added to this one
     */
    public Inventory<T> add(final Inventory<T> inventory) {
        if (inventory == null) {
            return this;
        }
        final HashMap<T, Integer> freshMap = new HashMap<>();
        for (T t : enums) {
            freshMap.put(t, inventory.quantity(t) + items.get(t));
        }
        return new Inventory<T>(enums, Collections.unmodifiableMap(freshMap));
    }

    /**
     * Subtract a single item from this inventory.
     * @param item the item to subtract
     * @return an inventory with the item subtracted from this one
     */
    public Inventory<T> subtract(final T item) {
        if (item == null) {
            return this;
        }
        final Map<T, Integer> freshMap = new HashMap<>();
        freshMap.putAll(items);
        if (freshMap.get(item) <= 0) {
            freshMap.put(item, 0);
        } else {
            freshMap.put(item, freshMap.get(item) - 1);
        }
        return new Inventory<T>(enums, Collections.unmodifiableMap(freshMap));
    }

    /**
     * Subtract the entirety of an inventory from this inventory.
     * @param inventory the inventory to subtract
     * @return an inventory with the inventory subtracted from this one
     */
    public Inventory<T> subtract(final Inventory<T> inventory) {
        if (inventory == null) {
            return this;
        }
        final Map<T, Integer> freshMap = new HashMap<>();
        final Map<T, Integer> toSubtract = inventory.getItems();
        for (T t : enums) {
            final Integer quantity = items.get(t);
            final Integer subtract = toSubtract.get(t);
            if (subtract >= quantity) {
                freshMap.put(t, 0);
            } else {
                freshMap.put(t, quantity - subtract);
            }
        }
        return new Inventory<T>(enums, Collections.unmodifiableMap(freshMap));
    }

    /**
     * Give the quantity of item in the inventory.
     * @param item the item to quantify
     * @return the quantity of the item in the inventory
     */
    public Integer quantity(final T item) {
        if (items.containsKey(item)) {
            return items.get(item);
        } else {
            return 0;
        }
    }
}
