import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PlayerHuman extends Player {
    protected final Scanner scanner = new Scanner(System.in);

    @Override
    public boolean isComputer () {
        return false;
    }

    @Override
    public void makeMove (State board, int playerNumber) {
        String input;
        char col;
        int row, n;
        boolean incorrectInput = true;
        while (incorrectInput) {
            System.out.println("Dostępne ruchy: " + board.currentPlayerMoves());
            System.out.println("Podaj współrzędne pionka którym chcesz się ruszyć.");
            System.out.print("kolumna/numer: ");
            input = scanner.nextLine();
            try {
                n = Integer.parseInt(input);
                if (!board.checkUserInput(n)) throw new InputMismatchException();
            } catch (NumberFormatException e) {
                col = input.charAt(0);
                System.out.print("rząd: ");
                try {
                    row = scanner.nextInt() - 1;
                    scanner.nextLine();
                    if (!board.checkUserInput(col, row)) throw new InputMismatchException();
                    n = board.coordinatesToNumber(row, State.letterToNumber(col));
                } catch (InputMismatchException ex) {
                    System.out.println("BŁĄD 01: Niepoprawny input dla rzędu.");
                    continue;
                }
            } catch (InputMismatchException ex) {
                System.out.println("BŁĄD 02: Numer pola nie znajduje się w zakresie.");
                continue;
            }
            ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
            for (ArrayList<Integer> move : board.currentPlayerMoves())
                if (move.get(0) == n) moves.add(move);
            if (moves.isEmpty()) {
                System.out.println("BŁĄD 03: Z podanego pola nie można wykonać ruchu.");
                continue;
            }
            int numberOfMoves = 1;

            while (moves.size() > 1 || numberOfMoves < moves.get(0).size()) {
                System.out.println("Podaj współrzędne pola do którego chcesz się poruszyć.");
                System.out.print("kolumna/numer: ");
                input = scanner.nextLine();
                try {
                    n = Integer.parseInt(input);
                    if (!board.checkUserInput(n)) throw new InputMismatchException();
                } catch (NumberFormatException e) {
                    col = input.charAt(0);
                    System.out.print("rząd: ");
                    try {
                        row = scanner.nextInt() - 1;
                        scanner.nextLine();
                        if (!board.checkUserInput(col, row)) throw new InputMismatchException();
                        n = board.coordinatesToNumber(row, State.letterToNumber(col));
                    } catch (InputMismatchException ex) {
                        System.out.println("BŁĄD 04: Niepoprawny input dla rzędu.");
                        continue;
                    }
                } catch (InputMismatchException ex) {
                    System.out.println("BŁĄD 05: Numer pola nie znajduje się w zakresie.");
                    continue;
                }
                for (ArrayList<Integer> move : board.currentPlayerMoves())
                    if (move.size() <= numberOfMoves || move.get(numberOfMoves) != n) moves.remove(move);
                if (moves.isEmpty()) {
                    System.out.println("BŁĄD 06: Niedozwolony ruch.");
                    continue;
                }
                ++numberOfMoves;
            }
            incorrectInput = false;
            board.makeMove(moves.get(0));
        }
    }

}
