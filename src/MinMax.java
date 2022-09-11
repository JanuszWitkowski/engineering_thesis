import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Główną odpowiedzialnością tej klasy jest poprawne przeprowadzanie rozumowania minimax.
 */
public class MinMax {
    /**
     * Służy do podejmowania losowych decyzji w przyjmowaniu potencjalnych ruchów o tej samej wartości oceny.
     */

    /**
     * Rekurencyjny algorytm minimax z alfa-beta-cięciami. Wykonuje ruch na planszy w trakcie działania.
     * @param state Rozpatrywany stan gry
     * @param depth Głębokość przeszukiwań, algorytm kończy przeszukiwania gdy tę głębokość osiągnie i zwraca wartość oceny
     * @param heuristic Funkcja oceny heurystycznej
     * @param alpha Minimalny wynik o którym wie gracz MAX, służy do optymalizacji w postaci alpha-beta-pruning
     * @param beta Maksymalny wynik o którym wie gracz MIN, służy do optymalizacji w postaci alpha-beta-pruning
     * @param maximizingPlayer Numer gracza z którego perspektywy należy wyznaczać wartość oceny
     * @param isPlayerMaximizing Determinuje czy obecnie rozpatrujący gracz chce maksymalizować czy minimalizować wartość oceny
     * @return Wartość potencjału ruchu który został wykonany
     */
    public int minimax(State state, int depth, Heuristic heuristic, int alpha, int beta, int maximizingPlayer, boolean isPlayerMaximizing) {
        // Gdy osiągniemy maksymalną głębokość przeszukiwań, zwracamy od razu wartość oceny danego stanu gry.
        if (depth == 0) return heuristic.evaluate(state, maximizingPlayer);

        State best = null;
        if (isPlayerMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            double maxEps = 0.0;
            for (State child : state.getChildren()) {
                // Rekurencyjnie przeszukaj przestrzeń stanów które można osiągnąć z obecnego stanu.
                int eval = minimax(child, depth - 1, heuristic, alpha, beta, maximizingPlayer, false);
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
                    double eps = RNG.randomDoubleFromZeroToOne();
                    if (maxEps < eps) {
                        best = child;
                        maxEps = eps;
                    }
                }
                if (alpha < eval) alpha = eval;
                if (beta <= alpha) break;   // Nie musimy przeszukiwać reszty przesztrzeni stanów jeśli możemy zastosować cięcie.
            }
            if (best != null) state.makeMove(best.creationMove());    // Wykonaj najlepszy ruch na oryginalnej planszy.
            return maxEval;
        }
        else {
            int minEval = Integer.MAX_VALUE;
            double minEps = 1.0;
            for (State child : state.getChildren()) {
                int eval = minimax(child, depth - 1, heuristic, alpha, beta, maximizingPlayer, true);
                if (eval < minEval) {
                    best = child;
                    minEval = eval;
                }
                else if (eval == minEval)  {
                    double eps = RNG.randomDoubleFromZeroToOne();
                    if (eps < minEps) {
                        best = child;
                        minEps = eps;
                    }
                }
                if (eval < beta) beta = eval;
                if (beta <= alpha) break;
            }
            if (best != null) state.makeMove(best.creationMove());
            return minEval;
        }
    }
}
