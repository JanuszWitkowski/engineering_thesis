import java.util.Arrays;
import java.util.EnumMap;
import java.util.Objects;

public class Show {
    private static final HParam[] intToEnum = HParam.values();
    private static final int numberOfParams = intToEnum.length;
    private static final EnumMap<HParam, Integer> enumToInt = generateEnumToInt();
    private static final EnumMap<HParam, String> enumToString = generateEnumToString();

    private static EnumMap<HParam, Integer> generateEnumToInt() {
        EnumMap<HParam, Integer> map = new EnumMap<>(HParam.class);
        for (int i = 0; i < numberOfParams; ++i) {
            map.put(intToEnum[i], i);
        }
        return map;
    }

    private static EnumMap<HParam, String> generateEnumToString() {
        EnumMap<HParam, String> map = new EnumMap<>(HParam.class);
        map.put(HParam.PAWNS, "Liczba sojuszniczych pionów");
        map.put(HParam.KINGS, "Liczba sojuszniczych damek");
        map.put(HParam.ENEMY_PAWNS, "Liczba przeciwnych pionów");
        map.put(HParam.ENEMY_KINGS, "Liczba przeciwnych damek");
        map.put(HParam.SAFE_PAWNS, "Liczba sojuszniczych pionów przy ścianie");
        map.put(HParam.SAFE_KINGS, "Liczba sojuszniczych damek przy ścianie");
        map.put(HParam.SAFE_ENEMY_PAWNS, "Liczba przeciwnych pionów przy ścianie");
        map.put(HParam.SAFE_ENEMY_KINGS, "Liczba przeciwnych damek przy ścianie");
        map.put(HParam.MOVABLE_PAWNS, "Liczba ruchomych pionów gracza");
        map.put(HParam.MOVABLE_KINGS, "Liczba ruchomych damek gracza");
        map.put(HParam.MOVABLE_ENEMY_PAWNS, "Liczba ruchomych pionów przeciwnika");
        map.put(HParam.MOVABLE_ENEMY_KINGS, "Liczba ruchomych damek przeciwnika");
        map.put(HParam.POSSIBLE_MOVES, "Liczba możliwych ruchów gracza");
        map.put(HParam.POSSIBLE_ENEMY_MOVES, "Liczba możliwych ruchów przeciwnika");
        map.put(HParam.CAPTURE_MOVE_EXISTS, "Istnienie bijącego ruchu gracza");
        map.put(HParam.CAPTURE_MOVES, "Liczba bijących ruchów gracza");
        map.put(HParam.LONGEST_CAPTURE_MOVE, "Rozmiar najdłuższego bijącego ruchu gracza");
        map.put(HParam.ENEMY_CAPTURE_MOVE_EXISTS, "Istnienie bijącego ruchu przeciwnika");
        map.put(HParam.ENEMY_CAPTURE_MOVES, "Liczba bijących ruchów przeciwnika ");
        map.put(HParam.LONGEST_ENEMY_CAPTURE_MOVE, "Rozmiar najdłuższego bijącego ruchu przeciwnika");
        map.put(HParam.DISTANCE_TO_PROMOTION, "Suma dystansów pionów gracza do rzędu awansu");
        map.put(HParam.DISTANCE_TO_ENEMY_PROMOTION, "Suma dystansów pionów przeciwnika do rzędu awansu");
        map.put(HParam.UNOCCUPIED_PROMOTION_FIELDS, "Liczba niezajętych pól w rzędzie awansu gracza");
        map.put(HParam.UNOCCUPIED_ENEMY_PROMOTION_FIELDS, "Liczba niezajętych pól w rzędzie awansu przeciwnika");
        map.put(HParam.LOWERMOST_PAWNS, "Liczba sojuszniczych pionów w dolnych rzędach ");
        map.put(HParam.LOWERMOST_KINGS, "Liczba sojuszniczych damek w dolnych rzędach");
        map.put(HParam.LOWERMOST_ENEMY_PAWNS, "Liczba przeciwnych pionów w dolnych rzędach");
        map.put(HParam.LOWERMOST_ENEMY_KINGS, "Liczba przeciwnych damek w dolnych rzędach");
        map.put(HParam.CENTRAL_PAWNS, "Liczba sojuszniczych pionów w środkowych rzędach");
        map.put(HParam.CENTRAL_KINGS, "Liczba sojuszniczych damek w środkowych rzędach");
        map.put(HParam.CENTRAL_ENEMY_PAWNS, "Liczba przeciwnych pionów w środkowych rzędach");
        map.put(HParam.CENTRAL_ENEMY_KINGS, "Liczba przeciwnych damek w środkowych wierszach");
        map.put(HParam.UPPERMOST_PAWNS, "Liczba sojuszniczych pionów w górnych rzędach");
        map.put(HParam.UPPERMOST_KINGS, "Liczba sojuszniczych damek w górnych rzędach");
        map.put(HParam.UPPERMOST_ENEMY_PAWNS, "Liczba przeciwnych pionów w górnych rzędach");
        map.put(HParam.UPPERMOST_ENEMY_KINGS, "Liczba przeciwnych damek w górnych rzędach");
        map.put(HParam.LONER_PAWNS, "Liczba samotnych sojuszniczych pionów");
        map.put(HParam.LONER_KINGS, "Liczba samotnych sojuszniczych damek");
        map.put(HParam.LONER_ENEMY_PAWNS, "Liczba samotnych przeciwnych pionów");
        map.put(HParam.LONER_ENEMY_KINGS, "Liczba samotnych przeciwnych damek");
        map.put(HParam.CORNER_PAWN, "Czy pion gracza jest w kącie");
        map.put(HParam.CORNER_KING, "Czy damka gracza jest w kącie");
        map.put(HParam.DOUBLE_CORNER, "Czy gracz zajmuje dwa kąty");
        map.put(HParam.CORNER_ENEMY_PAWN, "Czy pion przeciwnika jest w kącie");
        map.put(HParam.CORNER_ENEMY_KING, "Czy damka przeciwnika jest w kącie");
        map.put(HParam.DOUBLE_ENEMY_CORNER, "Czy przeciwnik zajmuje dwa kąty");
        map.put(HParam.TRIANGLE_PATTERN, "Obecność „Triangle pattern” u gracza");
        map.put(HParam.OREO_PATTERN, "Obecność „Oreo pattern” u gracza");
        map.put(HParam.BRIDGE_PATTERN, "Obecność „Bridge pattern” u gracza");
        map.put(HParam.DOG_PATTERN, "Obecność „Dog pattern” u gracza");
        map.put(HParam.ENEMY_TRIANGLE_PATTERN, "Obecność „Triangle pattern” u przeciwnika");
        map.put(HParam.ENEMY_OREO_PATTERN, "Obecność „Oreo pattern” u przeciwnika");
        map.put(HParam.ENEMY_BRIDGE_PATTERN, "Obecność „Bridge pattern” u przeciwnika");
        map.put(HParam.ENEMY_DOG_PATTERN, "Obecność „Dog pattern” u przeciwnika");
        map.put(HParam.BLOCKING_PIECES, "Liczba blokujących sojuszniczych figur");
        map.put(HParam.BLOCKING_LINES, "Liczba linii bloku gracza");
        map.put(HParam.LONGEST_BLOCKING_LINE, "Wielkość najdłuższej linii bloku gracza");
        map.put(HParam.BLOCKING_ENEMY_PIECES, "Liczba blokujących przeciwnych figur");
        map.put(HParam.ENEMY_BLOCKING_LINES, "Liczba linii bloku przeciwnika");
        map.put(HParam.LONGEST_ENEMY_BLOCKING_LINE, "Wielkość najdłuższej linii bloku przeciwnika");
        return map;
    }


    public static void printHeuristic (short[] weights) {
        for (int i = 0; i < numberOfParams; ++i) {
            System.out.println(enumToString.get(intToEnum[i]) + ": " + weights[i]);
        }
    }

    public static void printHeuristic (Heuristic h) {
        printHeuristic(h.getParamWeights());
    }


    private static void printSignature () {
        System.out.println("Należy podać argumenty w jednej z następujących konfiguracji:");
        {
            System.out.println("> Wydrukowanie wag heurystyki");
            {
                System.out.println("\t- Ścieżka do pliku ciągu wag.");
            }
            System.out.println();
        }
    }


    public static void main (String[] args) {
        System.out.println(Console.PURPLE_BOLD + "#### SHOW HEURISTIC ####" + Console.RESET);
        System.out.println(Console.PURPLE_BOLD + "# Drukowanie ciągu wag #" + Console.RESET);
        int argc = args.length;
        if (argc == 0 || Objects.equals(args[0], "--help")) {
            printSignature();
            return;
        }
        try {
            Heuristic h = FileHandler.loadAndRecreate(args[0]);
            printHeuristic(h);
        } catch (Exception e) {
            printSignature();
            throw new RuntimeException(e);
        }
    }
}
