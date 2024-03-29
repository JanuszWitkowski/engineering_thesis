import java.time.Duration;
import java.time.Instant;

/**
 * Klasa odpowiedzialna za reprezentację sesji algorytmu genetycznego.
 * Przechowuje wszystkie informacje na temat sesji, manipuluje plikami z pomocą FileHandler'a,
 * oraz przeprowadza turnieje z pomocą GameHandler'a.
 */
public class Genetic {
    private abstract static class StopCondition {
        protected final long threshold;
        protected StopCondition (long threshold, long stamp) {
            this.threshold = threshold;
//            setStamp(stamp);
        }
        protected long getThreshold () {
            return this.threshold;
        }
        abstract boolean conditionNotMet ();
        abstract long getStamp ();
        abstract void setStamp (long stamp);
    }

    private static class StopConditionTime extends StopCondition {
        protected Instant start = Instant.now();
        protected long offset = 0;
        protected StopConditionTime (long threshold, long stamp) {
            super(threshold, stamp);
            setStamp(stamp);
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
        protected StopConditionGenerations (long threshold, long stamp) {
            super(threshold, stamp);
            setStamp(stamp);
        }

        @Override
        boolean conditionNotMet() {
            ++generations;
//            System.out.println("condition: " + generations + " vs " + threshold);
            return generations < threshold;
        }

        @Override
        long getStamp() {
            return generations;
        }

        @Override
        void setStamp(long stamp) {
            generations = stamp;
        }
    }

    private static final int genotypeSize = HParam.values().length;

    private final PlayerComputer player1, player2;
    private final GameHandler game1, game2;

    private final short[][] population;
    private final int populationSize;
    private final int parentPopulationSize;
    private int generation;
    private final int duelsNumber;
    private final int minmaxDepth;
    private final int selectionFactor;
    private final double mutationChance;
    private final StopCond stopCondType;
    private final long stopCondStamp;
    private final long stopCondThreshold;

    private short[] bestSoFar = null;

    /**
     * Tworzy sesję o standardowych argumentach.
     */
    public Genetic () {
        this(createStartingPopulation(100), 0, 0, 1, 20, 0.2,
                StopCond.GENERATIONS, 0, 1000);
    }

    /**
     * Wygodny i ogólny konstruktor w którym wystarczy sprecyzować 3 argumenty.
     * @param populationSize Rozmiar populacji
     * @param selectionFactor Parametr ruletki w selekcji
     * @param mutationChance Szansa na mutację
     */
    public Genetic (int populationSize, int selectionFactor, double mutationChance) {
        this(createStartingPopulation(populationSize), 0, 0, 1, selectionFactor, mutationChance, StopCond.GENERATIONS,
                0, 20);
    }

    /**
     * Konstruktor tworzący świeżą sesję.
     * @param populationSize Rozmiar populacji
     * @param duelsNumber Liczba pojedynków dla wszystkich osobników; 0 oznacza FFA
     * @param minmaxDepth Głębokość przeszukiwania przestrzeni stanów
     * @param selectionFactor Parametr ruletki w selekcji
     * @param mutationChance Szansa na mutację
     * @param stopCondTypeNumber Typ kryterium stopu
     * @param stopCondThreshold Limit kryterium stopu
     */
    public Genetic (int populationSize, int duelsNumber, int minmaxDepth, int selectionFactor, double mutationChance, int stopCondTypeNumber, long stopCondThreshold) {
        this(createStartingPopulation(populationSize), 0, duelsNumber, minmaxDepth, selectionFactor, mutationChance,
                StopCondConverter.intToEnum(stopCondTypeNumber), 0, stopCondThreshold);
    }

    /**
     * Konstruktor służący do reaktywacji sesji.
     * Służy
     * @param population Odzyskana populacja w postaci tablicy ciągów wag
     * @param generation Numer iteracji sesji
     * @param duelsNumber Liczba pojedynków dla wszystkich osobników; 0 oznacza FFA
     * @param minmaxDepth Głębokość przeszukiwania przestrzeni stanów
     * @param selectionFactor Parametr ruletki w selekcji
     * @param mutationChance Szansa na mutację
     * @param stopCondType Typ kryterium stopu
     * @param stopCondStamp Postęp kryterium stopu
     * @param stopCondThreshold Limit kryterium stopu
     */
    public Genetic (short[][] population, int generation, int duelsNumber, int minmaxDepth, int selectionFactor, double mutationChance,
                    StopCond stopCondType, long stopCondStamp, long stopCondThreshold) {
        this.population = population;
        this.populationSize = population.length;
        assert this.populationSize % 4 == 0;
        this.parentPopulationSize = populationSize / 2;
        this.generation = generation;
        this.duelsNumber = duelsNumber;
        this.minmaxDepth = minmaxDepth;
        this.selectionFactor = selectionFactor;
        this.mutationChance = mutationChance;
        this.stopCondType = stopCondType;
        this.stopCondStamp = stopCondStamp;
        this.stopCondThreshold = stopCondThreshold;
        this.player1 = new PlayerComputer(new Heuristic((short) 0), minmaxDepth);
        this.player2 = new PlayerComputer(new Heuristic((short) 0), minmaxDepth);
        this.game1 = new GameHandler(player1, player2);
        this.game2 = new GameHandler(player2, player1);
    }

    /**
     * Fisher–Yates shuffle.
     * Wykorzystany do losowego wymieszania tablicy osobników.
     * @param ar Tablica ciągów wag
     */
    private void shuffleArray(short[][] ar) {
        for (int i = ar.length - 1; i > 0; i--) {
            int index = RNG.randomInt(i + 1);
            // Simple swap
            short[] tmp = ar[index];
            ar[index] = ar[i];
            ar[i] = tmp;
        }
    }

    /**
     * Porównuje ze sobą dwa ciągi wag.
     * @param g1 Pierwszy ciąg wag
     * @param g2 Drugi ciąg wag
     * @return True jeśli podane ciągi wag mają te same wartości dla tych samych parametrów; False wp.p.
     */
    private boolean areTwoGenotypesTheSame (short[] g1, short[] g2) {
        for (int i = 0; i < genotypeSize; ++i) {
            if (g1[i] != g2[i]) return false;
        }
        return true;
    }

    /**
     * Sprawdza, czy podany ciąg wag znajduje się już w populacji.
     * @param genotype Ciąg wag
     * @param population Tablica ciągów wag
     * @param startIndex Miejsce w populacji, od którego rozpoczyna się przeszukiwanie
     * @param finishIndex Miejsce w populacji, na którym kończy się przeszukiwanie
     * @return True jeśli dany ciąg nie znajduje się w populacji; False w przeciwnym razie.
     */
    private boolean isGenotypeNotInPopulation (short[] genotype, short[][] population, int startIndex, int finishIndex) {
        for (int i = startIndex; i < finishIndex; ++i) {
            if (areTwoGenotypesTheSame(genotype, population[i])) return false;
        }
        return true;
    }

    /**
     * Metoda selekcji. Najważniejsza funkcja determinująca zachowanie algorytmu.
     * Można to zachowanie zmienić, zmieniając metodę selekcji.
     * @param population Tablica osobników, z której należy wyłonić populację rodziców
     * @return Populacja rodziców
     */
    private short[][] selection (short[][] population) {
        int popSize = population.length;
        // 0 - index; 1 - wygrane w ataku; 2 - wygrane w obronie; 3 - ogólny wynik.
        int[][] results = playDuels(population, popSize);

        /* REGUŁY SELEKCJI
        Można zmienić w każdej chwili. */
        // Sortowanie insertion-sort
        for (int i = 1; i < popSize; ++i) {
            for (int j = i; j > 0; --j) {
                if (results[j-1][3] < results[j][3]) {
                    int[] tmp = results[j];
                    results[j] = results[j-1];
                    results[j-1] = tmp;
                } else break;
            }
        }

//        // Znajdź najlepszy wynik
//        for (int j = popSize - 1; j > 0; --j) {
//            if (results[j-1][3] < results[j][3]) {
//                int[] tmp = results[j];
//                results[j] = results[j-1];
//                results[j-1] = tmp;
//            }
//        }

        // Wybierz najlepszego.
        if (bestSoFar == null) bestSoFar = population[results[0][0]];
        else {
            short[] bestThisTime = population[results[0][0]];
            player1.changeHeuristicWeights(bestThisTime);
            player2.changeHeuristicWeights(bestSoFar);
            game1.resetBoard();
            game2.resetBoard();
            int result1 = game1.quickGame();
            int result2 = (-1) * game2.quickGame();
            if (result1 + result2 >= 0) bestSoFar = bestThisTime;
        }

        // Ruletka, czyli loteria osobników, które przechodzą dalej.
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

    /**
     * Metoda handlująca pojedynki w procesie selekcji.
     * @param population Tablica osobników
     * @param popSize Wielkość populacji
     * @return Dwuwymiarowa tablica zapisanych wyników pojedynków dla każdego osobnika w populacji
     */
    private int[][] playDuels (short[][] population, int popSize) {
        int[][] results = new int[popSize][4];
        for (int p = 0; p < popSize; ++p) {
            results[p][0] = p;
            results[p][1] = 0;
            results[p][2] = 0;
            results[p][3] = 0;
        }
        if (duelsNumber == 0) return getDuelsResults(results, population, popSize);
        return getDuelsResults(results, population, popSize, duelsNumber);
    }

    /**
     * Metoda przeprowadzająca pojedynki i edytująca tablicę wyników.
     * @param population Tablica osobników
     * @param p1 Gracz numer 1
     * @param p2 Gracz numer 2
     * @param results Tablica wyników, na którą nachodzone będą zmiany
     */
    private void playDuelAndWriteResults(short[][] population, int p1, int p2, int[][] results) {
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

    private int[][] getDuelsResults (int[][] results, short[][] population, int popSize) {
        for (int p = 0; p < popSize; ++p) {
            results[p][0] = p;
            results[p][1] = 0;
            results[p][2] = 0;
            results[p][3] = 0;
        }
        // Ewaluacja osobników poprzez przeprowadzenie pojedynków każdy z każdym po 2 gry.
        for (int p1 = 0; p1 < popSize; ++p1) {
            for (int p2 = p1 + 1; p2 < popSize; ++p2) {
                playDuelAndWriteResults(population, p1, p2, results);
            }
        }
        for (int p = 0; p < popSize; ++p) {
            results[p][3] = results[p][1] + results[p][2];
        }
        return results;
    }

    private int[][] getDuelsResults (int[][] results, short[][] population, int popSize, int duelsNumber) {
        shuffleArray(population);
        for (int p = 0; p < popSize; ++p) {
            results[p][0] = p;
        }
        if (duelsNumber == 1) return getDuelsResultsWithSingleDuels(results, population, popSize);
        int numberOfDuelsForOne = duelsNumber / 2;
        for (int p = 0; p < popSize; ++p) {
            for (int d = 1; d <= numberOfDuelsForOne; ++d) {
                playDuelAndWriteResults(population, p, (p + d) % popSize, results);
            }
        }
        for (int p = 0; p < popSize; ++p) {
            results[p][3] = results[p][1] + results[p][2];
        }
        return results;
    }

    private int[][] getDuelsResultsWithSingleDuels (int[][] results, short[][] population, int popSize) {
        for (int p = 0; p < popSize; p += 2) {
            playDuelAndWriteResults(population, p, p + 1, results);
        }
        for (int p = 0; p < popSize; ++p) {
            results[p][3] = results[p][1] + results[p][2];
        }
        return results;
    }

    /**
     * Metoda krzyżowania dwóch osobników. Działa na zasadzie XOveru z losowo wybieranym punktem.
     * @param parentA Krzyżujący się osobnik A
     * @param parentB Krzyżujący się osobnik B
     * @return Nowy osobnik, powstały w wyniku krzyżowania
     */
    private short[] crossover (short[] parentA, short[] parentB) {
        short[] child = new short[genotypeSize];
        int crossoverPoint = RNG.randomInt(genotypeSize - 2) + 1;
        if (crossoverPoint >= 0)
            System.arraycopy(parentA, 0, child, 0, crossoverPoint);
        if (genotypeSize - crossoverPoint >= 0)
            System.arraycopy(parentB, crossoverPoint, child, crossoverPoint, genotypeSize - crossoverPoint);
        return child;
    }

    /**
     * Metoda mutacji. Wprowadza losową zmianę na losowej wartości w ciągu.
     * @param genotype Ciąg wag podlegający mutacji
     */
    private void mutation (short[] genotype) {
        int index = RNG.randomInt(genotypeSize);
        genotype[index] = RNG.randomShort();
    }

    /**
     * Funkcja generująca losowy ciąg wag.
     * @return Losowy ciąg wag
     */
    private static short[] createRandomGenotype () {
        short[] h = new short[genotypeSize];
        for (int i = 0; i < genotypeSize; ++i)
            h[i] = RNG.randomShort();
        return h;
    }

    /**
     * Tworzenie początkowej populacji z losowych osobników.
     * @param popSize Docelowa wielkość populacji
     * @return Tablica ciągów wag, będąca nową populacją
     */
    public static short[][] createStartingPopulation (int popSize) {
        short[][] population = new short[popSize][genotypeSize];
        for (int i = 0; i < popSize; ++i)
            population[i] = createRandomGenotype();
        return population;
    }

    /**
     * Służy do wygenerowania kryterium stopu na podstawie podanej wartości numerycznej.
     * @param type Wartość numeryczna kryterium stopu
     * @param stopCondStamp Postęp kryterium stopu
     * @param threshold Limit kryterium stopu
     * @return Obiekt prywatnej klasy, stanowiący kryterium stopu dla algorytmu
     */
    private StopCondition buildStopCondition (StopCond type, long stopCondStamp, long threshold) {
        StopCondition s;
        switch (type) {
            case TIME -> s = new StopConditionTime(threshold, stopCondStamp);
            case GENERATIONS -> s = new StopConditionGenerations(threshold, stopCondStamp);
            default -> s = new StopConditionTime(threshold, stopCondStamp);
        }
        return s;
    }

    /**
     * Uruchomienie sesji algorytmu genetycznego. Argumentami tej funkcji są pola w obiekcie.
     * Każda instancja klasy Genetic może wykonać sesję algorytmu genetycznego tylko raz.
     * @return Najlepiej przystosowany osobnik (ciąg wag)
     */
    public short[] run() {
        StopCondition stop = buildStopCondition(stopCondType, stopCondStamp, stopCondThreshold);

        while (stop.conditionNotMet()) {    // Dopóki nie osiągniemy kryterium stopu:
            System.out.println("Generation " + ++generation + "; Progress: " + stop.getStamp() + "/" + stop.getThreshold());
            // Selekcja populacji rodziców (każdy gra z każdym, patrzymy kto ile wygrywał jako biały/czarny).
            short[][] parents = selection(population);
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
//                population[2 * i] = parents[i];
//                population[2 * i + 1] = children[i];
                population[2 * i] = children[i];
                population[2 * i + 1] = createRandomGenotype();
            }
            // Zapisz populację i usuń poprzednią.
            String currentPopulationFilename = FileHandler.savePopulation(population, generation, duelsNumber, minmaxDepth,
                    selectionFactor, mutationChance, stopCondType, stop.getStamp(), stopCondThreshold);
            FileHandler.removePopulationsExceptOne(currentPopulationFilename);
        }
        // Przeprowadź ponowną selekcję i wyznacz najlepszego.
        selection(population);
        // Zapisz do pliku w heuristics/output/ najlepszego i go zwróć.
        String bestFilename = FileHandler.saveGeneticOutputHeuristic(bestSoFar);
        System.out.println("Najlepiej przystosowany osobnik został zapisany jako " + bestFilename);
        return bestSoFar;
    }

}
