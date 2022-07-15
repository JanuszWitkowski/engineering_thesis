import java.util.EnumMap;

public class Heuristic {
    private short[] paramWeights;
    private final int numberOfParams = HParam.values().length;
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

    public Heuristic() {
        this((short)0);     // TODO: Losować wartości
    }

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

    public int evaluate (State state, int player) {
        int winner = state.heuristicWinner(player);
        if (winner == player) return Integer.MAX_VALUE;
        if (winner == state.opponent(player)) return Integer.MIN_VALUE;
        int[] params = getParams(state, player);
        int sum = 0;
        for (int i = 0; i < numberOfParams; i++) {
            sum += params[i] * paramWeights[i];
        }
        return sum;
    }

    private int[] getParams (State state, int player) {
        int[] params = new int[numberOfParams];
        for (int i = 0; i < numberOfParams; i++) params[i] = 0; // TODO
        params[enumToInt.get(HParam.PAWNS)] = state.getNumberOfPawns(player);
        params[enumToInt.get(HParam.KINGS)] = state.getNumberOfKings(player);
        params[enumToInt.get(HParam.SAFE_PAWNS)] = state.getNumberOfSafePawns(player);
        params[enumToInt.get(HParam.SAFE_KINGS)] = state.getNumberOfSafeKings(player);
        params[enumToInt.get(HParam.MOVABLE_PAWNS)] = state.getNumberOfMovablePawns(player);
        params[enumToInt.get(HParam.MOVABLE_KINGS)] = state.getNumberOfMovableKings(player);
        return params;
    }
}
