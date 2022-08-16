import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Genetic {
    private static final int genotypeSize = HParam.values().length;
    private static final int crossoverPoint = genotypeSize / 2;

//    private final Random rng = new Random();
    private final Random rng = ThreadLocalRandom.current();

    private int populationSize;
    private int parentPopulationSize;
    private double mutationChance;
    private double mutationPercentage = 0.1;

    private short[] globalBest;

    public Genetic () {
        this(100, 0.1, 0.1);
    }

    public Genetic (int populationSize, double mutationChance, double mutationPercentage) {
        while (populationSize % 4 != 0) ++populationSize;
        this.populationSize = populationSize;
        this.parentPopulationSize = populationSize / 2;
        this.mutationChance = mutationChance;
        this.mutationPercentage = mutationPercentage;
    }

    public short randomShort () {
        return (short)(rng.nextInt(Short.MAX_VALUE - Short.MIN_VALUE + 1) + Short.MIN_VALUE);
    }

    // Implementing Fisher–Yates shuffle
    private void shuffleArray(short[][] ar) {
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rng.nextInt(i + 1);
            // Simple swap
            short[] tmp = ar[index];
            ar[index] = ar[i];
            ar[i] = tmp;
        }
    }

    private short[][] selection (short[][] population) {
        return null;
    }

    private short[] crossover (short[] parentA, short[] parentB) {  // TODO: Może losować punkt Xoveru?
        short[] child = new short[genotypeSize];
        if (crossoverPoint >= 0)
            System.arraycopy(parentA, 0, child, 0, crossoverPoint);
        if (genotypeSize - crossoverPoint >= 0)
            System.arraycopy(parentB, crossoverPoint, child, crossoverPoint, genotypeSize - crossoverPoint);
        return child;
    }

    private void mutation (short[] genotype) {
        int index = rng.nextInt(genotypeSize);
        double modifier = rng.nextBoolean() ? 1.0 : -1.0;
        genotype[index] = (short) (genotype[index] + (short)(modifier * mutationPercentage * genotype[index]));
    }

    public short[][] createStartingPopulation () {
        short[][] population = new short[populationSize][genotypeSize];
        for (int i = 0; i < populationSize; ++i)
            for (int k = 0; k < genotypeSize; ++k)
                population[i][k] = randomShort();
        return population;
    }

    public short[] GA (short[][] population) {
        // Dopóki nie osiągniemy kryterium stopu:
        while (true) {
            // Selekcja populacji rodziców (każdy gra z każdym, patrzymy kto ile wygrywał jako biały/czarny).
            short[][] parents = selection(population);
            // Krzyżowanie (wyznaczamy pary rodziców, każda para rodziców ma dwoje dzieci).
            assert parents != null;
            shuffleArray(parents);
            short[][] children = new short[parentPopulationSize][genotypeSize];
            for (int i = 0; i < parentPopulationSize; i += 2) {
                children[i] = crossover(parents[i], parents[i + 1]);
                children[i + 1] = crossover(parents[i + 1], parents[i]);
            }
            // Mutacje (szansa na mutację, zmieniamy jeden parametr o jakiś procent).
            for (short[] child : children)
                if (rng.nextDouble() > mutationChance)
                    mutation(child);
            // Połącz dzieci i rodziców w następną populację.
            for (int i = 0; i < parentPopulationSize; ++i) {
                population[2 * i] = parents[i];
                population[2 * i + 1] = children[i];
            }
            // Zapisz populację i usuń poprzednią.
            FileHandler.savePopulation(population);
            FileHandler.removePopulationsExceptOne();
            break;  // TODO: Remove this and implement proper stop condition.
        }
        // Przeprowadź ponowną selekcję i wyznacz najlepszego.
        selection(population);
        // Zapisz do pliku w heuristics/output/ najlepszego i go zwróć.
        FileHandler.saveGeneticOutput(globalBest);
        return globalBest;
        // (V2) Pętla to: Krzyżowanie, Mutacja, Selekcja. Dzięki temu nie powtarzamy selekcji po wyjściu z pętli.
    }

}
