import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FileHandler {

    protected static final String singleHeuristicsDirectory = "heuristics/single";
    protected static final String populationsDirectory = "heuristics/populations";


    //Poniżej znajdują się metody do zapisu i odczytu wag parametrów heurystyki.

    public static void savePopulation (short[][] population, String filename) {
        int size = population.length;
        assert size > 0;
        int length = population[0].length;
        assert length == Heuristic.numberOfParams;
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(length + " " + size + '\n');
            for (int i = 0; i < size; ++i) {
                for (int j = 0; j < length; ++j)
                    writer.write(population[i][j] + " ");
                writer.write("\n");
            }
            writer.write("\n");
        } catch (IOException e) {
            System.err.println("ERROR: Zapis populacji do pliku nie powiódł się.");
            e.printStackTrace();
        }
    }

    public static void savePopulation (short[][] population) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String filename = populationsDirectory + "/" + dtf.format(LocalDateTime.now()) + ".txt";
        savePopulation(population, filename);
    }

    public static void saveWeights(short[] values, String filename) {
        int length = values.length;
        assert length == Heuristic.numberOfParams;
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(length + " " + 1 + '\n');
            for (int i = 0; i < length; ++i)
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

    public void saveWeights(Heuristic h, String filename) {
        saveWeights(h.paramWeights, filename);
    }

    public void saveWeights(Heuristic h) {
        saveWeights(h.paramWeights);
    }

    public static short[][] loadPopulation (String filename) {
        short[][] population = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String[] numbers = reader.readLine().split(" ");
            int length = Integer.parseInt(numbers[0]), size = Integer.parseInt(numbers[1]);
            assert length == Heuristic.numberOfParams;
            assert size > 0;
            population = new short[size][length];
            for (int i = 0; i < size; ++i) {
                numbers = reader.readLine().split(" ");
                for (int j = 0; j < length; ++j) {
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
        return loadPopulation(files[files.length - 1].getName());
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

}
