import java.util.Arrays;
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

        params[enumToInt(HParam.PAWNS)] = paramPawns(state, player);
        params[enumToInt(HParam.KINGS)] = paramKings(state, player);
        params[enumToInt(HParam.ENEMY_PAWNS)] = paramEnemyPawns(state, player);
        params[enumToInt(HParam.ENEMY_KINGS)] = paramEnemyKings(state, player);
        params[enumToInt(HParam.SAFE_PAWNS)] = paramSafePawns(state, player);
        params[enumToInt(HParam.SAFE_KINGS)] = paramSafeKings(state, player);
        params[enumToInt(HParam.SAFE_ENEMY_PAWNS)] = paramSafeEnemyPawns(state, player);
        params[enumToInt(HParam.SAFE_ENEMY_KINGS)] = paramSafeEnemyKings(state, player);

        params[enumToInt(HParam.MOVABLE_PAWNS)] = paramMovablePawns(state, player);
        params[enumToInt(HParam.MOVABLE_KINGS)] = paramMovableKings(state, player);
        params[enumToInt(HParam.MOVABLE_ENEMY_PAWNS)] = paramMovableEnemyPawns(state, player);
        params[enumToInt(HParam.MOVABLE_ENEMY_KINGS)] = paramMovableEnemyKings(state, player);
        params[enumToInt(HParam.POSSIBLE_MOVES)] = paramPossibleMoves(state, player);
        params[enumToInt(HParam.POSSIBLE_ENEMY_MOVES)] = paramPossibleEnemyMoves(state, player);

        params[enumToInt(HParam.DISTANCE_TO_PROMOTION)] = paramDistanceToPromotion(state, player);
        params[enumToInt(HParam.DISTANCE_TO_ENEMY_PROMOTION)] = paramDistanceToEnemyPromotion(state, player);
        params[enumToInt(HParam.UNOCCUPIED_PROMOTION_FIELDS)] = paramUnoccupiedPromotionFields(state, player);
        params[enumToInt(HParam.UNOCCUPIED_ENEMY_PROMOTION_FIELDS)] = paramUnoccupiedEnemyPromotionFields(state, player);

        params[enumToInt(HParam.LOWERMOST_PAWNS)] = paramLowermostPawns(state, player);
        params[enumToInt(HParam.LOWERMOST_KINGS)] = paramLowermostKings(state, player);
        params[enumToInt(HParam.LOWERMOST_ENEMY_PAWNS)] = paramLowermostEnemyPawns(state, player);
        params[enumToInt(HParam.LOWERMOST_ENEMY_KINGS)] = paramLowermostEnemyKings(state, player);
        params[enumToInt(HParam.CENTRAL_PAWNS)] = paramCentralPawns(state, player);
        params[enumToInt(HParam.CENTRAL_KINGS)] = paramCentralKings(state, player);
        params[enumToInt(HParam.CENTRAL_ENEMY_PAWNS)] = paramCentralEnemyPawns(state, player);
        params[enumToInt(HParam.CENTRAL_ENEMY_KINGS)] = paramCentralEnemyKings(state, player);
        params[enumToInt(HParam.UPPERMOST_PAWNS)] = paramUppermostPawns(state, player);
        params[enumToInt(HParam.UPPERMOST_KINGS)] = paramUppermostKings(state, player);
        params[enumToInt(HParam.UPPERMOST_ENEMY_PAWNS)] = paramUppermostEnemyPawns(state, player);
        params[enumToInt(HParam.UPPERMOST_ENEMY_KINGS)] = paramUppermostEnemyKings(state, player);

        params[enumToInt(HParam.LONER_PAWNS)] = paramLonerPawns(state, player);
        params[enumToInt(HParam.LONER_KINGS)] = paramLonerKings(state, player);
        params[enumToInt(HParam.LONER_ENEMY_PAWNS)] = paramLonerEnemyPawns(state, player);
        params[enumToInt(HParam.LONER_ENEMY_KINGS)] = paramLonerEnemyKings(state, player);

        params[enumToInt(HParam.CORNER_PAWN)] = paramCornerPawn(state, player);
        params[enumToInt(HParam.CORNER_KING)] = paramCornerKing(state, player);
        params[enumToInt(HParam.CORNER_ENEMY_PAWN)] = paramCornerEnemyPawn(state, player);
        params[enumToInt(HParam.CORNER_ENEMY_KING)] = paramCornerEnemyKing(state, player);

        params[enumToInt(HParam.TRIANGLE_PATTERN)] = paramTrianglePattern(state, player);
        params[enumToInt(HParam.OREO_PATTERN)] = paramOreoPattern(state, player);
        params[enumToInt(HParam.BRIDGE_PATTERN)] = paramBridgePattern(state, player);
        params[enumToInt(HParam.DOG_PATTERN)] = paramDogPattern(state, player);
        params[enumToInt(HParam.ENEMY_TRIANGLE_PATTERN)] = paramEnemyTrianglePattern(state, player);
        params[enumToInt(HParam.ENEMY_OREO_PATTERN)] = paramEnemyOreoPattern(state, player);
        params[enumToInt(HParam.ENEMY_BRIDGE_PATTERN)] = paramEnemyBridgePattern(state, player);
        params[enumToInt(HParam.ENEMY_DOG_PATTERN)] = paramEnemyDogPattern(state, player);

        params[enumToInt(HParam.BLOCKING_PIECES)] = paramBlockingPieces(state, player);
        params[enumToInt(HParam.BLOCKING_LINES)] = paramBlockingLines(state, player);
        params[enumToInt(HParam.LONGEST_BLOCKING_LINE)] = paramLongestBlockingLine(state, player);
        params[enumToInt(HParam.BLOCKING_ENEMY_PIECES)] = paramBlockingEnemyPieces(state, player);
        params[enumToInt(HParam.ENEMY_BLOCKING_LINES)] = paramEnemyBlockingLines(state, player);
        params[enumToInt(HParam.LONGEST_ENEMY_BLOCKING_LINE)] = paramLongestEnemyBlockingLine(state, player);

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

    private int paramDistanceToPromotion(State s, int p) {
        return s.getAggregatedDistanceToPromotionLine(p);
    }

    private int paramDistanceToEnemyPromotion(State s, int p) {
        return s.getAggregatedDistanceToPromotionLine(s.opponent(p));
    }

    private int paramUnoccupiedPromotionFields(State s, int p) {
        return s.getNumberOfUnoccupiedPromotionFields(p);
    }

    private int paramUnoccupiedEnemyPromotionFields(State s, int p) {
        return s.getNumberOfUnoccupiedPromotionFields(s.opponent(p));
    }

    private int paramLowermostPawns (State s, int p) {
        return s.getNumberOfLowermostPawns(p, false);
    }

    private int paramLowermostKings (State s, int p) {
        return s.getNumberOfLowermostPawns(p, true);
    }

    private int paramLowermostEnemyPawns (State s, int p) {
        return s.getNumberOfLowermostPawns(s.opponent(p), false);
    }

    private int paramLowermostEnemyKings (State s, int p) {
        return s.getNumberOfLowermostPawns(s.opponent(p), true);
    }

    private int paramCentralPawns (State s, int p) {
        return s.getNumberOfCentralPawns(p, false);
    }

    private int paramCentralKings (State s, int p) {
        return s.getNumberOfCentralPawns(p, true);
    }

    private int paramCentralEnemyPawns (State s, int p) {
        return s.getNumberOfCentralPawns(s.opponent(p), false);
    }

    private int paramCentralEnemyKings (State s, int p) {
        return s.getNumberOfCentralPawns(s.opponent(p), true);
    }

    private int paramUppermostPawns (State s, int p) {
        return s.getNumberOfUppermostPawns(p, false);
    }

    private int paramUppermostKings (State s, int p) {
        return s.getNumberOfUppermostPawns(p, true);
    }

    private int paramUppermostEnemyPawns (State s, int p) {
        return s.getNumberOfUppermostPawns(s.opponent(p), false);
    }

    private int paramUppermostEnemyKings (State s, int p) {
        return s.getNumberOfUppermostPawns(s.opponent(p), true);
    }

    private int paramLonerPawns (State s, int p) {
        return s.getNumberOfLonerPawns(p, false);
    }

    private int paramLonerKings (State s, int p) {
        return s.getNumberOfLonerPawns(p, true);
    }

    private int paramLonerEnemyPawns (State s, int p) {
        return s.getNumberOfLonerPawns(s.opponent(p), false);
    }

    private int paramLonerEnemyKings (State s, int p) {
        return s.getNumberOfLonerPawns(s.opponent(p), true);
    }

    private int paramCornerPawn (State s, int p) {
        return s.presenceOfCornerPawn(p, false) ? 1 : 0;
    }

    private int paramCornerKing (State s, int p) {
        return s.presenceOfCornerPawn(p, true) ? 1 : 0;
    }

    private int paramCornerEnemyPawn (State s, int p) {
        return s.presenceOfCornerPawn(s.opponent(p), false) ? 1 : 0;
    }

    private int paramCornerEnemyKing (State s, int p) {
        return s.presenceOfCornerPawn(s.opponent(p), true) ? 1 : 0;
    }

    private int paramTrianglePattern (State s, int p) {
        return s.presenceOfTrianglePattern(p) ? 1 : 0;
    }

    private int paramOreoPattern (State s, int p) {
        return s.presenceOfOreoPattern(p) ? 1 : 0;
    }

    private int paramBridgePattern (State s, int p) {
        return s.presenceOfBridgePattern(p) ? 1 : 0;
    }

    private int paramDogPattern (State s, int p) {
        return s.presenceOfDogPattern(p) ? 1 : 0;
    }

    private int paramEnemyTrianglePattern (State s, int p) {
        return s.presenceOfTrianglePattern(s.opponent(p)) ? 1 : 0;
    }

    private int paramEnemyOreoPattern (State s, int p) {
        return s.presenceOfOreoPattern(s.opponent(p)) ? 1 : 0;
    }

    private int paramEnemyBridgePattern (State s, int p) {
        return s.presenceOfBridgePattern(s.opponent(p)) ? 1 : 0;
    }

    private int paramEnemyDogPattern (State s, int p) {
        return s.presenceOfDogPattern(s.opponent(p)) ? 1 : 0;
    }

    private int paramBlockingPieces (State s, int p) {
        return s.getNumberOfBlockingPieces(p);
    }

    private int paramBlockingLines (State s, int p) {
        return s.getNumberOfBlockingLines(p);
    }

    private int paramLongestBlockingLine (State s, int p) {
        return s.getLengthOfTheLongestBlockingLine(p);
    }

    private int paramBlockingEnemyPieces (State s, int p) {
        return s.getNumberOfBlockingPieces(s.opponent(p));
    }

    private int paramEnemyBlockingLines (State s, int p) {
        return s.getNumberOfBlockingLines(s.opponent(p));
    }

    private int paramLongestEnemyBlockingLine (State s, int p) {
        return s.getLengthOfTheLongestBlockingLine(s.opponent(p));
    }

}
