/**
 * Klasa odpowiedzialna za obsługę zachowań i ruchów SI.
 * Przechowuje informacje o głębokości przeszukiwań, używanej heurystyce, oraz startowych wartościach do alfa-beta-cięć.
 */
public class PlayerComputer extends Player {
    private final int depth;
    private final Heuristic heuristic;
    private final MinMax m = new MinMax();  // TODO: Może przenieść MinMaxa poza graczy, żeby był tylko jeden?
    private static final int alpha = Integer.MIN_VALUE;
    private static final int beta = Integer.MAX_VALUE;

    public PlayerComputer () {
        super();
        this.depth = 5;
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

    public void changeHeuristicWeights (short[] values) {
        this.heuristic.changeWeights(values);
    }

    /**
     * Uruchamia algorytm minimax na głębokości oraz danym stanie gry i wykonuje optymalny (zdaniem algorytmu) ruch.
     * @param board Stan gry w którym należy wykonać ruch.
     * @param playerNumber Numer gracza, potrzebny do oceny heurystycznej z odpowiedniej perspektywy.
     */
    @Override
    public void makeMove (State board, int playerNumber) {
        m.minimax(board, depth, heuristic, alpha, beta, playerNumber, true);
    }

}
