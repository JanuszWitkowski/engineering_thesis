import java.util.EnumMap;

public class StopCondConverter {
    private static final StopCond[] intToEnum = StopCond.values();
    private static final int numberOfConds = intToEnum.length;
    private static final EnumMap<StopCond, Integer> enumToInt = generateEnumToInt();
    private static EnumMap<StopCond, Integer> generateEnumToInt() {
        EnumMap<StopCond, Integer> map = new EnumMap<>(StopCond.class);
        for (int i = 0; i < numberOfConds; ++i) {
            map.put(intToEnum(i), i);
        }
        return map;
    }

    public static int enumToInt (StopCond s) {
        return enumToInt.get(s);
    }

    public static StopCond intToEnum (int index) {
        return intToEnum[index];
    }

    public static int getNumberOfConds () {
        return numberOfConds;
    }
}
