import java.util.Arrays;

public class Test {

    private static void multipleDuels (PlayerComputer player1, PlayerComputer player2, int numberOfDoubleDuels) {
        int[][] results = new int[2][numberOfDoubleDuels];
        GameHandler game1 = new GameHandler(player1, player2);
        GameHandler game2 = new GameHandler(player2, player1);
        for (int i = 0; i < numberOfDoubleDuels; i++) {
            System.out.println(i + "/" + numberOfDoubleDuels);

//            game1.printBoard();
            results[0][i] = game1.computerDuel();
//            game1.printBoard();
            game1.resetBoard();

//            game2.printBoard();
            results[1][i] = game2.computerDuel();
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
        short[] weights1 = new short[HParam.values().length];
        weights1[Heuristic.enumToInt(HParam.PAWNS)] = (short)2;
        weights1[Heuristic.enumToInt(HParam.KINGS)] = (short)5;
        weights1[Heuristic.enumToInt(HParam.ENEMY_PAWNS)] = (short)-2;
        weights1[Heuristic.enumToInt(HParam.ENEMY_KINGS)] = (short)-5;
        weights1[Heuristic.enumToInt(HParam.POSSIBLE_MOVES)] = (short)1;
        weights1[Heuristic.enumToInt(HParam.ENEMY_POSSIBLE_MOVES)] = (short)-1;
        return weights1;
    }

    private static short[] getWeights2 () {
        short[] weights2 = new short[HParam.values().length];
        weights2[Heuristic.enumToInt(HParam.PAWNS)] = (short)0;
        weights2[Heuristic.enumToInt(HParam.KINGS)] = (short)0;
        weights2[Heuristic.enumToInt(HParam.ENEMY_PAWNS)] = (short)0;
        weights2[Heuristic.enumToInt(HParam.ENEMY_KINGS)] = (short)0;
        weights2[Heuristic.enumToInt(HParam.POSSIBLE_MOVES)] = (short)0;
        weights2[Heuristic.enumToInt(HParam.ENEMY_POSSIBLE_MOVES)] = (short)0;
        return weights2;
    }


    private static void testAIvsAI () {
        int depth1 = 3, depth2 = 3;
        short[] weights1 = getWeights1(), weights2 = getWeights2();
        Heuristic h1 = new Heuristic(weights1);
        Heuristic h2 = new Heuristic(weights2);
        PlayerComputer c1 = new PlayerComputer(h1, depth1);
        PlayerComputer c2 = new PlayerComputer(h2, depth2);
        multipleDuels(c1, c2, 1);
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
//        testAIvsAI();
        testHumanVsAI();
    }
}
