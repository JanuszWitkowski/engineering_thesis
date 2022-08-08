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

    private static short[] getWeights1 () {
        short[] weights = new short[HParam.values().length];
        weights[Heuristic.enumToInt(HParam.PAWNS)] = (short)2;
        weights[Heuristic.enumToInt(HParam.KINGS)] = (short)5;
        weights[Heuristic.enumToInt(HParam.ENEMY_PAWNS)] = (short)-2;
        weights[Heuristic.enumToInt(HParam.ENEMY_KINGS)] = (short)-5;
        weights[Heuristic.enumToInt(HParam.SAFE_PAWNS)] = (short)4;
        weights[Heuristic.enumToInt(HParam.SAFE_KINGS)] = (short)8;
        weights[Heuristic.enumToInt(HParam.ENEMY_SAFE_PAWNS)] = (short)-6;
        weights[Heuristic.enumToInt(HParam.ENEMY_SAFE_KINGS)] = (short)-3;
        weights[Heuristic.enumToInt(HParam.POSSIBLE_MOVES)] = (short)1;
        weights[Heuristic.enumToInt(HParam.ENEMY_POSSIBLE_MOVES)] = (short)-1;
        return weights;
    }

    private static short[] getWeights2 () {
        short[] weights = new short[HParam.values().length];
        weights[Heuristic.enumToInt(HParam.PAWNS)] = (short)-4;
        weights[Heuristic.enumToInt(HParam.KINGS)] = (short)-1;
        weights[Heuristic.enumToInt(HParam.ENEMY_PAWNS)] = (short)3;
        weights[Heuristic.enumToInt(HParam.ENEMY_KINGS)] = (short)5;
        weights[Heuristic.enumToInt(HParam.SAFE_PAWNS)] = (short)-6;
        weights[Heuristic.enumToInt(HParam.SAFE_KINGS)] = (short)-3;
        weights[Heuristic.enumToInt(HParam.ENEMY_SAFE_PAWNS)] = (short)4;
        weights[Heuristic.enumToInt(HParam.ENEMY_SAFE_KINGS)] = (short)8;
        weights[Heuristic.enumToInt(HParam.POSSIBLE_MOVES)] = (short)-1;
        weights[Heuristic.enumToInt(HParam.ENEMY_POSSIBLE_MOVES)] = (short)1;
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

    private static void testHumanVsAI () {
        int depth = 3;
        short[] weights = getWeights1();
        Heuristic h = new Heuristic(weights);
        PlayerHuman p1 = new PlayerHuman();
        PlayerComputer p2 = new PlayerComputer(h, depth);
        GameHandler game = new GameHandler(p1, p2);
        game.run();
    }


    public static void main (String[] args) {
        testAIvsAI(5, 5, 10);
//        testHumanVsAI();
    }
}
