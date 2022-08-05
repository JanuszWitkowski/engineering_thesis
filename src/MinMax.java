import java.util.Random;

public class MinMax {
    private final Random rng;

    public MinMax () {
        this.rng = new Random();
    }

    public int minimax(State state, int depth, Heuristic heuristic, int alpha, int beta, int maximizingPlayer, boolean isPlayerMaximizing) {
        if (depth == 0) return heuristic.evaluate(state, maximizingPlayer);

        State best = null;
        if (isPlayerMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            double maxEps = 0.0;
            for (State child : state.getChildren()) {
                int eval = minimax(child, depth - 1, heuristic, alpha, beta, maximizingPlayer, false);
                System.out.println(depth + ":" + isPlayerMaximizing + ":" + eval);
                if (maxEval < eval) {
                    best = child;
                    maxEval = eval;
                }
                else if (maxEval == eval) {
                    /*
                    Stanów o maksymalnej wartości funkcji oceny heurystycznej może być więcej niż jeden.
                    Wówczas losujemy spośród tych stanów, aby strategia nie była zupełnie deterministyczna.
                    Dla każdego stanu o maksymalnej wartości funkcji oceny heurystycznej losujemy epsilon
                    i przyjmujemy ten największy, zatem losowanie stanu jest sprawiedliwe.
                     */
                    double eps = rng.nextDouble();
                    if (maxEps < eps) {
                        best = child;
                        maxEps = eps;
                    }
                }
                if (alpha < eval) alpha = eval;
                if (beta <= alpha) break;
            }
            if (best != null) state.makeMove(best.creationMove());    // Wykonaj najlepszy ruch na oryginalnej planszy.
            System.out.println(depth + ":-" + isPlayerMaximizing + "->" + maxEval);
            return maxEval;
        }
        else {
            int minEval = Integer.MAX_VALUE;
            double minEps = 1.0;
            for (State child : state.getChildren()) {
                int eval = minimax(child, depth - 1, heuristic, alpha, beta, maximizingPlayer, true);
                System.out.println(depth + ":" + isPlayerMaximizing + ":" + eval);
                if (eval < minEval) {
                    best = child;
                    minEval = eval;
                }
                else if (eval == minEval)  {
                    double eps = rng.nextDouble();
                    if (eps < minEps) {
                        best = child;
                        minEps = eps;
                    }
                }
                if (eval < beta) beta = eval;
                if (beta <= alpha) break;
            }
            if (best != null) state.makeMove(best.creationMove());
            System.out.println(depth + ":-" + isPlayerMaximizing + "-> " + minEval);
            return minEval;
        }
    }
}
