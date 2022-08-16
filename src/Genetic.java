import java.util.Random;

public class Genetic {
    private static final int genotypeSize = HParam.values().length;
    private static final int crossoverPoint = genotypeSize / 2;

    private final Random rng = new Random();

    private int populationSize;
    private int parentPopulationSize;
    private int mutationChance;
    private double mutationPercentage = 0.1;

    private short[] globalBest;

    public short randomShort () {
        return (short)(rng.nextInt(Short.MAX_VALUE - Short.MIN_VALUE + 1) + Short.MIN_VALUE);
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

    private short[] mutation (short[] genotype) {
        int index = rng.nextInt(genotypeSize);
        double modifier = rng.nextBoolean() ? 1.0 : -1.0;
        genotype[index] = (short) (genotype[index] + (short)(modifier * mutationPercentage * genotype[index]));
        return genotype;
    }

    public short[] GA () {
        // Wyznacz populację początkową.
        // Dopóki nie osiągniemy kryterium stopu:
        // Selekcja populacji rodziców (każdy gra z każdym, patrzymy kto ile wygrywał jako biały/czarny).
        // Krzyżowanie (wyznaczamy pary rodziców, każda para rodziców ma dwoje dzieci).
        // Mutacje (szansa na mutację, zmieniamy jeden parametr o jakiś procent).
        // Wyjście z pętli.
        // Przeprowadź ponowną selekcję i wyznacz najlepszego.
        // Zapisz do pliku w heuristics/output/ najlepszego i go zwróć.
        // (V2) Pętla to: Krzyżowanie, Mutacja, Selekcja. Dzięki temu nie powtarzamy selekcji po wyjściu z pętli.
        return null;
    }

}
