package numbers;

public enum Property {

    BUZZ("buzz", 3, 0),
    DUCK("duck", 1, 1),
    PALINDROMIC("palindromic", 5, 2),
    GAPFUL("gapful", 4, 3),
    SPY("spy", 1, 4),
    SQUARE("square", 2, 5),
    SUNNY("sunny",2, 6),
    JUMPING("jumping", 6, 7),
    HAPPY("happy", 7, 8),
    SAD("sad", 7, 9),
    EVEN("even", 0, 10),
    ODD("odd", 0, 11);

    private String value;
    private int comparator;
    private int reverse;

    Property(String value, int comparator, int reverse) {
        this.value = value;
        this.comparator = comparator;
        this.reverse = reverse;
    }

    public String getValue() {
        return this.value;
    }

    public int getComparator() {
        return comparator;
    }

    public boolean mutuallyExclusive(Property that) {
        return this.getComparator() == that.getComparator();
    }

}
