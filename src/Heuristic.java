public class Heuristic {
    private short[] paramWeights;
    private final int numberOfParams = HeuristicParameters.values().length;

    public Heuristic() {
        this.paramWeights = new short[numberOfParams];
    }

    public int evaluate(State state) {
        return 0;
    }
}
