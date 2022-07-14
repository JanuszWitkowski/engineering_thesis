public class PlayerComputer extends Player {
    private final int depth;
    private final Heuristic heuristic;
    private final MinMax m = new MinMax();
    private final int alpha = Integer.MIN_VALUE;
    private final int beta = Integer.MAX_VALUE;

    public PlayerComputer () {
        super();
        this.depth = 3;
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
    public void makeMove (State board) {
        m.minimax(board, depth, heuristic, alpha, beta, playerNumber, true);
    }

    public int depth () {
        return this.depth;
    }

    public Heuristic heuristic () {
        return this.heuristic;
    }
}
