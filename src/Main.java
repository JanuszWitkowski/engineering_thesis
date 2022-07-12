import java.util.Random;

public class Main {
    public static void main(String[] args) {
        State state = new State();
        state.printBoard();
        state.showNumberedFields();
        // initial call
        int depth = 3;
        Heuristic heuristic = new Heuristic();
//        heuristic.generateParamValues();
        MinMax m = new MinMax();
//        m.minimax(state, depth, heuristic, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
    }
}
