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
            System.out.println("> Szybka gra z głupim komputerem");
            {
                System.out.println("\t- Gracz zaczynający [1: zaczyna człowiek; -1: zaczyna komputer];");
                System.out.println("\t- Głębokość przeszukiwań komputera [liczba naturalna większa od 0].");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.println(Console.GREEN_BOLD + "#### PLAY CHECKERS ####" + Console.RESET);
        System.out.println(Console.GREEN_BOLD + "# Rozgrywki w warcaby #" + Console.RESET);
        int argc = args.length;
        if (argc != 4 && argc != 2) {
            System.out.println(Console.RED_BOLD + "BŁĄD: Niewłaściwa liczba argumentów." + Console.RESET);
            printSignature();
            return;
        }
        GameHandler game;
        Player p1, p2;
        int info1, info2;
        if (argc == 4) {
            System.out.println("## Rozgrywka");
            try {
                info1 = Integer.parseInt(args[0]);
                info2 = Integer.parseInt(args[2]);
                if (info1 < 0 || info2 < 0) throw new NumberFormatException();
                if (info1 == 0) p1 = new PlayerHuman();
                else {
                    String filename = args[1];
                    Heuristic h = new Heuristic(FileHandler.loadWeights(filename));
                    p1 = new PlayerComputer(h, info1);
                }
                if (info2 == 0) p2 = new PlayerHuman();
                else {
                    String filename = args[3];
                    Heuristic h = new Heuristic(FileHandler.loadWeights(filename));
                    p2 = new PlayerComputer(h, info2);
                }
            } catch (NumberFormatException e) {
                System.out.println(Console.RED_BOLD + "BŁĄD: Niewłaściwy format danych." + Console.RESET);
                printSignature();
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println("## Szybka gra z głupim komputerem");
            try {
                info1 = Integer.parseInt(args[0]);
                info2 = Integer.parseInt(args[1]);
                if (info1 != 1 && info1 != -1) throw new NumberFormatException();
                if (info2 <= 0) throw new NumberFormatException();
                Player human = new PlayerHuman(), computer = new PlayerComputer(new Heuristic((short)0), info2);
                p1 = info1 == 1 ? human : computer;
                p2 = info1 == -1 ? computer : human;
            } catch (NumberFormatException e) {
                System.out.println(Console.RED_BOLD + "BŁĄD: Niewłaściwy format danych." + Console.RESET);
                printSignature();
                throw new RuntimeException(e);
            }
        }
        game = new GameHandler(p1, p2);
        System.out.println("Rozpoczynam rozgrywkę.\n");
        game.run();
    }
}
