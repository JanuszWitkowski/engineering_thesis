public class Find {
    private static void printSignature () {
        System.out.println("Należy podać argumenty w jednej z następujących konfiguracji:");
        {
            System.out.println("> Pierwsze uruchomienie algorytmu genetycznego");
            {
                System.out.println("\t- Liczebność populacji osobników [liczba naturalna podzielna przez 4];");
                System.out.println("\t- Współczynnik losowej selekcji osobników [liczba całkowita];");
                System.out.println("\t- Szansa na mutację [liczba wymierna między 0 a 1];");
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
    }

    public static void main(String[] args) {
        System.out.println(Console.RED_BOLD + "#### FIND HEURISTIC USING GA ####" + Console.RESET);
        System.out.println(Console.RED_BOLD + "# Szukanie najlepszej strategii #" + Console.RESET);
        int argc = args.length;
        if (argc < 5 && argc > 1) {
            System.out.println("BŁĄD: Niewłaściwa liczba argumentów.");
            printSignature();
            return;
        }
        Genetic ga;
        if (argc == 1) {
            ga = FileHandler.reloadGeneticAlgorithm(args[0]);
        } else if (argc == 0) {
            ga = FileHandler.reloadGeneticAlgorithm();
        } else {
            try {
                int populationSize = Integer.parseInt(args[0]),
                        selectionFactor = Integer.parseInt(args[1]),
                        stopCondTypeNumber = Integer.parseInt(args[3]);
                double mutationChance = Double.parseDouble(args[2]);
                long stopCondThreshold = Long.parseLong(args[4]);
                if (populationSize < 0 || populationSize % 4 != 0)
                    throw new NumberFormatException();
                if (mutationChance > 1.0 || mutationChance < 0.0)
                    throw new NumberFormatException();
                if (stopCondTypeNumber >= StopCondConverter.getNumberOfConds())
                    throw new NumberFormatException();
                if (stopCondThreshold < 0)
                    throw new NumberFormatException();
                ga = new Genetic(populationSize, selectionFactor, mutationChance, stopCondTypeNumber, stopCondThreshold);
            } catch (NumberFormatException e) {
                System.out.println("BŁĄD: Niewłaściwy format danych.");
                printSignature();
                throw new RuntimeException(e);
            }
        }
        ga.GA();
    }
}
