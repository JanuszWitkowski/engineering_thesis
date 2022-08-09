import java.util.EnumMap;
import java.util.Random;

/**
 * Obsługuje liczenie wartości funkcji oceny heurystycznej.
 * Ocena heurystyczna opiera się o szereg parametrów (jak np. liczba pionków, dostępnych ruchów), którym każda heurystyka
 * przypisuje wagi. Suma ważona tych parametrów daje wartość oceny danego stanu gry.
 */
public class Heuristic {
    private short[] paramWeights;   // TODO: Może zrobić final?
    private final int numberOfParams = HParam.values().length;

    /**
     * Mapa ujednolicająca translację typu wyliczeniowego na indeksy tablicy parametrów i ich wag.
     */
    private static final EnumMap<HParam, Integer> enumToInt = generateEnumToInt();

    private static EnumMap<HParam, Integer> generateEnumToInt () {
        EnumMap<HParam, Integer> map = new EnumMap<>(HParam.class);
        int i = 0;
        for (HParam param : HParam.values()) {
            map.put(param, i);
            ++i;
        }
        return map;
    }

    public static int enumToInt (HParam p) {
        return enumToInt.get(p);
    }

    /**
     * Konstruktor losujący wartości wag. W trakcie implementacji algorytmu genetycznego może się okazać,
     * że logikę tworzenia wag parametrów należy umieścić poza odpowiedzialność klasy heurystyki.
     */
    public Heuristic() {    // TODO: Zastanowić się czy losować wartości w konstruktorze, czy poza nim (chodzi o występowanie klasy Random).
        paramWeights = new short[numberOfParams];
        Random rng = new Random();
        int rangeMin = Short.MIN_VALUE, rangeMax = Short.MAX_VALUE;
        int range = rangeMax - rangeMin + 1;
        for (int i = 0; i < numberOfParams; ++i) {
            paramWeights[i] = (short)(rng.nextInt(range) + rangeMin);
        }
    }

    /**
     * Konstruktor służący bardziej jako narzędzie do testowania/debugowania.
     * Ustawia wartość wagi każdego parametru na jedną podaną wartość.
     * @param v Wartość do przypisania dla wagi każdego parametru
     */
    public Heuristic (short v) {
        short[] values = new short[numberOfParams];
        for (int i = 0; i < numberOfParams; i++) {
            values[i] = v;
        }
        this.paramWeights = values;
    }

    public Heuristic (short[] values) {
        assert values.length == numberOfParams;
        this.paramWeights = values;
    }

    /**
     * Narzędzie służące mutacji w algorytmie genetycznym. Może się okazać nieprzydatne
     * jeśli wyjmiemy z tej klasy logikę tworzenia tablicy wag.
     * @param index Numer parametru do zaaplikowania mutacji
     * @param value Nowa wartość wagi parametru po mutacji
     */
    public void changeParamWeight (int index, short value) {
        this.paramWeights[index] = value;
    }

    /**
     * Funkcja oceny heurystycznej.
     * @param state Obecny stan gry
     * @param player Numer gracza z którego perspektywy należy wyliczyć wartość oceny
     * @return Wartość funkcji oceny heurystycznej danego pola dla danego gracza
     */
    public int evaluate (State state, int player) {
        int[] params = getParams(state, player);
        int sum = 0;
        for (int i = 0; i < numberOfParams; i++) {
            sum += params[i] * (int)paramWeights[i];
        }
        return sum;
    }

    // TODO: Funkcja do debugowania, po ukończeniu projektu należy usunąć.
    public int[] getParamsDebug (State state, int player) {
        return getParams(state, player);
    }

    /**
     * Funkcja pomocnicza, wykorzystywana w funkcji oceny heurystycznej.
     * @param state Obecny stan gry
     * @param player Numer gracza z którego perspektywy należy wyliczyć wartość oceny
     * @return Tablica wartości parametrów danego stanu gry (np. liczba pionków)
     */
    private int[] getParams (State state, int player) {
        int[] params = new int[numberOfParams];
        for (int i = 0; i < numberOfParams; i++) params[i] = 0; // TODO: Tymczasowe rozwiązanie, w pełnej implementacji należy usunąć.
        params[enumToInt.get(HParam.PAWNS)] = paramPawns(state, player);
        params[enumToInt.get(HParam.KINGS)] = paramKings(state, player);
        params[enumToInt.get(HParam.ENEMY_PAWNS)] = paramEnemyPawns(state, player);
        params[enumToInt.get(HParam.ENEMY_KINGS)] = paramEnemyKings(state, player);
        params[enumToInt.get(HParam.SAFE_PAWNS)] = paramSafePawns(state, player);
        params[enumToInt.get(HParam.SAFE_KINGS)] = paramSafeKings(state, player);
        params[enumToInt.get(HParam.SAFE_ENEMY_PAWNS)] = paramSafeEnemyPawns(state, player);
        params[enumToInt.get(HParam.SAFE_ENEMY_KINGS)] = paramSafeEnemyKings(state, player);
        params[enumToInt.get(HParam.MOVABLE_PAWNS)] = paramMovablePawns(state, player);
        params[enumToInt.get(HParam.MOVABLE_KINGS)] = paramMovableKings(state, player);
        params[enumToInt.get(HParam.MOVABLE_ENEMY_PAWNS)] = paramMovableEnemyPawns(state, player);
        params[enumToInt.get(HParam.MOVABLE_ENEMY_KINGS)] = paramMovableEnemyKings(state, player);
        params[enumToInt.get(HParam.POSSIBLE_MOVES)] = paramPossibleMoves(state, player);
        params[enumToInt.get(HParam.POSSIBLE_ENEMY_MOVES)] = paramPossibleEnemyMoves(state, player);
        return params;
    }

    /*
    Poniżej znajdują się funkcje wyłuskujące wartości parametrów.
    Rozbicie na funkcje pozwala na ewentualne nadpisywanie funkcji przez klasy dziedziczące
    (jeśli np. chcemy podnieść liczbę damek do kwadratu).
     */

    private int paramPawns (State s, int p) {
        return s.getNumberOfPawns(p, false);
    }

    private int paramKings (State s, int p) {
        return s.getNumberOfPawns(p, true);
    }

    private int paramEnemyPawns (State s, int p) {
        return s.getNumberOfPawns(s.opponent(p), false);
    }

    private int paramEnemyKings (State s, int p) {
        return s.getNumberOfPawns(s.opponent(p), true);
    }

    private int paramSafePawns (State s, int p) {
        return s.getNumberOfSafePawns(p, false);
    }

    private int paramSafeKings (State s, int p) {
        return s.getNumberOfSafePawns(p, true);
    }

    private int paramSafeEnemyPawns(State s, int p) {
        return s.getNumberOfSafePawns(s.opponent(p), false);
    }

    private int paramSafeEnemyKings(State s, int p) {
        return s.getNumberOfSafePawns(s.opponent(p), true);
    }

    private int paramMovablePawns(State s, int p) {
        return s.getNumberOfMovablePawns(p, false);
    }

    private int paramMovableKings(State s, int p) {
        return s.getNumberOfMovablePawns(p, true);
    }

    private int paramMovableEnemyPawns(State s, int p) {
        return s.getNumberOfMovablePawns(s.opponent(p), false);
    }

    private int paramMovableEnemyKings(State s, int p) {
        return s.getNumberOfMovablePawns(s.opponent(p), true);
    }

    private int paramPossibleMoves (State s, int p) {
        return s.getNumberOfPossibleMoves(p);
    }

    private int paramPossibleEnemyMoves(State s, int p) {
        return s.getNumberOfPossibleMoves(s.opponent(p));
    }

}
