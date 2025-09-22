public class Validator {
    public static boolean isValidMove(SudokuBoard board, int row, int col, int value) {
        if (value < 1 || value > 9) return false;
        // row
        for (int c = 0; c < 9; c++)
            if (board.getCell(row, c).getValue() == value) return false;
        // column
        for (int r = 0; r < 9; r++)
            if (board.getCell(r, col).getValue() == value) return false;
        // box
        int startRow = (row / 3) * 3, startCol = (col / 3) * 3;
        for (int r = startRow; r < startRow + 3; r++)
            for (int c = startCol; c < startCol + 3; c++)
                if (board.getCell(r, c).getValue() == value) return false;
        return true;
    }
}
