import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RNG {
    private static final Random rand = ThreadLocalRandom.current();

    public static double randomDoubleFromZeroToOne () {
        return rand.nextDouble();
    }

    public static long randomLong (long minValue, long maxValue) {
//        return rand.nextLong(maxValue - minValue + 1) + minValue;
//        return rand.nextLong(minValue, maxValue + 1);
        long l = rand.nextLong();
        while (l < minValue || l > maxValue) l = rand.nextLong();
        return l;
    }

    public static int randomInt () {
        return rand.nextInt();
    }

    public static int randomInt (int bound) {
        return rand.nextInt(bound);
    }

    public static int randomInt (int minValue, int maxValue) {
        return rand.nextInt(maxValue - minValue + 1) + minValue;
    }

    public static short randomShort () {
        return (short)(rand.nextInt(Short.MAX_VALUE - Short.MIN_VALUE + 1) + Short.MIN_VALUE);
    }

    // Temporary function for testing for Bytes.
    public static byte randomByte () {
        return (byte)(rand.nextInt(Byte.MAX_VALUE - Byte.MIN_VALUE + 1) + Byte.MIN_VALUE);
    }

    public static boolean randomBool () {
        return rand.nextBoolean();
    }
}
