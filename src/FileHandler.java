import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Statyczna klasa odpowiedzialna za operacje na plikach w katalogu heuristics.
 */
public class FileHandler {

    private static final String globalDirectory = "heuristics";
    private static final String singleHeuristicsDirectory = globalDirectory + "/single";
    private static final String populationsDirectory = globalDirectory + "/populations";
    private static final String archiveDirectory = globalDirectory + "/archive";
    private static final String gaOutputDirectory = globalDirectory + "/output";

    /**
     * Pakuje nazwy potrzebnych podkatalogów w tablicę.
     * @return Tablica nazw używanych podkatalogów
     */
    public static String[] getAllNecessaryDirs () {
        return new String[]{globalDirectory, singleHeuristicsDirectory,
        populationsDirectory, archiveDirectory, gaOutputDirectory};
    }

    /**
     * Sprawdza dostępność podkatalogów. Tworzy podkatalogi jeżeli ich nie znajdzie.
     * @param dirs Tablica nazw katalogów do sprawdzenia
     * @return True jeżeli nie udało się stworzyć któregoś podkatalogu; False w przeciwnym przypadku
     */
    public static boolean checkIfDirectoriesExist(String[] dirs) {
        for (String dirName : dirs) {
            File directory = new File(dirName);
            if (!directory.exists()) {
                if (!directory.mkdirs()) return true;
            }
        }
        return false;
    }


    //Poniżej znajdują się metody do zapisu i odczytu wag parametrów heurystyki.

    /**
     * Zapisuje postęp sesji algorytmu genetycznego (populację osobników) w pliku o podanej nazwie.
     * @param filename Nazwa docelowego pliku
     * @param population Tablica ciągów wag
     * @param generations Liczba dotychczasowych iteracji
     * @param duelsNumber Liczba pojedynków dla wszystkich osobników
     * @param minmaxDepth Głębokość przeszukiwania
     * @param selectionFactor Parametr ruletki w selekcji osobników
     * @param mutationChance Szansa na mutację
     * @param stopCondType Typ kryterium stopu
     * @param stopCondStamp Postęp kryterium stopu
     * @param threshold Limit kryterium stopu
     */
    public static void savePopulation (String filename, short[][] population, int generations, int duelsNumber, int minmaxDepth, int selectionFactor,
                                       double mutationChance, StopCond stopCondType, long stopCondStamp, long threshold) {
        int populationLength = population.length;
        assert populationLength > 0;
        int heuristicSize = population[0].length;
        assert heuristicSize == Heuristic.numberOfParams;
        try (FileWriter writer = new FileWriter(filename)) {
            String header = heuristicSize + " " + populationLength + " " + generations + " " + duelsNumber + " " + minmaxDepth + " " + selectionFactor +
                    " " + mutationChance + " " + StopCondConverter.enumToInt(stopCondType) + " " + stopCondStamp + " " + threshold;
            header = header + '\n';
            writer.write(header);
            for (int i = 0; i < populationLength; ++i) {
                for (int j = 0; j < heuristicSize; ++j)
                    writer.write(population[i][j] + " ");
                writer.write("\n");
            }
            writer.write("\n");
        } catch (IOException e) {
            System.err.println("ERROR: Zapis populacji do pliku nie powiódł się.");
            e.printStackTrace();
        }
    }

    /**
     * Zapisuje postęp sesji algorytmu genetycznego (populację osobników) w pliku o wygenerowanej nazwie.
     * Nazwa pliku oparta jest o obecną datę i czas. Plik zapisywany jest w katalogu heuristics/populations.
     * @param population Tablica ciągów wag
     * @param generations Liczba dotychczasowych iteracji
     * @param duelsNumber Liczba pojedynków dla wszystkich osobników
     * @param minmaxDepth Głębokość przeszukiwania
     * @param selectionFactor Parametr ruletki w selekcji osobników
     * @param mutationChance Szansa na mutację
     * @param stopCondType Typ kryterium stopu
     * @param stopCondStamp Postęp kryterium stopu
     * @param threshold Limit kryterium stopu
     * @return Nazwa pliku w którym zapisany został postęp
     */
    public static String savePopulation (short[][] population, int generations, int duelsNumber, int minmaxDepth, int selectionFactor,
                                         double mutationChance, StopCond stopCondType, long stopCondStamp, long threshold) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String filename = populationsDirectory + "/" + dtf.format(LocalDateTime.now()) + ".txt";
        savePopulation(filename, population, generations, duelsNumber, minmaxDepth, selectionFactor, mutationChance, stopCondType, stopCondStamp, threshold);
        return filename;
    }

    /**
     * Zapisuje pojedynczy ciąg wag do pliku o podanej nazwie.
     * @param values Ciąg wag do zapisu
     * @param filename Nazwa pliku docelowego
     */
    public static void saveWeights(short[] values, String filename) {
        int heuristicSize = values.length;
        assert heuristicSize == Heuristic.numberOfParams;
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(heuristicSize + " " + 1 + '\n');
            for (int i = 0; i < heuristicSize; ++i)
                writer.write(values[i] + " ");
            writer.write("\n");
        } catch (IOException e) {
            System.err.println("ERROR: Zapis heurystyki do pliku nie powiódł się.");
            e.printStackTrace();
        }
    }

    /**
     * Zapisuje pojedynczy ciąg wag do pliku o wygenerowanej nazwie.
     * Nazwa pliku oparta jest o obecną datę i czas. Plik zapisywany jest w katalogu heuristics/single.
     * @param values Ciąg wag do zapisu
     * @return Nazwa pliku z zapisanym ciągiem wag
     */
    public static String saveWeights(short[] values) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String filename = singleHeuristicsDirectory + "/" + dtf.format(LocalDateTime.now()) + ".txt";
        saveWeights(values, filename);
        return filename;
    }

    /**
     * Zapisuje ciąg wag obiektu heurystyki do pliku o podanej nazwie.
     * @param h Obiekt heurystyki z ciągiem wag do zapisania
     * @param filename Nazwa docelowego pliku
     */
    public static void saveWeights(Heuristic h, String filename) {
        saveWeights(h.paramWeights, filename);
    }

    /**
     * Zapisuje ciąg wag obiektu heurystyki do pliku o wygenerowanej nazwie.
     * Nazwa pliku oparta jest o obecną datę i czas. Plik zapisywany jest w katalogu heuristics/single.
     * @param h Obiekt heurystyki z ciągiem wag do zapisania
     * @return Nazwa pliku z zapisanym ciągiem wag heurystyki
     */
    public static String saveWeights(Heuristic h) {
        return saveWeights(h.paramWeights);
    }

    /**
     * Specjalna metoda służąca do zapisu wyniku sesji algorytmu genetycznego.
     * Metoda zapisze ciąg wag w katalogu heuristics/output.
     * @param genotype Ciąg wag będący wynikiem sesji algorytmu genetycznego
     * @return Nazwa pliku z zapisanym wynikiem
     */
    public static String saveGeneticOutputHeuristic(short[] genotype) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String filename = gaOutputDirectory + "/" + dtf.format(LocalDateTime.now()) + ".txt";
        saveWeights(genotype, filename);
        return filename;
    }

    /**
     * Wczytuje zapisaną populację z pliku.
     * @param filename Nazwa pliku do wczytania
     * @return Populacja w postaci tablicy ciągów wag
     */
    public static short[][] loadPopulation (String filename) {
        short[][] population = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String[] numbers = reader.readLine().split(" ");
            int heuristicSize = Integer.parseInt(numbers[0]), populationLength = Integer.parseInt(numbers[1]);
            assert heuristicSize == Heuristic.numberOfParams;
            assert populationLength > 0;
            population = new short[populationLength][heuristicSize];
            for (int i = 0; i < populationLength; ++i) {
                numbers = reader.readLine().split(" ");
                for (int j = 0; j < heuristicSize; ++j) {
                    population[i][j] = Short.parseShort(numbers[j]);
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR: Odczyt populacji nie powiódł się.");
            e.printStackTrace();
        }
        return population;
    }

    /**
     * Wczytuje populację z ostatnio zapisanego pliku populacji w katalogu heuristics/populations.
     * @return Populacja w postaci tablicy ciągów wag
     */
    public static short[][] loadPopulation () {
        File dir = new File(populationsDirectory);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".txt"));
        assert files != null;
        return loadPopulation(populationsDirectory + "/" + files[files.length - 1].getName());
    }

    /**
     * Przywraca zapisany postęp sesji algorytmu genetycznego z wybranego pliku.
     * @param filename Plik do wczytania
     * @return Sesja algorytmu genetycznego, gotowa do wznowienia
     */
    public static Genetic reloadGeneticAlgorithm (String filename) {
        short[][] population = null;
        int heuristicSize, populationLength, generation = 0, selectionFactor = 0, duelsNumber = 0, minmaxDepth = 1;
        long stopCondStamp = 0, stopCondThreshold = 0;
        double mutationChance = 0.0;
        StopCond stopCondType = StopCond.GENERATIONS;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String[] numbers = reader.readLine().split(" ");
            heuristicSize = Integer.parseInt(numbers[0]);
            assert heuristicSize == Heuristic.numberOfParams;
            populationLength = Integer.parseInt(numbers[1]);
            assert populationLength > 0;
            generation = Integer.parseInt(numbers[2]);
            duelsNumber = Integer.parseInt(numbers[3]);
            minmaxDepth = Integer.parseInt(numbers[4]);
            selectionFactor = Integer.parseInt(numbers[5]);
            mutationChance = Double.parseDouble(numbers[6]);
            stopCondType = StopCondConverter.intToEnum(Integer.parseInt(numbers[7]));
            stopCondStamp = Long.parseLong(numbers[8]);
            stopCondThreshold = Long.parseLong(numbers[9]);
            population = new short[populationLength][heuristicSize];
            for (int i = 0; i < populationLength; ++i) {
                numbers = reader.readLine().split(" ");
                for (int j = 0; j < heuristicSize; ++j) {
                    population[i][j] = Short.parseShort(numbers[j]);
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR: Odczyt algorytmu genetycznego nie powiódł się.");
            e.printStackTrace();
        }
        assert population != null;
        return new Genetic(population, generation, duelsNumber, minmaxDepth, selectionFactor, mutationChance, stopCondType, stopCondStamp, stopCondThreshold);
    }

    /**
     * Przywraca zapisany postęp sesji algorytmu genetycznego z ostatnio zapisanego pliku w katalogu heuristics/populations.
     * @return Sesja algorytmu genetycznego, gotowa do wznowienia
     */
    public static Genetic reloadGeneticAlgorithm () {
        File dir = new File(populationsDirectory);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".txt"));
        assert files != null;
        return reloadGeneticAlgorithm(populationsDirectory + "/" + files[files.length - 1].getName());
    }

    /**
     * Wczytuje ciąg wag z wybranego pliku.
     * @param filename Plik do wczytania
     * @return Wczytany ciąg wag
     */
    public static short[] loadWeights (String filename) {
        short[] values = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            String[] numbers = line.split(" ");
            int length = Integer.parseInt(numbers[0]);
            assert length == Heuristic.numberOfParams;
            line = reader.readLine();
            numbers = line.split(" ");
            values = new short[length];
            for (int i = 0; i < length; ++i)
                values[i] = Short.parseShort(numbers[i]);
        } catch (IOException e) {
            System.err.println("ERROR: Odczyt populacji nie powiódł się.");
            e.printStackTrace();
        }
        return values;
    }

    /**
     * Wczytuje ciąg wag z ostatnio zapisanego pliku w katalogu heuristics/single.
     * @return Wczytany ciąg wag
     */
    public static short[] loadWeights () {
        File[] files = new File(singleHeuristicsDirectory).listFiles((dir1, name) -> name.endsWith(".txt"));
        assert files != null;
        return loadWeights(singleHeuristicsDirectory + "/" + files[files.length - 1].getName());
    }

    /**
     * Załadowuje zapisany ciąg wag z wybranego pliku do obiektu heurystyki.
     * @param filename Nazwa pliku do wczytania
     * @return Obiekt heurystyki ze wczytanym ciągiem wag
     */
    public static Heuristic loadAndRecreate (String filename) {
        short[] values = loadWeights(filename);
        return new Heuristic(values);
    }

    /**
     * Załadowuje zapisany ciąg wag z ostatnio zapisanego pliku w heuristics/single do obiektu heurystyki.
     * @return Obiekt heurystyki ze wczytanym ciągiem wag
     */
    public static Heuristic loadAndRecreate () {
        File[] files = new File(singleHeuristicsDirectory).listFiles((dir1, name) -> name.endsWith(".txt"));
        assert files != null;
        return loadAndRecreate(singleHeuristicsDirectory + "/" + files[files.length - 1].getName());
    }

//    public static void loadIntoHeuristic (Heuristic h, String filename) {
//        //
//    }

//    public static void loadIntoHeuristic (Heuristic h) {
//        File[] files = new File(singleHeuristicsDirectory).listFiles((dir1, name) -> name.endsWith(".txt"));
//        assert files != null;
//        loadIntoHeuristic(h, singleHeuristicsDirectory + "/" + files[files.length - 1].getName());
//    }

    /**
     * Metoda czyszcząca podkatalog heuristics/single.
     */
    public static void removeSingleHeuristics () {
        File dir = new File(singleHeuristicsDirectory);
        for (File file: Objects.requireNonNull(dir.listFiles()))
            if (!file.isDirectory())
                if (!file.delete())
                    System.err.println("ERROR: Błąd podczas usuwania plików.");
    }

    /**
     * Metoda czyszcząca populacje w heuristics/populations oprócz ostatnio utworzonego pliku.
     */
    public static void removePopulationsExceptOne () {
        File dir = new File(populationsDirectory);
        File[] files = Objects.requireNonNull(dir.listFiles());
        for (int i = 0; i < files.length - 1; ++i)
            if (!files[i].isDirectory())
                if (!files[i].delete())
                    System.err.println("ERROR: Błąd podczas usuwania plików populacji.");
        // DEBUG
        files = Objects.requireNonNull(dir.listFiles());
        System.out.println("File: " + files[0].getName() + " | " + files.length);
    }

    /**
     * Metoda czyszcząca populacje w heuristics/populations oprócz wybranego pliku.
     * @param filename Plik którego metoda ma nie usuwać
     */
    public static void removePopulationsExceptOne (String filename) {
        String name = filename.substring(filename.lastIndexOf("/") + 1);
        File dir = new File(populationsDirectory);
        for (File file: Objects.requireNonNull(dir.listFiles()))
            if (!file.getName().equals(name) && !file.isDirectory())
                if (!file.delete())
                    System.err.println("ERROR: Błąd podczas usuwania plików populacji poza jednym wybranym.");
    }

}
