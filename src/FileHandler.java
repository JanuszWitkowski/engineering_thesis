import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FileHandler {

    private static final String globalDirectory = "heuristics";
    private static final String singleHeuristicsDirectory = globalDirectory + "/single";
    private static final String populationsDirectory = globalDirectory + "/populations";
    private static final String archiveDirectory = globalDirectory + "/archive";
    private static final String gaOutputDirectory = globalDirectory + "/output";

    public static String[] getAllNecessaryDirs () {
        return new String[]{globalDirectory, singleHeuristicsDirectory,
        populationsDirectory, archiveDirectory, gaOutputDirectory};
    }

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

    public static void savePopulation (String filename, short[][] population, int generations, int duelsNumber, int selectionFactor,
                                       double mutationChance, StopCond stopCondType, long stopCondStamp, long threshold) {
        int populationLength = population.length;
        assert populationLength > 0;
        int heuristicSize = population[0].length;
        assert heuristicSize == Heuristic.numberOfParams;
        try (FileWriter writer = new FileWriter(filename)) {
            String header = heuristicSize + " " + populationLength + " " + generations + " " + duelsNumber + " " + selectionFactor +
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

    public static String savePopulation (short[][] population, int generations, int duelsNumber, int selectionFactor,
                                         double mutationChance, StopCond stopCondType, long stopCondStamp, long threshold) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String filename = populationsDirectory + "/" + dtf.format(LocalDateTime.now()) + ".txt";
        savePopulation(filename, population, generations, duelsNumber, selectionFactor, mutationChance, stopCondType, stopCondStamp, threshold);
        return filename;
    }

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

    public static void saveWeights(short[] values) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String filename = singleHeuristicsDirectory + "/" + dtf.format(LocalDateTime.now()) + ".txt";
        saveWeights(values, filename);
    }

    public static void saveWeights(Heuristic h, String filename) {
        saveWeights(h.paramWeights, filename);
    }

    public static void saveWeights(Heuristic h) {
        saveWeights(h.paramWeights);
    }

    public static String saveGeneticOutput (short[] genotype) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String filename = gaOutputDirectory + "/" + dtf.format(LocalDateTime.now()) + ".txt";
        saveWeights(genotype, filename);
        return filename;
    }

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

    public static short[][] loadPopulation () {
        File dir = new File(populationsDirectory);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".txt"));
        assert files != null;
        return loadPopulation(populationsDirectory + "/" + files[files.length - 1].getName());
    }

    public static Genetic reloadGeneticAlgorithm (String filename) {
        short[][] population = null;
        int heuristicSize, populationLength, generation = 0, selectionFactor = 0, duelsNumber = 0;
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
            selectionFactor = Integer.parseInt(numbers[4]);
            mutationChance = Double.parseDouble(numbers[5]);
            stopCondType = StopCondConverter.intToEnum(Integer.parseInt(numbers[6]));
            stopCondStamp = Long.parseLong(numbers[7]);
            stopCondThreshold = Long.parseLong(numbers[8]);
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
        return new Genetic(population, generation, duelsNumber, selectionFactor, mutationChance, stopCondType, stopCondStamp, stopCondThreshold);
    }

    public static Genetic reloadGeneticAlgorithm () {
        File dir = new File(populationsDirectory);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".txt"));
        assert files != null;
        return reloadGeneticAlgorithm(populationsDirectory + "/" + files[files.length - 1].getName());
    }

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

    public static short[] loadWeights () {
        File[] files = new File(singleHeuristicsDirectory).listFiles((dir1, name) -> name.endsWith(".txt"));
        assert files != null;
        return loadWeights(singleHeuristicsDirectory + "/" + files[files.length - 1].getName());
    }

    public static Heuristic loadAndRecreate (String filename) {
        short[] values = loadWeights(filename);
        return new Heuristic(values);
    }

    public static Heuristic loadAndRecreate () {
        File[] files = new File(singleHeuristicsDirectory).listFiles((dir1, name) -> name.endsWith(".txt"));
        assert files != null;
        return loadAndRecreate(singleHeuristicsDirectory + "/" + files[files.length - 1].getName());
    }

    public static void loadIntoHeuristic (Heuristic h, String filename) {
        //
    }

    public static void loadIntoHeuristic (Heuristic h) {
        File[] files = new File(singleHeuristicsDirectory).listFiles((dir1, name) -> name.endsWith(".txt"));
        assert files != null;
        loadIntoHeuristic(h, singleHeuristicsDirectory + "/" + files[files.length - 1].getName());
    }

    public static void removeSingleHeuristics () {
        File dir = new File(singleHeuristicsDirectory);
        for (File file: Objects.requireNonNull(dir.listFiles()))
            if (!file.isDirectory())
                if (!file.delete())
                    System.err.println("ERROR: Błąd podczas usuwania plików.");
    }

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

    public static void removePopulationsExceptOne (String filename) {
        String name = filename.substring(filename.lastIndexOf("/") + 1);
        File dir = new File(populationsDirectory);
        for (File file: Objects.requireNonNull(dir.listFiles()))
            if (!file.getName().equals(name) && !file.isDirectory())
                if (!file.delete())
                    System.err.println("ERROR: Błąd podczas usuwania plików populacji poza jednym wybranym.");
    }

}
