import java.util.Arrays;

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
            System.out.println("> Reaktywacja algorytmu genetycznego z ostatniego pliku (o ile istnieje) [BRAK ARGUMENTÓW]");
            System.out.println();
            System.out.println("> Kontynuacja algorytmu genetycznego z nowymi parametrami");
            {
                System.out.println("\t- Nazwa pliku;");
                System.out.println("\t- Współczynnik losowej selekcji osobników [liczba całkowita];");
                System.out.println("\t- Szansa na mutację [liczba wymierna między 0 a 1];");
                System.out.println("\t- Rodzaj kryterium stopu [0 - czas (w sekundach), 1 - liczba iteracji];");
                System.out.println("\t- Limit dla kryterium stopu [liczba naturalna].");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.println(Console.BLUE_BOLD + "#### FIND HEURISTIC USING GA ####" + Console.RESET);
        System.out.println(Console.BLUE_BOLD + "# Szukanie najlepszej strategii #" + Console.RESET);
        {
            String[] dirs = FileHandler.getAllNecessaryDirs();
            if (!FileHandler.checkDirectories(dirs)) {
                System.out.println(Console.RED_BOLD + "FATALNY BŁĄD: Nie udało się utworzyć potrzebnych katalogów." + Console.RESET);
                System.out.println("Utwórz ręcznie katalogi: " + Arrays.toString(dirs));
            }
        }
        int argc = args.length;
        if (argc < 5 && argc > 1) {
            System.out.println(Console.RED_BOLD + "BŁĄD: Niewłaściwa liczba argumentów." + Console.RESET);
            printSignature();
            return;
        }
        Genetic ga;
        if (argc == 1) {
            System.out.println("## Reaktywacja algorytmu genetycznego z pliku " + args[0]);
            ga = FileHandler.reloadGeneticAlgorithm(args[0]);
        } else if (argc == 0) {
            System.out.println("## Reaktywacja algorytmu genetycznego z ostatniego pliku w folderze populations/");
            ga = FileHandler.reloadGeneticAlgorithm();
        } else {
            int populationSize, selectionFactor, stopCondTypeNumber;
            double mutationChance;
            long stopCondThreshold;
            short[][] startingPopulation;
            try {
                populationSize = Integer.parseInt(args[0]);
                if (populationSize < 0 || populationSize % 4 != 0)
                    throw new NumberFormatException();
                startingPopulation = Genetic.createStartingPopulation(populationSize);
                System.out.println("## Pierwsze uruchomienie algorytmu genetycznego.");
            } catch (NumberFormatException e) {
                System.out.println("## Kontyuacja algorytmu genetycznego z nowymi parametrami.");
                 startingPopulation = FileHandler.loadPopulation(args[0]);
                 populationSize = startingPopulation.length;
            }
            try {
                selectionFactor = Integer.parseInt(args[1]);
                stopCondTypeNumber = Integer.parseInt(args[3]);
                mutationChance = Double.parseDouble(args[2]);
                stopCondThreshold = Long.parseLong(args[4]);
                if (mutationChance > 1.0 || mutationChance < 0.0)
                    throw new NumberFormatException();
                if (stopCondTypeNumber >= StopCondConverter.getNumberOfConds())
                    throw new NumberFormatException();
                if (stopCondThreshold < 0)
                    throw new NumberFormatException();
                System.out.println("Uruchamiam algorytm genetyczny o następujących parametrach");
                System.out.println("> Liczebność populacji: " + populationSize);
                System.out.println("> Współczynnik selekcji: " + selectionFactor);
                System.out.println("> Szansa na mutację: " + mutationChance);
                System.out.println("> Kryterium stopu: " + stopCondTypeNumber);
                System.out.println("> Limit dla kryterium stopu: " + stopCondThreshold);
                System.out.println();
                ga = new Genetic(startingPopulation, 0, selectionFactor, mutationChance,
                        StopCondConverter.intToEnum(stopCondTypeNumber), 0, stopCondThreshold);
            } catch (NumberFormatException e) {
                System.out.println(Console.RED_BOLD + "BŁĄD: Niewłaściwy format danych." + Console.RESET);
                printSignature();
                throw new RuntimeException(e);
            }
        }
        ga.GA();
    }
}
