import java.util.Random;

public class Main {
    public static void main(String[] args) {
        State state = new State();
        state.printBoard();
//        state.showNumberedFields();
//        for (int i = -1; i <= 34; i++) {
//            Pair p = state.numberToPair(i);
//            System.out.println(p.l() + " " + p.r());
//        }
//        for (int i = 1; i <= 32; i++) {
//            int row = state.numberToRow(i);
//            int col = state.numberToCol(i);
//            System.out.println(row + " " + col);
//        }
        // initial call
        int depth = 3;
        Heuristic heuristic = new Heuristic();
//        heuristic.generateParamValues();
        MinMax m = new MinMax();
//        m.minimax(state, depth, heuristic, Integer.MIN_VALUE, Integer.MAX_VALUE, 1, true);
    }
}
