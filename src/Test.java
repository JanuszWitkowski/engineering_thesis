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

    public static void main (String[] args) {
        int depth1 = 7, depth2 = 7;
        short[] weights1 = new short[HParam.values().length], weights2 = new short[HParam.values().length];
        weights1[Heuristic.enumToInt(HParam.PAWNS)] = (short)20;
        weights1[Heuristic.enumToInt(HParam.KINGS)] = (short)20;
        weights1[Heuristic.enumToInt(HParam.ENEMY_PAWNS)] = (short)-10;
        weights1[Heuristic.enumToInt(HParam.ENEMY_KINGS)] = (short)-10;
        weights1[Heuristic.enumToInt(HParam.POSSIBLE_MOVES)] = (short)5;
        weights1[Heuristic.enumToInt(HParam.ENEMY_POSSIBLE_MOVES)] = (short)-3;
        weights2[Heuristic.enumToInt(HParam.PAWNS)] = (short)0;
        weights2[Heuristic.enumToInt(HParam.KINGS)] = (short)0;
        weights2[Heuristic.enumToInt(HParam.ENEMY_PAWNS)] = (short)0;
        weights2[Heuristic.enumToInt(HParam.ENEMY_KINGS)] = (short)0;
        weights2[Heuristic.enumToInt(HParam.POSSIBLE_MOVES)] = (short)0;
        weights2[Heuristic.enumToInt(HParam.ENEMY_POSSIBLE_MOVES)] = (short)0;
        Heuristic h1 = new Heuristic(weights1);
        Heuristic h2 = new Heuristic(weights2);
        PlayerComputer c1 = new PlayerComputer(h1, depth1);
        PlayerComputer c2 = new PlayerComputer(h2, depth2);
        multipleDuels(c1, c2, 10);
    }
}
