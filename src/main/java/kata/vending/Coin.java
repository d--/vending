package kata.vending;

public final class Coin {
    public enum Size {
        SMALL,
        MEDIUM,
        LARGE
    }

    public enum Weight {
        LIGHT,
        HEAVY
    }

    private final Coin.Size size;
    private final Coin.Weight weight;

    public Size getSize() {
        return size;
    }

    public Weight getWeight() {
        return weight;
    }

    public Coin(Coin.Size size, Coin.Weight weight) {
        this.size = size;
        this.weight = weight;
    }
}
