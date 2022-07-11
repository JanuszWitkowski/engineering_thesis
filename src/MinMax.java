public class MinMax {

//    public static int minimax(State state, int depth, Heuristic heuristic, int alpha, int beta, boolean maximizingPlayer) {
//        if (depth == 0 || state.gameOver()) {
//            return heuristic.evaluate(state);
//        }
//
//        if (maximizingPlayer) {
//            int maxEval = Integer.MIN_VALUE;
//            for (State child : state.getChildren()) {
//                int eval = minimax(child, depth - 1, alpha, beta, false);
//                if (maxEval < eval) maxEval = eval;   // TODO: wprowadź element losowy (wylosuj z grupy najlepszych o tej samej wartości oceny)
//                if (alpha < eval) alpha = eval;
//                if (beta <= alpha) break;
//            }
//            return maxEval;
//        }
//        else {
//            int minEval = Integer.MAX_VALUE;
//            for (State child : state.getChildren()) {
//                int eval = minimax(child, depth - 1, alpha, beta, true);
//                if (eval < minEval) minEval = eval;
//                if (eval < beta) beta = eval;
//                if (beta <= alpha) break;
//            }
//            return minEval;
//        }
//    }
}
