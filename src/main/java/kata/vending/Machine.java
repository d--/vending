package kata.vending;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Machine {
    private final List<Coin> coinReturn;

    public List<Coin> getCoinReturn() {
        return coinReturn;
    }

    public Machine(List<Coin> coinReturn) {
        this.coinReturn = coinReturn;
    }

    public Machine insertCoin(Coin coin) {
        final List<Coin> coinReturn = new ArrayList<>();
        coinReturn.addAll(this.coinReturn);
        if (Currency.toCurrency(coin) == Currency.UNKNOWN) {
            coinReturn.add(coin);
        }
        return new Machine(Collections.unmodifiableList(coinReturn));
    }
}
