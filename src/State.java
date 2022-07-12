import java.util.ArrayList;

public class State {
    // FIELDS
    private final int dimension;
    private int[][] board;

    // CONSTRUCTORS
    public State () {
        this.dimension = 8;
        this.board = new int[this.dimension][this.dimension];
    }

    public State (int dimension) {
        this.dimension = dimension;
        this.board = new int[this.dimension][this.dimension];
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
    public ArrayList<State> getChildren () {
        ArrayList<State> children = new ArrayList<>();
        return children;
    }

    public boolean gameOver() {
        return false;
    }

    // SETTERS & GETTERS
    public int dimension () {
        return this.dimension;
    }

    public int board (int row, int col) {
        return this.board[row][col];
    }
}
