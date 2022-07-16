import java.util.ArrayList;
import java.util.Scanner;

public class PlayerHuman extends Player {
    protected final Scanner scanner = new Scanner(System.in);

    @Override
    public boolean isComputer () {
        return false;
    }

    // TODO: Rozbudować.
    @Override
    public void makeMove (State board) {
        int row1, col1, row2, col2;
        System.out.println("Dostępne ruchy: " + board.currentPlayerMoves());
        System.out.println("Podaj współrzędne pionka którym chcesz się ruszyć.");
        System.out.print("rząd: ");
        row1 = scanner.nextInt();
        System.out.print("kolumna: ");
        col1 = scanner.nextInt();
        System.out.println("Podaj współrzędne pola do którego chcesz się poruszyć.");
        System.out.print("rząd: ");
        row2 = scanner.nextInt();
        System.out.print("kolumna: ");
        col2 = scanner.nextInt();
        ArrayList<Integer> move = new ArrayList<>();
        move.add(board.coordinatesToNumber(row1, col1));
        move.add(board.coordinatesToNumber(row2, col2));
        board.makeMove(move);
    }

}
