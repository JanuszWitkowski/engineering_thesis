import java.util.Random;

public class MinMax {
    private final Random rng;

    public MinMax () {
        this.rng = new Random();
    }

    public int minimax(State state, int depth, Heuristic heuristic, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || state.gameOver()) {
            return heuristic.evaluate(state);
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            double maxEps = 0.0;
            for (State child : state.getChildren()) {
                int eval = minimax(child, depth - 1, heuristic, alpha, beta, false);
                if (maxEval <= eval) {
                    /*
                    Stanów o maksymalnej wartości funkcji oceny heurystycznej może być więcej niż jeden.
                    Wówczas losujemy spośród tych stanów, aby strategia nie była zupełnie deterministyczna.
                    Dla każdego stanu o maksymalnej wartości funkcji oceny heurystycznej losujemy epsilon
                    i przyjmujemy ten największy, zatem losowanie stanu jest sprawiedliwe.
                     */
                    double eps = rng.nextDouble();
                    if (maxEps < eps) {
                        maxEval = eval;
                        maxEps = eps;
                    }
                }
                if (alpha < eval) alpha = eval;
                if (beta <= alpha) break;
            }
            return maxEval;
        }
        else {
            int minEval = Integer.MAX_VALUE;
            double minEps = 1.0;
            for (State child : state.getChildren()) {
                int eval = minimax(child, depth - 1, heuristic, alpha, beta, true);
                if (eval <= minEval)  {
                    double eps = rng.nextDouble();
                    if (eps < minEps) {
                        minEval = eval;
                        minEps = eps;
                    }
                }
                if (eval < beta) beta = eval;
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }
}
