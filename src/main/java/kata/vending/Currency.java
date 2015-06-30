package kata.vending;

public enum Currency {
    NICKEL,
    DIME,
    QUARTER,
    UNKNOWN;

    public static Currency toCurrency(Coin coin) {
        if (coin.getWeight() == Coin.Weight.LIGHT &&
                coin.getSize() == Coin.Size.SMALL) {
            return DIME;
        }
        if (coin.getWeight() == Coin.Weight.HEAVY &&
                coin.getSize() == Coin.Size.MEDIUM) {
            return NICKEL;
        }
        if (coin.getWeight() == Coin.Weight.HEAVY &&
                coin.getSize() == Coin.Size.LARGE) {
            return QUARTER;
        }
        return UNKNOWN;
    }
}
