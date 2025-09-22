public class SudokuBoard implements Cloneable {
    private Cell[][] grid = new Cell[9][9];

    public SudokuBoard() {
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                grid[r][c] = new Cell(0, false);
    }

    public Cell getCell(int row, int col) { return grid[row][col]; }

    // set value directly (GameManager will check fixed before letting a user change)
    public void setValue(int row, int col, int value) {
        grid[row][col].setValue(value);
    }

    public boolean isFull() {
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                if (grid[r][c].getValue() == 0) return false;
        return true;
    }

    @Override
    public SudokuBoard clone() {
        SudokuBoard copy = new SudokuBoard();
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++) {
                copy.grid[r][c].setValue(this.grid[r][c].getValue());
                copy.grid[r][c].setFixed(this.grid[r][c].isFixed());
            }
        return copy;
    }

    public void printBoard() {
        System.out.println();
        System.out.println("   0 1 2   3 4 5   6 7 8");
        System.out.println(" -------------------------");
        for (int r = 0; r < 9; r++) {
            System.out.print(r + "| ");
            for (int c = 0; c < 9; c++) {
                int v = grid[r][c].getValue();
                System.out.print((v == 0 ? "." : v) + " ");
                if ((c + 1) % 3 == 0) System.out.print("| ");
            }
            System.out.println();
            if ((r + 1) % 3 == 0) System.out.println(" -------------------------");
        }
        System.out.println();
    }
}
