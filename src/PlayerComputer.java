import java.util.Arrays;

public class PlayerComputer extends Player {
    private final int depth;
    private final Heuristic heuristic;
    private final MinMax m = new MinMax();
    private final int alpha = Integer.MIN_VALUE;
    private final int beta = Integer.MAX_VALUE;

    public PlayerComputer () {
        super();
        this.depth = 5;
        this.heuristic = new Heuristic((short) 0);
    }

    public PlayerComputer (Heuristic heuristic, int depth) {
        super();
        this.depth = depth;
        this.heuristic = heuristic;
    }

    @Override
    public boolean isComputer() {
        return true;
    }

    @Override
    public void makeMove (State board, int playerNumber) {
//        System.out.println("Wykonuję ruch...");
//        System.out.println("Ruch gracza " + playerNumber);
        m.minimax(board, depth, heuristic, alpha, beta, playerNumber, true);
//        int eval = m.minimax(board, depth, heuristic, alpha, beta, playerNumber, true);
//        System.out.println("Wykonałem ruch o wartości oceny " + eval + ". Obecne parametry: " + Arrays.toString(heuristic.getParamsDebug(board, playerNumber)));
    }

    public int depth () {
        return this.depth;
    }

    public Heuristic heuristic () {
        return this.heuristic;
    }
}
