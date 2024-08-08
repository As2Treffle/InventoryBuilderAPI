package fr.as2treffle.inventorybuilderapi.manager;

public enum Comparator {

    INFERIOR("<"),
    SUPERIOR(">"),
    EQUALS_OR_INFERIOR("<="),
    EQUALS_OR_SUPERIOR(">="),
    EQUALS_("=="),
    NOT_EQUALS("!="),
    STARTS_WITH("SW"),
    ENDS_WITH("EW"),
    CONTAINS("C"),
    NOT_FOUND("");

    private final String symbol;

    Comparator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Comparator getBySymbol(String symbol) {
        for (Comparator comparator : Comparator.values()) {
            if (comparator.getSymbol().equals(symbol)) {
                return comparator;
            }
        }
        return Comparator.NOT_FOUND;
    }

}
