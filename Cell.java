public class Cell {
    private int value;      // 0 = empty
    private boolean fixed;  // true if given (original puzzle) and should not be changed by player

    public Cell(int value, boolean fixed) {
        this.value = value;
        this.fixed = fixed;
    }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    public boolean isFixed() { return fixed; }
    public void setFixed(boolean fixed) { this.fixed = fixed; }
}
