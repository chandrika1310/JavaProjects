import java.util.*;

public class GameManager {
    private SudokuBoard board;
    private BacktrackingSolver solver = new BacktrackingSolver();
    private PuzzleGenerator generator = new PuzzleGenerator();
    private Deque<Move> undoStack = new ArrayDeque<>();

    public void startGame(String difficulty) {
        System.out.println("Generating puzzle (" + difficulty + ") ... (may take a few seconds)");
        board = generator.generatePuzzle(difficulty);
        System.out.println("Puzzle ready. Commands: put r c v   |   hint   |   undo   |   solve   |   exit");
        play();
    }

    private void play() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            board.printBoard();
            if (board.isFull()) {
                System.out.println("Board is complete. Verifying...");
                if (solver.solve(board.clone())) System.out.println("Congratulations — puzzle solved!");
                else System.out.println("Board filled but invalid.");
                break;
            }
            System.out.print("Enter command: ");
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            String cmd = parts[0].toLowerCase();

            try {
                if (cmd.equals("hint")) {
                    giveHint();
                } else if (cmd.equals("undo")) {
                    undo();
                } else if (cmd.equals("solve")) {
                    System.out.println("Auto-solving...");
                    solver.solve(board);
                } else if (cmd.equals("exit")) {
                    System.out.println("Exiting. Goodbye!");
                    break;
                } else {
                    // parse "put r c v" or "r c v"
                    int idx = 0;
                    if (cmd.equals("put")) idx = 1;
                    int r = Integer.parseInt(parts[idx]);
                    int c = Integer.parseInt(parts[idx + 1]);
                    int v = Integer.parseInt(parts[idx + 2]);

                    if (r < 0 || r > 8 || c < 0 || c > 8 || v < 1 || v > 9) {
                        System.out.println("Invalid indices/values. Rows/cols: 0-8, values 1-9.");
                        continue;
                    }
                    if (board.getCell(r, c).isFixed()) {
                        System.out.println("Cell is fixed (given in puzzle) — cannot change it.");
                        continue;
                    }
                    if (!Validator.isValidMove(board, r, c, v)) {
                        System.out.println("Invalid move (violates Sudoku rules).");
                        continue;
                    }
                    int old = board.getCell(r, c).getValue();
                    board.setValue(r, c, v);
                    undoStack.push(new Move(r, c, old, v));
                }
            } catch (Exception ex) {
                System.out.println("Bad command or parse error. Use: put r c v   OR   r c v   OR   hint/undo/solve/exit");
            }
        }
        sc.close();
    }

    private void giveHint() {
        SudokuBoard temp = board.clone();
        if (solver.solve(temp)) {
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (board.getCell(r, c).getValue() == 0) {
                        System.out.println("Hint -> row " + r + " col " + c + " = " + temp.getCell(r, c).getValue());
                        return;
                    }
                }
            }
            System.out.println("No hint available (no empty cells).");
        } else {
            System.out.println("Cannot compute hint (current board may be invalid).");
        }
    }

    private void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        Move m = undoStack.pop();
        board.setValue(m.row, m.col, m.oldValue);
        System.out.println("Undid move at (" + m.row + "," + m.col + ").");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose difficulty: easy, medium, hard (default = medium)");
        String diff = sc.nextLine().trim();
        if (diff.isEmpty()) diff = "medium";
        GameManager gm = new GameManager();
        gm.startGame(diff);
        sc.close();
    }
}
