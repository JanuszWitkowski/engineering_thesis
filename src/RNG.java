import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RNG {
    private static final Random rand = ThreadLocalRandom.current();

    public static double randomDoubleFromZeroToOne () {
        return rand.nextDouble();
    }

    public static long randomLong (long minValue, long maxValue) {
        return (long)((rand.nextDouble() * (maxValue - minValue + 1)) + minValue);
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

    public static short randomShort (short minValue, short maxValue) {
        return (short)(rand.nextInt(maxValue - minValue + 1) + minValue);
    }

    public static short randomShort () {
        return randomShort(Short.MIN_VALUE, Short.MAX_VALUE);
    }

    // Temporary function for testing for Bytes.
    public static byte randomByte () {
        return (byte)(rand.nextInt(Byte.MAX_VALUE - Byte.MIN_VALUE + 1) + Byte.MIN_VALUE);
    }

    public static boolean randomBool () {
        return rand.nextBoolean();
    }
}
