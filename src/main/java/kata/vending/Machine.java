package kata.vending;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a vending machine and its behavior.
 * Immutable.
 */
public final class Machine {
    /**
     * List of coins representing the coin return slot of the machine.
     */
    private final List<Coin> coinReturn;
    public List<Coin> getCoinReturn() {
        return coinReturn;
    }

    /**
     * Machine constructor.
     * @param coinReturn the coin return of the machine
     */
    public Machine(final List<Coin> coinReturn) {
        this.coinReturn = coinReturn;
    }

    /**
     * Insert a coin into the vending machine.
     * @param coin the coin to insert
     * @return a vending machine with the coin inserted
     */
    public Machine insertCoin(final Coin coin) {
        final List<Coin> tmpCoinReturn = new ArrayList<>();
        tmpCoinReturn.addAll(this.coinReturn);
        if (Currency.toCurrency(coin) == Currency.UNKNOWN) {
            tmpCoinReturn.add(coin);
        }
        return new Machine(Collections.unmodifiableList(tmpCoinReturn));
    }
}
