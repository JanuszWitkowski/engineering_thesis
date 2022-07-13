import java.util.ArrayList;
import java.lang.Math;

import static java.lang.Math.ceil;
import static java.lang.Math.pow;

public class State {
    // FIELDS
    private final int dimension;
    private int[][] board;
    private ArrayList<Integer> lastMoveList = null;
    private static final char[] playerFigure = new char[]{'X', 'x', ' ', 'o', 'O'};

    // CONSTRUCTORS
    public State () {
        this.dimension = 8;
        this.board = new int[this.dimension][this.dimension];
        initBoard();
    }

    public State (int dimension) {
        this.dimension = dimension;
        this.board = new int[this.dimension][this.dimension];
        initBoard();
    }

    public State (State state) {
        this.dimension = state.dimension();
        this.board = new int[this.dimension][this.dimension];
        copyBoard(state);
    }

    public State (State state, ArrayList<Integer> moveList) {
        this.dimension = state.dimension();
        this.board = new int[this.dimension][this.dimension];
        copyBoard(state);
        this.lastMoveList = moveList;
        makeMove(moveList);
    }

    // PRIVATE METHODS
    private void initBoard () {
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                board[row][col] = 0;
            }
        }
        for (int row = 0; row < 3; row += 2) {
            for (int col = 0; col < dimension; col += 2) {
                board[dimension - row - 1][col] = 1;
                board[row][col+1] = -1;
            }
        }
        for (int col = 0; col < dimension; col += 2) {
            board[1][col] = -1;
            board[6][col+1] = 1;
        }
    }

    private void copyBoard (State state) {
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                this.board[row][col] = state.board(row, col);
            }
        }
    }

    private void makeOneMove (int fromRow, int fromCol, int destRow, int destCol) {
        if ((fromRow - destRow) % 2 == 0) {     // capture pawn
            int captRow = (fromRow + destRow) / 2, captCol = (fromCol + destRow) / 2;
            this.board[captRow][captCol] = 0;
        }
        this.board[destRow][destCol] = this.board[fromRow][fromCol];
        this.board[fromRow][fromCol] = 0;
    }

    private void makeOneMove (int from, int dest) {
        int fromRow = numberToRow(from), fromCol = numberToCol(from);
        int destRow = numberToRow(dest), destCol = numberToCol(dest);
        makeOneMove(fromRow, fromCol, destRow, destCol);
    }

    private void makeOneMove (Pair from, Pair dest) {
        makeOneMove(from.l(), from.r(), dest.l(), dest.r());
    }

    // PUBLIC METHODS
    public static char getPlayerFigure (int player) {
        if (player < -2 || player > 2) return '?';
        return playerFigure[player + 2];
    }

    public int coordinatesToNumber (int row, int col) {
        int n = dimension * row + col + 1;
        return ((n + row + 1) % 2) * (n + 1) / 2;
    }

    public Pair numberToPair (int number) {
        if (number <= 0 || number > dimension*dimension/2) return new Pair (0, 0);
        int half = (dimension/2);
        int row = (number - 1) / half;
        int col = 2 * ((number - 1) % half) + (row + 1) % 2;
        return new Pair (row, col);
    }

    public int numberToRow (int number) {
        return 2 * (number - 1) / dimension;
    }

    public int numberToCol (int number) {
        return 2 * ((number - 1) % (dimension/2)) + ((2 * (number - 1) / dimension) + 1) % 2;
    }

    public void makeMove (ArrayList<Integer> moveList) {
        if (moveList.isEmpty()) return;
        int prevMove = moveList.get(0);
        for (int move = 1; move < moveList.size(); move++) {
            int nextMove = moveList.get(move);
            makeOneMove(prevMove, nextMove);
            prevMove = nextMove;
        }
    }

    public ArrayList<State> getChildren () {
        ArrayList<State> children = new ArrayList<>();
        return children;
    }

    public boolean gameOver() {
        return false;
    }

    public void printBoard () {
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                System.out.print("[" + getPlayerFigure(board[row][col]) + "]");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void showNumberedFields () {
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                int n = coordinatesToNumber(row, col);
                String zero = n >= 10 ? "" : "0";
                System.out.print("[" + zero + n + "]");
            }
            System.out.println();
        }
        System.out.println();
    }

    // SETTERS & GETTERS
    public int dimension () {
        return this.dimension;
    }

    public int board (int row, int col) {
        return this.board[row][col];
    }

    public ArrayList<Integer> lastMoveList() {
        return this.lastMoveList;
    }

}
