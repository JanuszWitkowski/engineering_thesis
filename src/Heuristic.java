public class Heuristic {
    private short[] paramWeights;
    private final int numberOfParams = HeuristicParameters.values().length;

    public Heuristic() {
        this.paramWeights = new short[numberOfParams];
    }

    public Heuristic (short v) {
        this.paramWeights = new short[numberOfParams];
        for (int i = 0; i < numberOfParams; i++) {
            this.paramWeights[i] = v;
        }
    }

    public int evaluate (State state, int player) {
        if (state.playerHasNoPieces(state.opponent(player))) return Integer.MAX_VALUE;
        if (state.playerHasNoPieces(player)) return Integer.MIN_VALUE;
        if (player == state.currentPlayer() && state.currentPlayerOutOfMoves()) return Integer.MIN_VALUE;
        if (player == state.opponent() && state.currentPlayerOutOfMoves()) return Integer.MAX_VALUE;
        int[] params = getParams(state, player);
        int sum = 0;
        for (int i = 0; i < numberOfParams; i++) {
            sum += params[i] * paramWeights[i];
        }
        return sum;
    }

    private int[] getParams (State state, int player) {
        int[] params = new int[numberOfParams];
        for (int i = 0; i < numberOfParams; i++) params[i] = 0; // TODO
        return params;
    }
}
