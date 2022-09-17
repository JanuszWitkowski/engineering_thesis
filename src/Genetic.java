import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Genetic {
    private abstract static class StopCondition {
        protected final long threshold;
        protected StopCondition (long threshold) {
            this.threshold = threshold;
        }
        abstract boolean conditionNotMet ();
        abstract long getStamp ();
        abstract void setStamp (long stamp);
    }

    private static class StopConditionTime extends StopCondition {
        protected Instant start = Instant.now();
        protected long offset = 0;
        protected StopConditionTime (long threshold) {
            super(threshold);
        }

        @Override
        boolean conditionNotMet() {
            Instant finish = Instant.now();
            return Duration.between(start, finish).toSeconds() + offset <= threshold;
        }

        @Override
        long getStamp() {
            Instant finish = Instant.now();
            return Duration.between(start, finish).toSeconds() + offset;
        }

        @Override
        void setStamp(long stamp) {
            offset = stamp;
            start = Instant.now();
        }
    }

    private static class StopConditionGenerations extends StopCondition {
        long generations = 0;
        protected StopConditionGenerations (long threshold) {
            super(threshold);
        }

        @Override
        boolean conditionNotMet() {
            ++generations;
//            System.out.println("condition: " + generations + " vs " + threshold);
            return generations < threshold;
        }

        @Override
        long getStamp() {
            return 0;
        }

        @Override
        void setStamp(long stamp) {
            generations = stamp;
        }
    }

    private static final int genotypeSize = HParam.values().length;

    private final PlayerComputer player1, player2;
    private final GameHandler game1, game2;

    private int populationSize;
    private int parentPopulationSize;
    private double mutationChance;
    private int selectionFactor = 20;

    private short[] bestSoFar;

    public Genetic () {
        this(100, 50, 0.1, 1);
    }

    public Genetic (int populationSize, int selectionFactor, double mutationChance, int playerDepth) {
        while (populationSize % 4 != 0) ++populationSize;
        this.populationSize = populationSize;
        this.parentPopulationSize = populationSize / 2;
        this.selectionFactor = selectionFactor;
        this.mutationChance = mutationChance;
        this.player1 = new PlayerComputer(new Heuristic((short) 0), playerDepth);
        this.player2 = new PlayerComputer(new Heuristic((short) 0), playerDepth);
        this.game1 = new GameHandler(player1, player2);
        this.game2 = new GameHandler(player2, player1);
    }

    // Implementing Fisher–Yates shuffle
    private void shuffleArray(short[][] ar) {
        for (int i = ar.length - 1; i > 0; i--) {
            int index = RNG.randomInt(i + 1);
            // Simple swap
            short[] tmp = ar[index];
            ar[index] = ar[i];
            ar[i] = tmp;
        }
    }

    private boolean areTwoGenotypesTheSame (short[] g1, short[] g2) {
        for (int i = 0; i < genotypeSize; ++i) {
            if (g1[i] == g2[i]) return true;
        }
        return false;
    }

    private boolean isGenotypeNotInPopulation (short[] genotype, short[][] population, int startIndex, int finishIndex) {
        for (int i = startIndex; i < finishIndex; ++i) {
            if (areTwoGenotypesTheSame(genotype, population[i])) return false;
        }
        return true;
    }

    private short[][] selection (short[][] population) {
        int popSize = population.length;
        int[][] results = new int[popSize][4];
        // 0 - index; 1 - wygrane w ataku; 2 - wygrane w obronie; 3 - ogólny wynik.
        for (int p = 0; p < popSize; ++p) {
            results[p][0] = p;
            results[p][1] = 0;
            results[p][2] = 0;
            results[p][3] = 0;
        }
        // Ewaluacja osobników poprzez przeprowadzenie pojedynków każdy z każdym po 2 gry.
        for (int p1 = 0; p1 < popSize; ++p1) {
            for (int p2 = p1 + 1; p2 < popSize; ++p2) {
                player1.changeHeuristicWeights(population[p1]);
                player2.changeHeuristicWeights(population[p2]);
                game1.resetBoard();
                game2.resetBoard();
                int result1 = game1.quickGame();
                int result2 = game2.quickGame();
                results[p1][1] += result1;
                results[p2][2] -= result1;
                results[p1][2] -= result2;
                results[p2][1] += result2;
            }
        }
        for (int p = 0; p < popSize; ++p) {
            results[p][3] = results[p][1] + results[p][2];
        }

        /* REGUŁY SELEKCJI
        Można zmienić w każdej chwili. */
        // Sortowanie insertion-sort; TODO: Zmienić może na quick-sort?
        for (int i = 1; i < popSize; ++i) {
            for (int j = i; j > 0; --j) {
                if (results[j-1][3] < results[j][3]) {
                    int[] tmp = results[j];
                    results[j] = results[j-1];
                    results[j-1] = tmp;
                } else break;
            }
        }

        // TODO: Funckja debugująca. Należy jej się pozbyć w finalnym projeckie.
        System.out.println("----SORTED----");
        for (int i = 0; i < popSize; ++i) {
            System.out.print(results[i][0] + " ");
        }
        System.out.println();
        for (int i = 0; i < popSize; ++i) {
            System.out.print(results[i][3] + " ");
        }
        System.out.println("\n--------------");

        // Wybierz najlepszego.
        bestSoFar = population[results[0][0]];

        // Ruletka, czyli loteria osobników które przechodzą dalej.
        for (int i = 0; i < popSize; ++i) {
            results[i][3] += RNG.randomInt(selectionFactor);
        }

        for (int i = 1; i < popSize; ++i) {
            for (int j = i; j > 0; --j) {
                if (results[j-1][3] < results[j][3]) {
                    int[] tmp = results[j];
                    results[j] = results[j-1];
                    results[j-1] = tmp;
                } else break;
            }
        }

        // TODO: Funckja debugująca. Należy jej się pozbyć w finalnym projeckie.
        System.out.println("----SORTED POST ROULETTE----");
        for (int i = 0; i < popSize; ++i) {
            System.out.print(results[i][0] + " ");
        }
        System.out.println();
        for (int i = 0; i < popSize; ++i) {
            System.out.print(results[i][3] + " ");
        }
        System.out.println("\n--------------");

        // W miarę możliwości dobieraj osobniki różne.
        boolean[] candidatesFree = new boolean[popSize];
        for (int i = 0; i < popSize; ++i) candidatesFree[i] = true;
        short[][] parents = new short[parentPopulationSize][genotypeSize];
        int numberOfParents = 0;
        for (int i = 0; i < popSize && numberOfParents < parentPopulationSize; ++i) {
            short[] candidate = population[results[i][0]];
            if (isGenotypeNotInPopulation(candidate, parents, 0, numberOfParents)) {
                parents[numberOfParents] = candidate;
                ++numberOfParents;
                candidatesFree[i] = false;
            }
        }
        // Dokończ populację rodziców duplikatami, aby nie było pustych miejsc.
        int index = 0;
        while (numberOfParents < parentPopulationSize) {
            short[] candidate = population[results[index][0]];
            if (candidatesFree[index]) {
                parents[numberOfParents] = candidate;
                ++numberOfParents;
                candidatesFree[index] = false;
            }
            ++index;
        }

        return parents;
    }

    private short[][] selectionDebug (short[][] population) {
        int popSize = population.length;
        int[][] results = new int[popSize][2];
        for (int i = 0; i < popSize; ++i) {
            results[i][0] = i;
            player1.changeHeuristicWeights(population[i]);
            results[i][1] = player1.getEval();
        }
        for (int i = 1; i < popSize; ++i) {
            for (int j = i; j > 0; --j) {
                if (results[j-1][1] < results[j][1]) {
                    int[] tmp = results[j];
                    results[j] = results[j-1];
                    results[j-1] = tmp;
                } else break;
            }
        }
        bestSoFar = population[results[0][0]];

        System.out.println("----SORTED----");
        for (int i = 0; i < popSize; ++i) {
            System.out.print(results[i][0] + " ");
        }
        System.out.println();
        for (int i = 0; i < popSize; ++i) {
            System.out.print(results[i][1] + " ");
        }
        System.out.println("\n--------------");

        for (int i = 0; i < popSize; ++i) {
            results[i][1] += RNG.randomInt(selectionFactor);
        }

        for (int i = 1; i < popSize; ++i) {
            for (int j = i; j > 0; --j) {
                if (results[j-1][1] < results[j][1]) {
                    int[] tmp = results[j];
                    results[j] = results[j-1];
                    results[j-1] = tmp;
                } else break;
            }
        }

        System.out.println("----SORTED POST ROULETTE----");
        for (int i = 0; i < popSize; ++i) {
            System.out.print(results[i][0] + " ");
        }
        System.out.println();
        for (int i = 0; i < popSize; ++i) {
            System.out.print(results[i][1] + " ");
        }
        System.out.println("\n--------------");

        boolean[] candidatesFree = new boolean[popSize];
        for (int i = 0; i < popSize; ++i) candidatesFree[i] = true;
        short[][] parents = new short[parentPopulationSize][genotypeSize];
        int numberOfParents = 0;
        for (int i = 0; i < popSize && numberOfParents < parentPopulationSize; ++i) {
            short[] candidate = population[results[i][0]];
            if (isGenotypeNotInPopulation(candidate, parents, 0, numberOfParents)) {
                parents[numberOfParents] = candidate;
                ++numberOfParents;
                candidatesFree[i] = false;
            }
        }
        int index = 0;
        while (numberOfParents < parentPopulationSize) {
            short[] candidate = population[results[index][0]];
            if (candidatesFree[index]) {
                parents[numberOfParents] = candidate;
                ++numberOfParents;
                candidatesFree[index] = false;
            }
            ++index;
        }

        return parents;
    }

    private short[] crossover (short[] parentA, short[] parentB) {  // TODO: Może losować punkt Xoveru?
        short[] child = new short[genotypeSize];
        int crossoverPoint = RNG.randomInt(genotypeSize - 2) + 1;
        if (crossoverPoint >= 0)
            System.arraycopy(parentA, 0, child, 0, crossoverPoint);
        if (genotypeSize - crossoverPoint >= 0)
            System.arraycopy(parentB, crossoverPoint, child, crossoverPoint, genotypeSize - crossoverPoint);
        return child;
    }

    private void mutation (short[] genotype) {
        int index = RNG.randomInt(genotypeSize);
        double modifier = RNG.randomBool() ? 1.0 : -1.0;
        genotype[index] = RNG.randomShort();
    }

    public short[][] createStartingPopulation () {
        short[][] population = new short[populationSize][genotypeSize];
        for (int i = 0; i < populationSize; ++i)
            for (int k = 0; k < genotypeSize; ++k)
                population[i][k] = RNG.randomShort();
        return population;
    }

    private StopCondition buildStopCondition (StopConds type, long threshold) {
        StopCondition s;
        switch (type) {
            case TIME -> s = new StopConditionTime(threshold);
            case GENERATIONS -> s = new StopConditionGenerations(threshold);
            default -> s = new StopConditionTime(threshold);
        }
        return s;
    }

    public short[] GA (short[][] population, StopConds stopType, long threshold) {
        StopCondition stop = buildStopCondition(stopType, threshold);
        int gen = 0;    // Liczba iteracji/generacji.

        while (stop.conditionNotMet()) {    // Dopóki nie osiągniemy kryterium stopu:
            System.out.println("Generation " + ++gen);
            // Selekcja populacji rodziców (każdy gra z każdym, patrzymy kto ile wygrywał jako biały/czarny).
            short[][] parents = selection(population);
//            short[][] parents = selectionDebug(population); // TODO: Zmienić po debugu
            // Krzyżowanie (wyznaczamy pary rodziców, każda para rodziców ma dwoje dzieci).
            shuffleArray(parents);
            short[][] children = new short[parentPopulationSize][genotypeSize];
            for (int i = 0; i < parentPopulationSize; i += 2) {
                children[i] = crossover(parents[i], parents[i + 1]);
                children[i + 1] = crossover(parents[i + 1], parents[i]);
            }
            // Mutacje (szansa na mutację, zmieniamy jeden parametr o jakiś procent).
            for (short[] child : children)
                if (RNG.randomDoubleFromZeroToOne() > mutationChance)
                    mutation(child);
            // Połącz dzieci i rodziców w następną populację.
            for (int i = 0; i < parentPopulationSize; ++i) {
                population[2 * i] = parents[i];
                population[2 * i + 1] = children[i];
            }
            // Zapisz populację i usuń poprzednią.
            String newestPopulationName = FileHandler.savePopulation(population);
            FileHandler.removePopulationsExceptOne(newestPopulationName);
        }
        // Przeprowadź ponowną selekcję i wyznacz najlepszego.
        short[] best = selection(population)[0];  // TODO: Zmienić po debugu
//        short[] best = selectionDebug(population)[0];
        // Zapisz do pliku w heuristics/output/ najlepszego i go zwróć.
        String bestFilename = FileHandler.saveGeneticOutput(bestSoFar);
        System.out.println("Najlepiej przystosowany osobnik został zapisany jako " + bestFilename);
        return bestSoFar;
    }

}
