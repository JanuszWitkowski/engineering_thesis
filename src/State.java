import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Queue;

public class State {
    // FIELDS
    private final int dimension;
    private int[][] board;
    private int currentPlayer = 1;
    private ArrayList<Integer> lastMoveList = null;
    private final ArrayList<ArrayList<Integer>> currentPlayerMoves;
    private static final int[] directions = new int[]{1, -1};
    private static final char[] playerFigure = new char[]{'X', 'x', ' ', 'o', 'O'};

    // CONSTRUCTORS
    public State () {
        this(8);
    }

    public State (int dimension) {
        this.dimension = dimension;
        this.board = new int[this.dimension][this.dimension];
        initBoard();
        this.currentPlayerMoves = getPossibleMoves(currentPlayer);
    }

    public State (State state) {
        this.dimension = state.dimension();
        this.board = new int[this.dimension][this.dimension];
        this.currentPlayer = state.currentPlayer();
        copyBoard(state);
        this.lastMoveList = state.lastMoveList();
        this.currentPlayerMoves = state.currentPlayerMoves();
    }

    public State (State state, ArrayList<Integer> moveList) {
        this.dimension = state.dimension();
        this.board = new int[this.dimension][this.dimension];
        this.currentPlayer = state.currentPlayer();
        copyBoard(state);
        this.lastMoveList = moveList;
        makeMove(moveList);
        this.currentPlayerMoves = getPossibleMoves(currentPlayer);
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

    private boolean isKing (int row, int col) {
        return board[row][col] % 2 == 0;
    }

    private int ownerOfField (int row, int col) {
        int field = board[row][col];
        return Integer.compare(field, 0);
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

    private boolean isOutOfBounds (int row, int col) {
        return row < 0 || row >= dimension || col < 0 || col >= dimension;
    }

    private ArrayList<Integer> getMoveFromTree (Node tree) {
        ArrayList<Integer> move = new ArrayList<>();
        if (tree.height() > 0) {
            for (int i = tree.height(); i >= 0; i--) {
                move.add(tree.moveValue());
                tree = tree.parent();
            }
            Collections.reverse(move);
        }
        return move;
    }

    private void buildCaptureMove (Node parent, int height, ArrayList<ArrayList<Integer>> moves, int row, int col, int dr, boolean isKing) {
//        move.add(coordinatesToNumber(row, col));
        Node next = new Node(coordinatesToNumber(row, col), height + 1, parent);
        int adjRow, adjCol, newRow, newCol;
        boolean nodeIsLeaf = true, checkOtherRow = !isKing;
        do {
            checkOtherRow = !checkOtherRow;
            for (int dc = -1; dc <= 1; dc += 2) {
                adjRow = row + dr;
                adjCol = col + dc;
                if (!isOutOfBounds(adjRow, adjCol)) {
                    int owner = ownerOfField(adjRow, adjCol);
                    if (owner != 0 && owner != ownerOfField(row, col)) {
                        newRow = adjRow + dr;
                        newCol = adjCol + dc;
                        if (!isOutOfBounds(newRow, newCol) && board[newRow][newCol] == 0) {
                            nodeIsLeaf = false;
                            buildCaptureMove(next, next.height(), moves, newRow, newCol, dr, isKing);
                        }
                    }
                }
            }
            dr *= -1;
        } while (checkOtherRow);
        if (nodeIsLeaf) {
            moves.add(getMoveFromTree(next));
        }
    }

    private ArrayList<ArrayList<Integer>> getPossibleMovesForOnePawn (int row, int col, int n) {
        ArrayList<ArrayList<Integer>> possibleMoves = new ArrayList<>();
        if (board[row][col] != 0) {
            // TODO: TUTAJ UWAŻAĆ! NIE DODAWAĆ NIC JEŚLI NIE MA ŻADNYCH RUCHÓW!!
            ArrayList<ArrayList<Integer>> captureMoves = new ArrayList<>();
            int dr = direction(ownerOfField(row, col));
            buildCaptureMove(null, -1, captureMoves, row, col, dr, isKing(row, col));
//                    if (!isOutOfBounds(newRow, newCol)) {
//                        if (board[newRow][newCol] == 0) {
//                            move = new ArrayList<>();
//                            move.add(coordinatesToNumber(row, col));
//                            move.add(coordinatesToNumber(newRow, newCol));
//                            possibleMoves.add(move);
//                        }
//                        else if (ownerOfField(newRow, newCol) != ownerOfField(row, col)) {
//                            newRow += dr; newCol += dc;
//                            if (!isOutOfBounds(newRow, newCol) && board[newRow][newCol] == 0) {
//                                move = new ArrayList<>();
//                                move.add(coordinatesToNumber(row, col));
//                                buildCaptureMove(possibleMoves, move, newRow, newCol, dr, false);
//                            }
//                        }
//                    }
            if (captureMoves.isEmpty()) {
                boolean checkOtherRow = !isKing(row, col);
                do {
                    checkOtherRow = !checkOtherRow;
                    for (int dc = -1; dc <= 1; dc += 2) {
                        int newRow = row + dr, newCol = col + dc;
                        if (!isOutOfBounds(newRow, newCol) && board[newRow][newCol] == 0) {
                            ArrayList<Integer> move = new ArrayList<>();
                            move.add(coordinatesToNumber(row, col));
                            move.add(coordinatesToNumber(newRow, newCol));
                            possibleMoves.add(move);
                        }
                    }
                    dr *= -1;
                } while (checkOtherRow);
            }
            else {
                if (isKing(row, col)) {     // maksymalne bicie damką
                    int maxLength = 0;
                    for (ArrayList<Integer> move : captureMoves) {
                        if (maxLength < move.size()) maxLength = move.size();
                    }
                    for (ArrayList<Integer> move : captureMoves) {
                        if (maxLength == move.size()) possibleMoves.add(move);
                    }
                }
                else {
                    possibleMoves.addAll(captureMoves);
                }
            }
        }
        return possibleMoves;
    }

    private ArrayList<ArrayList<Integer>> getPossibleMovesForOnePawn (int row, int col) {
        return getPossibleMovesForOnePawn(row, col, coordinatesToNumber(row, col));
    }

    private ArrayList<ArrayList<Integer>> getPossibleMovesForOnePawn (int n) {
        return getPossibleMovesForOnePawn(numberToRow(n), numberToCol(n), n);
    }

    private static int direction (int field) {
        if (field == 0) return 0;
        else if (field > 0) return -1;
        return 1;
//        return directions[(player + 1) / 2];
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
        currentPlayer = opponent();
        if (moveList.isEmpty()) return;
        int prevMove = moveList.get(0);
        for (int move = 1; move < moveList.size(); move++) {
            int nextMove = moveList.get(move);
            makeOneMove(prevMove, nextMove);
            prevMove = nextMove;
        }
        // damka
        int row = numberToRow(prevMove), col = numberToCol(prevMove);
        if ((row == 0 || row == dimension - 1) && board[row][col] % 2 == 1) {
            board[row][col] = board[row][col] * 2;
        }
    }

    public ArrayList<ArrayList<Integer>> getPossibleMoves (int player) {
        ArrayList<ArrayList<Integer>> possibleMoves = new ArrayList<>();
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                if (board[row][col] == player || board[row][col] == 2 * player) {
                    // TODO: zastanowić się czy już na tym poziomie rozpatrzeć różne ruchy dla pionków i damek.
                    possibleMoves.addAll(getPossibleMovesForOnePawn(row, col));
                }
            }
        }
        return possibleMoves;
    }

    public ArrayList<State> getChildren () {
        ArrayList<State> children = new ArrayList<>();
        for (ArrayList<Integer> moves : currentPlayerMoves) {
            children.add(new State(this, moves));
        }
        return children;
    }

    public ArrayList<State> getChildren (int player) {
        ArrayList<State> children = new ArrayList<>();
        ArrayList<ArrayList<Integer>> possibleMoves = getPossibleMoves(currentPlayer);
        for (ArrayList<Integer> moves : possibleMoves) {
            children.add(new State(this, moves));
        }
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

    public boolean playerHasNoPieces (int player) {
        return false;   // TODO
    }

    public boolean currentPlayerHasNoPieces () {
        return playerHasNoPieces(currentPlayer);
    }

    public boolean opponentHasNoPieces () {
        return playerHasNoPieces(opponent());
    }

    public boolean currentPlayerOutOfMoves () {
        return currentPlayerMoves.isEmpty();
    }

    // SETTERS & GETTERS
    public int dimension () {
        return this.dimension;
    }

    public int board (int row, int col) {
        return this.board[row][col];
    }

    public int currentPlayer () {
        return this.currentPlayer;
    }

    public int opponent (int player) {
        return (-1) * this.currentPlayer;
    }

    public int opponent () {
        return opponent(currentPlayer);
    }

    public ArrayList<Integer> lastMoveList() {
        return this.lastMoveList;
    }

    public ArrayList<ArrayList<Integer>> currentPlayerMoves () {
        return this.currentPlayerMoves;
    }

}
