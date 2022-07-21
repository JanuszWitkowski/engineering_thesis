public class GameHandler {
    private final Player player1;
    private final Player player2;
    private final State board;

    public GameHandler () {
        this(new PlayerComputer(), new PlayerHuman(), new State());
    }

    public GameHandler (Player player1, Player player2) {
        this(player1, player2, new State());
    }

    public GameHandler (Player player1, Player player2, State board) {
        this.player1 = player1;
        player1.playerNumber(1);
        this.player2 = player2;
        player2.playerNumber(-1);
        this.board = board;
    }

    public void resetBoard () {
        board.initBoard();
    }

    public void printBoard () {
        board.printBoard();
    }

    public void printBoardWithCoordinates () {
        board.printBoardWithCoordinates();
    }

    public int computerDuel () {
        assert player1 instanceof PlayerComputer;
        assert player2 instanceof PlayerComputer;
//                printBoardWithCoordinates();
        while (!board.gameOver()) {
            player1.makeMove(board);
//                printBoardWithCoordinates();
            if (board.gameOver()) break;
            player2.makeMove(board);
//                printBoardWithCoordinates();
        }
        return board.winner();
    }

    public int run () {
        board.printBoardWithCoordinates();
        while (!board.gameOver()) {
            player1.makeMove(board);
            board.printBoardWithCoordinates();
            if (board.gameOver()) break;
            player2.makeMove(board);
            board.printBoardWithCoordinates();
        }
        int winner = board.winner();
        System.out.println("---[ GAME OVER ]---");
        System.out.println("ZWYCIEZCA: " + winner + " (" + State.symbol(winner) + ")");
        return winner;
    }
}
