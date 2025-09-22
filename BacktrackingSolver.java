public class BacktrackingSolver {

    public boolean solve(SudokuBoard board) {
        int[] loc = findEmpty(board);
        if (loc == null) return true; // solved
        int r = loc[0], c = loc[1];

        for (int num = 1; num <= 9; num++) {
            if (Validator.isValidMove(board, r, c, num)) {
                board.setValue(r, c, num);
                if (solve(board)) return true;
                board.setValue(r, c, 0); // backtrack
            }
        }
        return false;
    }

    // helper: find an empty cell (row, col) or null if none
    private int[] findEmpty(SudokuBoard board) {
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                if (board.getCell(r, c).getValue() == 0) return new int[]{r, c};
        return null;
    }

    // count solutions up to 'limit' (stop early when count >= limit)
    public int countSolutions(SudokuBoard board, int limit) {
        int[] count = new int[1];
        countSolutionsHelper(board, limit, count);
        return count[0];
    }

    private boolean countSolutionsHelper(SudokuBoard board, int limit, int[] count) {
        if (count[0] >= limit) return true; // early stop
        int[] loc = findEmpty(board);
        if (loc == null) { // full => one solution found
            count[0]++;
            return count[0] >= limit;
        }
        int r = loc[0], c = loc[1];
        for (int num = 1; num <= 9; num++) {
            if (Validator.isValidMove(board, r, c, num)) {
                board.setValue(r, c, num);
                boolean stop = countSolutionsHelper(board, limit, count);
                board.setValue(r, c, 0);
                if (stop) return true;
            }
        }
        return false;
    }
}
