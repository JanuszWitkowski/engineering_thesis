/**
 * Klasa wykonawcza programu do obsługi rozgrywki.
 */
public class Play {
    private static void printSignature () {
        System.out.println("Należy podać argumenty w jednej z następujących konfiguracji:");
        {
            System.out.println("> Rozgrywka");
            {
                System.out.println("\t- Typ gracza 1 [0 - człowiek; wartości powyżej 0 - komputer z głębokością przeszukiwań równą podanej liczbie];");
                System.out.println("\t- Ścieżka do pliku z heurystyką dla gracza 1 (program pomija ten argument jeśli wybrano człowieka za gracza;" +
                        "w przypadku niemożności odczytu pliku pojawi się opcja podania heurystyki z wiersza komend lub wygenerowania jej losowo);");
                System.out.println("\t- Typ gracza 2 [0 - człowiek; wartości powyżej 0 - komputer z głębokością przeszukiwań równą podanej liczbie];");
                System.out.println("\t- Ścieżka do pliku z heurystyką dla gracza 2 (program pomija ten argument jeśli wybrano człowieka za gracza;" +
                        "w przypadku niemożności odczytu pliku pojawi się opcja podania heurystyki z wiersza komend lub wygenerowania jej losowo).");
            }
            System.out.println();
            System.out.println("> Szybka gra z komputerem");
            {
                System.out.println("\t- Gracz zaczynający (1: zaczyna człowiek; -1: zaczyna komputer);");
                System.out.println("\t- Głębokość przeszukiwań komputera.");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.println(Console.GREEN_BOLD + "#### PLAY CHECKERS ####" + Console.RESET);
        System.out.println(Console.GREEN_BOLD + "# Rozgrywki w warcaby #" + Console.RESET);
        int argc = args.length;
        if (argc != 4 && argc != 2) {
            System.out.println("BŁĄD: Niewłaściwa liczba argumentów.");
            printSignature();
            return;
        }
        Player p1, p2;
        int info1, info2;
        if (argc == 4) {
            //
        }
    }
}
