//import java.util.ArrayList;
import java.util.Random;

public class MinMax {
    private final Random rng;

    public MinMax () {
        this.rng = new Random();
    }

    // TODO: Wykonywać ruchy na planszy.
    public int minimax(State state, int depth, Heuristic heuristic, int alpha, int beta, int maximizingPlayer, boolean isPlayerMaximizing) {
        if (depth == 0) {
            return heuristic.evaluate(state, maximizingPlayer);
        }

        State best = null;
        if (isPlayerMaximizing) {
//            if (state.opponentHasNoPieces()) {
//                return Integer.MAX_VALUE;
//            }
//            if (state.currentPlayerHasNoPieces()) {
//                return Integer.MIN_VALUE;
//            }
            int maxEval = Integer.MIN_VALUE;
            double maxEps = 0.0;
//            ArrayList<State> children = state.getChildren();
            for (State child : state.getChildren()) {
                int eval = minimax(child, depth - 1, heuristic, alpha, beta, maximizingPlayer, false);
                if (maxEval <= eval) {
                    /*
                    Stanów o maksymalnej wartości funkcji oceny heurystycznej może być więcej niż jeden.
                    Wówczas losujemy spośród tych stanów, aby strategia nie była zupełnie deterministyczna.
                    Dla każdego stanu o maksymalnej wartości funkcji oceny heurystycznej losujemy epsilon
                    i przyjmujemy ten największy, zatem losowanie stanu jest sprawiedliwe.
                     */
                    double eps = rng.nextDouble();
                    if (maxEps < eps) {
                        best = child;
                        maxEval = eval;
                        maxEps = eps;
                    }
                }
                if (alpha < eval) alpha = eval;
                if (beta <= alpha) break;
            }
            assert best != null;
            state.makeMove(best.lastMoveList());    // Wykonaj najlepszy ruch na oryginalnej planszy.
            return maxEval;
        }
        else {
//            if (state.opponentHasNoPieces()) {
//                return Integer.MIN_VALUE;
//            }
//            if (state.currentPlayerHasNoPieces()) {
//                return Integer.MAX_VALUE;
//            }
            int minEval = Integer.MAX_VALUE;
            double minEps = 1.0;
            for (State child : state.getChildren()) {
                int eval = minimax(child, depth - 1, heuristic, alpha, beta, maximizingPlayer, true);
                if (eval <= minEval)  {
                    double eps = rng.nextDouble();
                    if (eps < minEps) {
                        best = child;
                        minEval = eval;
                        minEps = eps;
                    }
                }
                if (eval < beta) beta = eval;
                if (beta <= alpha) break;
            }
            assert best != null;
            state.makeMove(best.lastMoveList());
            return minEval;
        }
    }
}
