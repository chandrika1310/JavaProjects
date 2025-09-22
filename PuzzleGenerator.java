import java.util.*;

public class PuzzleGenerator {
    private BacktrackingSolver solver = new BacktrackingSolver();
    private Random rand = new Random();

    public SudokuBoard generatePuzzle(String difficulty) {
        SudokuBoard board = new SudokuBoard();
        fillDiagonalBlocks(board);
        // solve full board
        solver.solve(board);

        // mark all cells as fixed (we have a complete solution now)
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                board.getCell(r, c).setFixed(true);

        // remove cells while preserving unique solution
        removeCellsWithUniqueness(board, difficulty);
        return board;
    }

    private void fillDiagonalBlocks(SudokuBoard board) {
        for (int i = 0; i < 9; i += 3)
            fillBlock(board, i, i);
    }

    private void fillBlock(SudokuBoard board, int row, int col) {
        List<Integer> nums = new ArrayList<>();
        for (int n = 1; n <= 9; n++) nums.add(n);
        Collections.shuffle(nums, rand);
        int idx = 0;
        for (int r = row; r < row + 3; r++)
            for (int c = col; c < col + 3; c++) {
                board.setValue(r, c, nums.get(idx++));
            }
    }

    private void removeCellsWithUniqueness(SudokuBoard board, String difficulty) {
        int clues;
        switch (difficulty.toLowerCase()) {
            case "easy": clues = 36; break;
            case "medium": clues = 32; break;
            case "hard": clues = 28; break;
            default: clues = 32;
        }
        int cellsToRemove = 81 - clues;
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < 81; i++) positions.add(i);
        Collections.shuffle(positions, rand);

        BacktrackingSolver countingSolver = new BacktrackingSolver();

        for (int pos : positions) {
            if (cellsToRemove <= 0) break;
            int r = pos / 9, c = pos % 9;
            int backup = board.getCell(r, c).getValue();
            if (backup == 0) continue;
            // remove it
            board.setValue(r, c, 0);
            board.getCell(r, c).setFixed(false);

            // check uniqueness
            SudokuBoard temp = board.clone();
            int sols = countingSolver.countSolutions(temp, 2);
            if (sols != 1) {
                // revert removal
                board.setValue(r, c, backup);
                board.getCell(r, c).setFixed(true);
            } else {
                cellsToRemove--;
            }
        }
    }
}
