public class GameHandler {
    private final Player player1;
    private final Player player2;
    private final State board;

    public GameHandler () {
        this(new Player(), new PlayerComputer(), new State());
    }

    public GameHandler (Player player1, Player player2) {
        this(player1, player2, new State());
    }

    public GameHandler (Player player1, Player player2, State board) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
    }

    public void run () {
        //
    }
}
