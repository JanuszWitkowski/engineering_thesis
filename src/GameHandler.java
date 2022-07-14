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

    public void run () {
        board.printBoardWithCoordinates();
        int winner = board.winner();
        while (winner == 0) {
            player1.makeMove(board);
            board.printBoardWithCoordinates();
            winner = board.winner();
            if (winner != 0) break;
            player2.makeMove(board);
            board.printBoardWithCoordinates();
            winner = board.winner();
        }
        System.out.println("---[ GAME OVER ]---");
        System.out.println("WYGRANY: " + winner);
    }
}
