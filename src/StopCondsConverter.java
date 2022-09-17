import java.util.EnumMap;

public class StopCondsConverter {
    private static final StopConds[] intToEnum = StopConds.values();
    private static final int numberOfConds = intToEnum.length;
    private static final EnumMap<StopConds, Integer> enumToInt = generateEnumToInt();
    private static EnumMap<StopConds, Integer> generateEnumToInt() {
        EnumMap<StopConds, Integer> map = new EnumMap<>(StopConds.class);
        for (int i = 0; i < numberOfConds; ++i) {
            map.put(intToEnum(i), i);
        }
        return map;
    }

    public static int enumToInt (StopConds s) {
        return enumToInt.get(s);
    }

    public static StopConds intToEnum (int index) {
        return intToEnum[index];
    }
}
