import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Klasa odpowiedzialna za obsługę stanów gry, ruchów na planszy, wyznaczania legalnych ruchów i wyznaczania
 * wartości parametrów dla funkcji oceny heurystycznej w minimaxie.
 * Jedna z najważniejszych klas w projekcie. Jej obiektami są stany rozgrywki.
 */
public class State {
    // PRIVATE CLASSES
    private record Pair(int l, int r) { }
    private record Node(int moveValue, int height, Node parent) { }

    // FIELDS
    private final int dimension;
    private int[][] board;
    private int currentPlayer = 1;
    private int noCaptureCounter = 0;    // Licznik ruchów bez bicia.
    private static final int maxNoCaptureMovesForDraw = 40; // Maksymalna liczba ruchów bez bicia po której gra kończy się remisem.
    private ArrayList<Integer> creationMove = null;     // Ruch który przyczynił się do stworzenia tego stanu gry.
    private ArrayList<Integer> lastMove = null;     // Zachowuje poprzedni podruch w całym ruchu, aby sprawdzić np. bicia.
    private ArrayList<ArrayList<Integer>> currentPlayerMoves;
    private static final char[] playerSymbol = new char[]{'X', 'x', ' ', 'o', 'O'};
    private static final char[] numbersToBigLetters = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L'};
    private static final char[] numbersToSmallLetters = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l'};
    private static final HashMap<Character, Integer> lettersToNumbers = initLettersToNumbers(); // Nakładka na współrzędne.
    private static final String[] playerColors = new String[] {     // Kolory pionków w konsoli.
            Console.RED_BOLD, Console.RED, "", Console.BLUE, Console.BLUE_BOLD
    };
    private static final String[] backgroundColors = new String[] { // Kolory pól w konsoli.
            "", Console.BLACK_BACKGROUND, Console.WHITE_BACKGROUND
    };


    // PRIVATE STATIC INITIALIZERS
    private static HashMap<Character, Integer> initLettersToNumbers () {
        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < numbersToBigLetters.length; i++) {
            map.put(numbersToBigLetters[i], i);
            map.put(numbersToSmallLetters[i], i);
        }
        return map;
    }


    // CONSTRUCTORS
    public State () {
        this(8);
    }

    public State (int dimension) {
        this.dimension = dimension;
        this.board = new int[this.dimension][this.dimension];
        initBoard();
//        initBoardTemplate();
//        this.currentPlayerMoves = getPossibleMoves(currentPlayer);
    }

    public State (State state, ArrayList<Integer> moveList) {
        this.dimension = state.dimension();
        this.board = new int[this.dimension][this.dimension];
        this.currentPlayer = state.currentPlayer();
        copyBoard(state);
        this.creationMove = moveList;
        makeMove(moveList);
    }


    // PRIVATE METHODS

    /**
     * Funkcja do debugowania, pozwala sprawdzać przypadki szczególne.
     */
    private void initBoardTemplate () {     // tmp
//        board = new int[][] {
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0}
//        };
        board = new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, -2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, -1, 0, 0, 0, -2, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, -1, 0, -2, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
//        board = new int[][] {
//                {0, -1, 0, -1, 0, -1, 0, -1},
//                {1, 0, 1, 0, 1, 0, 1, 0},
//                {0, -1, 0, -1, 0, -1, 0, -1},
//                {1, 0, 1, 0, 1, 0, 1, 0},
//                {0, -1, 0, -1, 0, -1, 0, -1},
//                {1, 0, 1, 0, 1, 0, 1, 0},
//                {0, -1, 0, -1, 0, -1, 0, -1},
//                {1, 0, 1, 0, 1, 0, 1, 0}
//        };
//        board = new int[][] {
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {-1, 0, 0, 0, 0, 0, 0, 0},
//                {0, -1, 0, 0, 0, 0, 0, 0},
//                {1, 0, 1, 0, 0, 0, 0, 0}
//        };
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

    /**
     * Determinuje właściciela pola na podstawie wartości na tym polu.
     * @param field Wartość w polu
     * @return 1 jeśli pole zajmuje pionek 1 lub 2, -1 jeśli pole zajmuje pionek -1 lub -2, 0 jeśli pole wynosi 0
     */
    private int ownerOfField (int field) {
        return Integer.compare(field, 0);
    }

    private int ownerOfField (int row, int col) {
        return ownerOfField(board[row][col]);
    }

    /**
     * Wykonuje pojedynczy ruch między jednym polem a drugim. Obsługuje bicia.
     * @param fromRow Rząd pierwszego pola
     * @param fromCol Kolumna pierwszego pola
     * @param destRow Rząd drugiego pola
     * @param destCol Kolumna drugiego pola
     */
    private void makeOneMove (int fromRow, int fromCol, int destRow, int destCol) {
        if ((fromRow - destRow) % 2 == 0) {     // capture pawn
            int captRow = (fromRow + destRow) / 2, captCol = (fromCol + destCol) / 2;
            this.board[captRow][captCol] = 0;
            noCaptureCounter = 0;
        }
        this.board[destRow][destCol] = this.board[fromRow][fromCol];
        this.board[fromRow][fromCol] = 0;
    }

    private void makeOneMove (int from, int dest) {
        int fromRow = numberToRow(from), fromCol = numberToCol(from);
        int destRow = numberToRow(dest), destCol = numberToCol(dest);
        makeOneMove(fromRow, fromCol, destRow, destCol);
    }

    private boolean isInsideTheBoard(int row, int col) {
        return row >= 0 && row < dimension && col >= 0 && col < dimension;
    }

    /**
     * Znajduje możliwe ruchy bez bić.
     * @param row Rząd pionka
     * @param col kolumna pionka
     * @return Lista możliwych ruchów dla danego pionka (bez bić)
     */
    private ArrayList<ArrayList<Integer>> getAdjacentMovesForOnePiece(int row, int col) {
        ArrayList<ArrayList<Integer>> possibleMoves = new ArrayList<>();
        if (board[row][col] != 0) {
            int dr = direction(ownerOfField(row, col));
            boolean checkOtherRow = !isKing(row, col);
            do {
                checkOtherRow = !checkOtherRow;
                for (int dc = -1; dc <= 1; dc += 2) {
                    int newRow = row + dr, newCol = col + dc;
                    if (isInsideTheBoard(newRow, newCol) && board[newRow][newCol] == 0) {
                        ArrayList<Integer> move = new ArrayList<>();
                        move.add(coordinatesToNumber(row, col));
                        move.add(coordinatesToNumber(newRow, newCol));
                        possibleMoves.add(move);
                    }
                }
                dr *= -1;
            } while (checkOtherRow);
        }
        return possibleMoves;
    }

    /**
     * Rozbicie drzewa bić na ruch.
     * @param tree Liść drzewa z którego chcemy wyznaczyć ruch
     * @return Ruch w postaci listy pól
     */
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

    /**
     * Rekurencyjna metoda wyznaczająca wszystkie możliwe bicia z jednego pola.
     * Umożliwia to budowana tutaj struktura drzewa bić.
     * @param parent Poprzedni wierzchołek drzewa bić
     * @param height Dotychczasowa wysokość drzewa bić
     * @param moves Lista ruchów do której wpisywany jest powstały z drzewa ruch
     * @param row Rząd rozpatrywanego pola
     * @param col Kolumna rozpatrywanego pola
     * @param dr Kierunek góra/dół w którym rozpatrujemy bicie dla zwykłego pionka ("dr" means "row difference")
     * @param isKing Informacja czy należy rozpatrzeć też bicie w kierunku przeciwnym niż pokazuje dr
     */
    private void buildCaptureMove (Node parent, int height, ArrayList<ArrayList<Integer>> moves, int row, int col, int dr, boolean isKing) {
        Node next = new Node(coordinatesToNumber(row, col), height + 1, parent);
        int adjRow, adjCol, newRow, newCol;
        boolean nodeIsLeaf = true, checkOtherRow = !isKing;
        do {
            checkOtherRow = !checkOtherRow;
            for (int dc = -1; dc <= 1; dc += 2) {
                adjRow = row + dr;
                adjCol = col + dc;
                if (isInsideTheBoard(adjRow, adjCol)) {
                    int owner = ownerOfField(adjRow, adjCol);
                    if (owner != 0 && owner != ownerOfField(row, col)) {
                        newRow = adjRow + dr;
                        newCol = adjCol + dc;
                        if (isInsideTheBoard(newRow, newCol) && board[newRow][newCol] == 0) {
                            nodeIsLeaf = false;
                            board[newRow][newCol] = board[row][col];
                            board[row][col] = 0;
                            int capturedPiece = board[adjRow][adjCol];
                            board[adjRow][adjCol] = 0;
                            buildCaptureMove(next, next.height(), moves, newRow, newCol, dr, isKing);
                            board[adjRow][adjCol] = capturedPiece;
                            board[row][col] = board[newRow][newCol];
                            board[newRow][newCol] = 0;
                        }
                    }
                }
            }
            dr *= -1;
        } while (checkOtherRow);
        if (nodeIsLeaf) {
            ArrayList<Integer> move = getMoveFromTree(next);
            if (!move.isEmpty()) {
                moves.add(move);
            }
        }
    }

    /**
     * Znajduje możliwe bicia dla danego pionka.
     * @param row Rząd pionka
     * @param col kolumna pionka
     * @return Lista możliwych bić
     */
    private ArrayList<ArrayList<Integer>> getCaptureMovesForOnePiece(int row, int col) {
        ArrayList<ArrayList<Integer>> possibleMoves = new ArrayList<>();
        if (board[row][col] != 0) {
            ArrayList<ArrayList<Integer>> captureMoves = new ArrayList<>();
            int dr = direction(ownerOfField(row, col));
            buildCaptureMove(null, -1, captureMoves, row, col, dr, isKing(row, col));
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
        return possibleMoves;
    }

    private static int direction (int field) {
        if (field == 0) return 0;
        else if (field > 0) return -1;
        return 1;
    }


    // PUBLIC METHODS

    /**
     * Reset planszy i wszystkich parametrów.
     */
    public void initBoard () {
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
        currentPlayer = 1;
        currentPlayerMoves = getPossibleMoves(currentPlayer);
        noCaptureCounter = 0;
    }

    public static char symbol(int field) {
        if (field < -2 || field > 2) return '?';
        return playerSymbol[field + 2];
    }

    public static String color (int field) {
        if (field < -2 || field > 2) return "";
        return playerColors[field + 2];
    }

    /**
     * Wprowadza system numerowania pól na planszy jako alternatywny sposób na wyłuskanie pola.
     * @param row Rząd pola
     * @param col Kolumna pola
     * @return Numer pola w nowym systemie
     */
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

    public boolean isUserInputInvalid(char col, int row) {
        return !isInsideTheBoard(row, letterToNumber(col));
    }

    public boolean isUserInputInvalid(int number) {
        if (number < 0 || number > dimension * dimension / 2) return true;
        return !isInsideTheBoard(numberToRow(number), numberToCol(number));
    }

    /**
     * Służy do przekazywania inputu od użytkownika i jego walidację. W razie powodzenia wykonuje ruch.
     * @param numbers Lista ruchów od użytkownika
     * @return TRUE jeśli walidacja przebiegła pomyślnie i wykonany został ruch; FALSE wp.p.
     */
    public boolean submitUserInput (ArrayList<Integer> numbers) {
        if (numbers.size() <= 1) return false;
        for (Integer n : numbers)
            if (isUserInputInvalid(n)) return false;
        makeMove(numbers);
        return true;
    }

    /**
     * Wykonuje podany ruch na planszy. Nie waliduje ruchu przed jego wykonaniem!
     * @param moveList Ruch do wykonania
     */
    public void makeMove (ArrayList<Integer> moveList) {
        lastMove = moveList;
        ++noCaptureCounter;
        if (moveList.isEmpty()) return;
        int prevMove = moveList.get(0);
        for (int move = 1; move < moveList.size(); move++) {
            int nextMove = moveList.get(move);
            makeOneMove(prevMove, nextMove);
            prevMove = nextMove;
        }
        // damka
        int row = numberToRow(prevMove), col = numberToCol(prevMove);
        int boarder = ownerOfField(row, col) == 1 ? 0 : dimension - 1;
        if (row == boarder && !isKing(row, col)) {
            board[row][col] = board[row][col] * 2;
        }
        currentPlayer = opponent();
        currentPlayerMoves = getPossibleMoves(currentPlayer);
    }

    /**
     * Wyszukuje wszystkie możliwe do wykonania ruchy dla danego gracza.
     * @param player Gracz którego ruchy chcemy otrzymać
     * @return Lista ruchów danego gracza
     */
    public ArrayList<ArrayList<Integer>> getPossibleMoves (int player) {
        ArrayList<ArrayList<Integer>> possibleMoves = new ArrayList<>();
        boolean captureMoveDoesNotExists = true;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                if (ownerOfField(row, col) == player) {
                    ArrayList<ArrayList<Integer>> captureMoves = getCaptureMovesForOnePiece(row, col);
                    if (!captureMoves.isEmpty()) {
                        captureMoveDoesNotExists = false;
                        possibleMoves.addAll(captureMoves);
                    }
                }
            }
        }
        if (captureMoveDoesNotExists) {
            for (int row = 0; row < dimension; row++) {
                for (int col = 0; col < dimension; col++) {
                    if (ownerOfField(row, col) == player) {
                        possibleMoves.addAll(getAdjacentMovesForOnePiece(row, col));
                    }
                }
            }
        }
        return possibleMoves;
    }

    /**
     * Tworzy nowe stany gry w oparciu o listę możliwych ruchów obecnego gracza.
     * @return Lista stanów gry możliwych do uzyskania za pomocą jednego ruchu za obecnego stanu gry
     */
    public ArrayList<State> getChildren () {
        ArrayList<State> children = new ArrayList<>();
        for (ArrayList<Integer> move : currentPlayerMoves) {
            children.add(new State(this, move));
        }
        return children;
    }

    public boolean gameOver() {
        if (noCaptureCounter >= maxNoCaptureMovesForDraw) return true;
        return winner() != 0;
    }

    public static int letterToNumber (char letter) {
        return lettersToNumbers.getOrDefault(letter, -1);
    }

    public void printBoard () {
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                System.out.print("[" + symbol(board[row][col]) + "]");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printBoardWithCoordinates () {
        System.out.print("   ");
        for (int col = 0; col < dimension; col++)
            System.out.print(" " + numbersToBigLetters[col] + " ");
        System.out.println();
        String background = backgroundColors[2], disabled = backgroundColors[1];
        int disabledCounter = 0;
        for (int row = 0; row < dimension; row++) {
            System.out.print(" " + (row+1) + " ");
            for (int col = 0; col < dimension; col++) {
                ++disabledCounter;
                if (disabledCounter % 2 == 1)
                    System.out.print(disabled + "[ ]" + Console.RESET);
                else if (lastMove != null && numberToRow(lastMove.get(0)) == row && numberToCol(lastMove.get(0)) == col)
                    System.out.print(backgroundColors[2] + "[ ]" + Console.RESET);
                else if (lastMove != null && numberToRow(lastMove.get(lastMove.size()-1)) == row
                        && numberToCol(lastMove.get(lastMove.size()-1)) == col)
                    System.out.print(background + "[" + color(board[row][col]) + symbol(board[row][col]) +
                            Console.RESET + background + "]" + Console.RESET);
                else System.out.print("[" + color(board[row][col]) + symbol(board[row][col]) + Console.RESET + "]");
            }
            System.out.println();
            ++disabledCounter;
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
        return getNumberOfPieces(player) == 0;
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

    public int winner () {
        if (opponentHasNoPieces()) return currentPlayer;
        if (currentPlayerHasNoPieces()) return opponent();
        if (currentPlayerOutOfMoves()) return opponent();
        return 0;
    }


    // HEURISTIC METHODS
    public int getNumberOfPieces (int player) {
        int sum = 0;
        for (int[] row : board) {
            for (int field : row) {
                if (ownerOfField(field) == player) ++sum;
            }
        }
        return sum;
    }

    public int getNumberOfPawns (int player) {
        int sum = 0;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                if (ownerOfField(row, col) == player && !isKing(row, col)) ++sum;
            }
        }
        return sum;
    }

    public int getNumberOfKings (int player) {
        int sum = 0;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                if (ownerOfField(row, col) == player && isKing(row, col)) ++sum;
            }
        }
        return sum;
    }

    public int getNumberOfSafePawns (int player) {
        int sum = 0;
        for (int row = 0; row < dimension-1; row++) {
            if (ownerOfField(row, 0) == player && !isKing(row, 0)) ++sum;
            if (ownerOfField(row + 1, dimension - 1) == player && !isKing(row + 1, dimension - 1)) ++sum;
        }
        for (int col = 0; col < dimension-1; col++) {
            if (ownerOfField(dimension - 1, col) == player && !isKing(dimension - 1, col)) ++sum;
            if (ownerOfField(0, col + 1) == player && !isKing(0, col + 1)) ++sum;
        }
        return sum;
    }

    public int getNumberOfSafeKings (int player) {
        int sum = 0;
        for (int row = 0; row < dimension-1; row++) {
            if (ownerOfField(row, 0) == player && isKing(row, 0)) ++sum;
            if (ownerOfField(row + 1, dimension - 1) == player && isKing(row + 1, dimension - 1)) ++sum;
        }
        for (int col = 0; col < dimension-1; col++) {
            if (ownerOfField(dimension - 1, col) == player && isKing(dimension - 1, col)) ++sum;
            if (ownerOfField(0, col + 1) == player && isKing(0, col + 1)) ++sum;
        }
        return sum;
    }

    public int getNumberOfPossibleMoves (int player) {
        if (player == currentPlayer) return currentPlayerMoves.size();
        return getPossibleMoves(player).size();
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
        return (-1) * player;
    }

    public int opponent () {
        return opponent(currentPlayer);
    }

    public ArrayList<Integer> creationMove () {
        return this.creationMove;
    }

    public ArrayList<Integer> lastMove () {
        return this.lastMove;
    }

    public ArrayList<ArrayList<Integer>> currentPlayerMoves () {
        return this.currentPlayerMoves;
    }

}
