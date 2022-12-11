/**
 * Odpowiedzialny za stworzenie, obsługę i przeprowadzenie pojedynczej gry.
 * Przechowuje informacje o graczach, ich numerach, oraz stanie gry.
 */
public class GameHandler {
    private final Player player1;
    private final Player player2;
    private static final int player1Number = 1;
    private static final int player2Number = -1;
    private final State board;

    /**
     * Tworzy prostą rozgrywkę między komputerem a człowiekiem.
     */
    public GameHandler () {
        this(new PlayerComputer(), new PlayerHuman(), new State());
    }

    /**
     * Tworzy świeżą grę dla wybranych graczy.
     * @param player1 Gracz pierwszy
     * @param player2 Gracz drugi
     */
    public GameHandler (Player player1, Player player2) {
        this(player1, player2, new State());
    }

    /**
     * Tworzy rozgrywkę z wybranymi graczami oraz planszą.
     * @param player1 Gracz pierwszy
     * @param player2 Gracz drugi
     * @param board Stan gry
     */
    public GameHandler (Player player1, Player player2, State board) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
    }

    /**
     * Czyści planszę i rozpoczyna grę od nowa.
     */
    public void resetBoard () {
        board.initBoard();
    }

    /**
     * Drukuje planszę bez współrzędnych.
     */
    public void printBoard () {
        board.printBoard();
    }

    /**
     * Drukuje planszę ze współrzędnymi.
     */
    public void printBoardWithCoordinates () {
        board.printBoardWithCoordinates();
    }

    /**
     * Przeprowadza grę bez drukowania planszy.
     * Metoda wykorzystywana do szybkich gier, zazwyczaj między dwoma SI.
     * @return Numer zwycięzcy
     */
    public int quickGame () {
        while (!board.gameOver()) {
            player1.makeMove(board, player1Number);
            if (board.gameOver()) break;
            player2.makeMove(board, player2Number);
        }
        return board.winner();
    }

    /**
     * Przeprowadza rozgrywkę z pełnym drukowaniem interfejsu.
     * Służy do przeprowadzania gier z graczem ludzkim.
     * @return Numer zwycięzcy
     */
    public int run () {
        board.printBoardWithCoordinates();
        while (!board.gameOver()) {
            player1.makeMove(board, player1Number);
            board.printBoardWithCoordinates();
            if (board.gameOver()) break;
            player2.makeMove(board, player2Number);
            board.printBoardWithCoordinates();
        }
        int winner = board.winner();
        System.out.println("---[ GAME OVER ]---");
        System.out.println("ZWYCIEZCA: " + winner + " (" + State.symbol(winner) + ")");
        return winner;
    }
}
