import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Klasa testująca program.
 * Główna metoda uruchamia podane podmetody testowe.
 */
public class Test {

    private static void multipleDuels (PlayerComputer player1, PlayerComputer player2, int numberOfDoubleDuels) {
        int[][] results = new int[2][numberOfDoubleDuels];
        GameHandler game1 = new GameHandler(player1, player2);
        GameHandler game2 = new GameHandler(player2, player1);
        for (int i = 0; i < numberOfDoubleDuels; i++) {
            System.out.println(i + "/" + numberOfDoubleDuels);

//            game1.printBoard();
            results[0][i] = game1.quickGame();
//            game1.printBoard();
            game1.resetBoard();

//            game2.printBoard();
            results[1][i] = game2.quickGame();
//            game2.printBoard();
            game2.resetBoard();
        }
        int sum1 = 0, sum2 = 0;
        for (int i = 0; i < numberOfDoubleDuels; i++) {
            sum1 += results[0][i];
            sum2 += results[1][i];
            results[1][i] *= -1;
        }
        System.out.println("1 vs -1 : " + Arrays.toString(results[0]));
        System.out.println("-1 vs 1 : " + Arrays.toString(results[1]));
        System.out.println("Przewaga graczy: " + sum1 + " vs " + sum2);
    }

    private static void testSpecificBoard () {
//        int[][] board = new int[][] {
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0}
//        };
        int[][] board = new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, -2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, -1, 0, 0, 0, -2, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, -1, 0, -2, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
//        int[][] board = new int[][] {
//                {0, -1, 0, -1, 0, -1, 0, -1},
//                {1, 0, 1, 0, 1, 0, 1, 0},
//                {0, -1, 0, -1, 0, -1, 0, -1},
//                {1, 0, 1, 0, 1, 0, 1, 0},
//                {0, -1, 0, -1, 0, -1, 0, -1},
//                {1, 0, 1, 0, 1, 0, 1, 0},
//                {0, -1, 0, -1, 0, -1, 0, -1},
//                {1, 0, 1, 0, 1, 0, 1, 0}
//        };
//        int[][] board = new int[][] {
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {-1, 0, 0, 0, 0, 0, 0, 0},
//                {0, -1, 0, 0, 0, 0, 0, 0},
//                {1, 0, 1, 0, 0, 0, 0, 0}
//        };
        State state = new State(board);
        int depth = 3;
        Heuristic h = new Heuristic((short)0);
        PlayerComputer p1 = new PlayerComputer(h, depth);
        PlayerHuman p2 = new PlayerHuman();
        GameHandler game = new GameHandler(p1, p2, state);
        game.run();
    }

    private static short[] getWeights1 () {
        short[] weights = new short[HParam.values().length];
        for (int i = 0; i < HParam.values().length; ++i) weights[i] = (short)1;
        weights[Heuristic.enumToInt(HParam.PAWNS)] = (short)10;
        weights[Heuristic.enumToInt(HParam.KINGS)] = (short)30;
        weights[Heuristic.enumToInt(HParam.ENEMY_PAWNS)] = (short)-5;
        weights[Heuristic.enumToInt(HParam.ENEMY_KINGS)] = (short)-8;
        weights[Heuristic.enumToInt(HParam.SAFE_PAWNS)] = (short)2;
        weights[Heuristic.enumToInt(HParam.SAFE_KINGS)] = (short)10;
        weights[Heuristic.enumToInt(HParam.SAFE_ENEMY_PAWNS)] = (short)-100;
        weights[Heuristic.enumToInt(HParam.SAFE_ENEMY_KINGS)] = (short)-200;
        weights[Heuristic.enumToInt(HParam.MOVABLE_PAWNS)] = (short)44;
        weights[Heuristic.enumToInt(HParam.MOVABLE_KINGS)] = (short)77;
        weights[Heuristic.enumToInt(HParam.MOVABLE_ENEMY_PAWNS)] = (short)-4;
        weights[Heuristic.enumToInt(HParam.MOVABLE_ENEMY_KINGS)] = (short)-7;
        weights[Heuristic.enumToInt(HParam.POSSIBLE_MOVES)] = (short)1;
        weights[Heuristic.enumToInt(HParam.POSSIBLE_ENEMY_MOVES)] = (short)-1;
        weights[Heuristic.enumToInt(HParam.DISTANCE_TO_PROMOTION)] = (short)-12;
        weights[Heuristic.enumToInt(HParam.DISTANCE_TO_ENEMY_PROMOTION)] = (short)66;
        weights[Heuristic.enumToInt(HParam.UNOCCUPIED_PROMOTION_FIELDS)] = (short)123;
        weights[Heuristic.enumToInt(HParam.UNOCCUPIED_ENEMY_PROMOTION_FIELDS)] = (short)-123;
        return weights;
    }

    private static short[] getWeights2 () {
        short[] weights = new short[HParam.values().length];
        for (int i = 0; i < HParam.values().length; ++i) weights[i] = (short)5;
//        weights[Heuristic.enumToInt(HParam.PAWNS)] = (short)-4;
//        weights[Heuristic.enumToInt(HParam.KINGS)] = (short)-1;
//        weights[Heuristic.enumToInt(HParam.ENEMY_PAWNS)] = (short)3;
//        weights[Heuristic.enumToInt(HParam.ENEMY_KINGS)] = (short)5;
//        weights[Heuristic.enumToInt(HParam.SAFE_PAWNS)] = (short)-6;
//        weights[Heuristic.enumToInt(HParam.SAFE_KINGS)] = (short)-3;
//        weights[Heuristic.enumToInt(HParam.SAFE_ENEMY_PAWNS)] = (short)4;
//        weights[Heuristic.enumToInt(HParam.SAFE_ENEMY_KINGS)] = (short)8;
//        weights[Heuristic.enumToInt(HParam.MOVABLE_PAWNS)] = (short)0;
//        weights[Heuristic.enumToInt(HParam.MOVABLE_KINGS)] = (short)0;
//        weights[Heuristic.enumToInt(HParam.MOVABLE_ENEMY_PAWNS)] = (short)0;
//        weights[Heuristic.enumToInt(HParam.MOVABLE_ENEMY_KINGS)] = (short)0;
//        weights[Heuristic.enumToInt(HParam.POSSIBLE_MOVES)] = (short)-1;
//        weights[Heuristic.enumToInt(HParam.POSSIBLE_ENEMY_MOVES)] = (short)1;
//        weights[Heuristic.enumToInt(HParam.DISTANCE_TO_PROMOTION)] = (short)1;
//        weights[Heuristic.enumToInt(HParam.DISTANCE_TO_ENEMY_PROMOTION)] = (short)-1;
//        weights[Heuristic.enumToInt(HParam.UNOCCUPIED_PROMOTION_FIELDS)] = (short)0;
//        weights[Heuristic.enumToInt(HParam.UNOCCUPIED_ENEMY_PROMOTION_FIELDS)] = (short)0;
        return weights;
    }


    private static void testAIvsAI (int depth1, int depth2, int numberOfDuels) {
        short[] weights1 = getWeights1(), weights2 = getWeights2();
        Heuristic h1 = new Heuristic(weights1);
        Heuristic h2 = new Heuristic(weights2);
        PlayerComputer c1 = new PlayerComputer(h1, depth1);
        PlayerComputer c2 = new PlayerComputer(h2, depth2);
        multipleDuels(c1, c2, numberOfDuels);
    }

    private static void testChildHeuristic () {
        //
    }

    private static void testHumanVsAI () {
        int depth = 3;
        short[] weights = getWeights1();
        Heuristic h = new Heuristic(weights);
        PlayerHuman p1 = new PlayerHuman();
        PlayerComputer p2 = new PlayerComputer(h, depth);
//        GameHandler game = new GameHandler(p1, p2);
        GameHandler game = new GameHandler(p2, p1);
        game.run();
    }

    private static void testOneParamInGame () {
        int depth = 3;
        Heuristic h = new Heuristic((short)0);
        h.changeParamWeight(Heuristic.enumToInt(HParam.DOUBLE_CORNER), (short)1);
        PlayerComputer p1 = new PlayerComputer(h, depth);
        PlayerHuman p2 = new PlayerHuman();
        GameHandler game = new GameHandler(p1, p2);
        game.run();
    }

    private static void testOneParamOneState () {
//        int[][] board = new int[][] {
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0}
//        };
//        int[][] board = new int[][] {
//                {0, -1, 0, -1, 0, -1, 0, -1},
//                {-1, 0, -1, 0, -1, 0, -1, 0},
//                {0, -1, 0, -1, 0, -1, 0, -1},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {1, 0, 1, 0, 1, 0, 1, 0},
//                {0, 1, 0, 1, 0, 1, 0, 1},
//                {1, 0, 1, 0, 1, 0, 1, 0}
//        };
//        int[][] board = new int[][] {
//                {0, -1, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 1, 0, 0},
//                {0, 0, 0, 0, 1, 0, 0, 0},
//                {0, 0, 0, -1, 0, 1, 0, 0},
//                {0, 0, 1, 0, 0, 0, 1, 0},
//                {0, 1, 0, 1, 0, 0, 0, 1},
//                {1, 0, 0, 0, 1, 0, 0, 0}
//        };
        int[][] board = new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, -1, 0, 0, 0, 2, 0, 0},
                {0, 0, 0, 0, -1, 0, 0, 0},
                {0, 0, 0, 1, 0, 1, 0, 0},
                {0, 0, 0, 0, -2, 0, 0, 0}
        };
        State state = new State(board);
        Heuristic h = new Heuristic((short)0);
        h.changeParamWeight(Heuristic.enumToInt(HParam.LONGEST_ENEMY_CAPTURE_MOVE), (short)1);
        int eval = h.evaluate(state, 1);
        state.printBoardWithCoordinates();
        System.out.println("Wartość oceny: " + eval);
    }

    private static void testDateTimeFormatter () {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String s = dtf.format(now) + ".txt";
        System.out.println(s);
    }

    private static void testSave () {
        short[] values = getWeights1();
        System.out.println(Arrays.toString(values));
        FileHandler.saveWeights(values);
    }

    private static void testLoad (String filename) {
        short[] values = FileHandler.loadWeights(filename);
        System.out.println(Arrays.toString(values));
    }

    private static void testSaveLoad () {
        short[] values = getWeights2();
        System.out.println(Arrays.toString(values));
        FileHandler.saveWeights(values);
        values = FileHandler.loadWeights();
        System.out.println(Arrays.toString(values));
        FileHandler.removeSingleHeuristics();
    }

//    private static void testPopulationSaveLoad () {
//        int populationSize = 100;
//        short[][] population1 = new short[populationSize][HParam.values().length];
//        for (short[] genotype : population1) {
//            for (int i = 0; i < HParam.values().length; ++i) {
//                genotype[i] = RNG.randomShort();
//            }
//        }
////        System.out.println(Arrays.deepToString(population1));
////        FileHandler.savePopulation(population1);
////        short[][] population2 = FileHandler.loadPopulation();
////        System.out.println(Arrays.deepToString(population2));
//        FileHandler.removePopulationsExceptOne();
//        for (int i = 0; i < populationSize; ++i) {
//            for (int j = 0; j < HParam.values().length; ++j) {
//                if (population1[i][j] != population2[i][j]) {
//                    System.out.println("Brak zgodności!");
//                    return;
//                }
//            }
//        }
//    }

    private static void testRandom () {
        int min = -3, max = 3;
        for (int i = 0; i < 100; ++i) {
            System.out.print(RNG.randomInt(min, max) + " ");
        }
    }

    private static void testDouble () {
        int i1 = 12, i2 = 14;
        ArrayList<Double> d = new ArrayList<>();
//        d.add(i1/i2);
        d.add(i1 / (double)i2);
        d.add((double)i1 / i2);
        d.add((double)(i1/i2));
        d.add((double)i1/(double)i2);
        d.add((1.0 * i1) / i2);
        d.add(i1 / (1.0 * i2));
        d.add(1.0 * (i1/i2));
        d.add((1.0 * i1) / (1.0 * i2));
        System.out.println(d);
        double dee = 0.6;
        long l = (long)(dee * 111);
        System.out.println(l);
    }

    private static void testGA () {
        Genetic ga = new Genetic(20, 20, 0.2);
        short[] bestWeights = ga.run();
        System.out.println("BEST: " + Arrays.toString(bestWeights));
        Heuristic h = new Heuristic(bestWeights);
    }

    private static void testReloadGA () {
        Genetic ga = new Genetic(20, 20, 0.2);
        short[] best = ga.run();
        System.out.println("!!! KONIEC ALGORYTMU, pora wczytać jego instancję.");
        ga = FileHandler.reloadGeneticAlgorithm();
        System.out.println("!!!! Instancja wczytania, kontynuowanie.");
        best = ga.run();
    }

    public static void main (String[] args) {
        System.out.println(Console.YELLOW_BOLD + "#### TESTING ####" + Console.RESET);
        System.out.println(Console.YELLOW_BOLD + "# Zbiór testowy #" + Console.RESET);
        {
            String[] dirs = FileHandler.getAllNecessaryDirs();
            if (FileHandler.checkIfDirectoriesExist(dirs)) {
                System.out.println(Console.RED_BOLD + "FATALNY BŁĄD: Nie udało się utworzyć potrzebnych katalogów." + Console.RESET);
                System.out.println("Utwórz ręcznie katalogi: " + Arrays.toString(dirs));
            }
        }
//        testAIvsAI(5, 5, 10);
//        testHumanVsAI();
//        testOneParamInGame();
        testOneParamOneState();
//        testChildHeuristic();
//        testDateTimeFormatter();
//        testSave();
//        testLoad("heuristics/2022-08-16_15-30-18.txt");
//        testSaveLoad();
//        testPopulationSaveLoad();
//        testRandom();
//        testGA();
//        testDouble();
//        testReloadGA();
    }
}
