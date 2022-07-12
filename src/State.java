import java.util.ArrayList;
import java.lang.Math;

import static java.lang.Math.ceil;

public class State {
    // FIELDS
    private final int dimension;
    private int[][] board;
    private static final char[] playerColor = new char[]{'X', 'x', ' ', 'o', 'O'};

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

    public State (State state, int row1, int col1, int row2, int col2) {
        this.dimension = state.dimension();
        this.board = new int[this.dimension][this.dimension];
        copyBoard(state);
        makeMove(row1, col1, row2, col2);
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

    private void makeMove (int row1, int col1, int row2, int col2) {
        int tmp = this.board[row1][col1];
        this.board[row1][col1] = this.board[row2][col2];
        this.board[row2][col2] = tmp;
    }

    // PUBLIC METHODS
    public static char getPlayerColor (int player) {
        if (player < -2 || player > 2) return 'X';
        return playerColor[player + 2];
    }

    public int coordinatesToNumber (int row, int col) {
        return (dimension/2) * row + col / 2 + 1;
    }

    public Pair numberToPair (int number) {
        int half = (dimension/2);
        int row = (number - 1) / half;
        int col = (number - 1) % half + (row + 1) % 2;
        return new Pair (row, col);
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
                System.out.print("[" + getPlayerColor(board[row][col]) + "]");
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

}
