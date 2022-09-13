public class Find {
    public static void main(String[] args) {
        int argc = args.length;
        if (argc < 5 && argc > 1) {
            System.out.println("BŁĄD: Należy podać argumenty w jednej z następujących konfiguracji:");
            {
                System.out.println("> Pierwsze uruchomienie algorytmu genetycznego");
                {
                    System.out.println("\t- Liczebność populacji osobników [liczba naturalna];");
                    System.out.println("\t- Szansa na mutację [liczba wymierna między 0 a 1];");
                    System.out.println("\t- Współczynnik losowej selekcji osobników [liczba całkowita];");
                    System.out.println("\t- Rodzaj kryterium stopu [0 - czas (w sekundach), 1 - liczba iteracji];");
                    System.out.println("\t- Limit dla kryterium stopu [liczba naturalna].");
                }
                System.out.println();
                System.out.println("> Reaktywacja algorytmu genetycznego z wybranego pliku");
                {
                    System.out.println("\t- Nazwa pliku.");
                }
                System.out.println();
                System.out.println("> Reaktywacja algorytmu genetycznego z ostatniego pliku (o ile istnieje)");
            }
            return;
        }
        int populationSize, selectionFactor, stopNumber;
        long threshold;
        double mutationChance;
//        if (argc >)
    }
}
