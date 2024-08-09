package fr.as2treffle.inventorybuilderapi.manager;

public enum Comparator {

    INFERIOR("I"),
    SUPERIOR("S"),
    EQUALS_OR_INFERIOR("EI"),
    EQUALS_OR_SUPERIOR("ES"),
    EQUALS("EQ"),
    NOT_EQUALS("NEQ"),
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
