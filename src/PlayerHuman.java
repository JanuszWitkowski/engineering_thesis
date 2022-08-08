import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Interfejs dla gracza ludzkiego. Obsługuje input i wykonuje ruchy.
 */
public class PlayerHuman extends Player {
    protected final Scanner scanner = new Scanner(System.in);

    @Override
    public boolean isComputer () {
        return false;
    }

    /**
     * Interaktywna obsługa ruchu ludzkiego gracza.
     * Należy podać współrzędne lub numery pól z których i do których chcemy się poruszyć.
     * Metoda obsługuje standardowe wyjątki oraz ruchy kilkupolowe.
     * @param board Obecny stan gry
     * @param playerNumber Numer ludzkiego gracza
     */
    @Override
    public void makeMove (State board, int playerNumber) {
        String input;
        char col;
        int row, n;
        boolean incorrectInput = true;

        // Możemy wyjść z tej pętli wyłącznie gdy wprowadzimy poprawny input i będzie on odpowiadał za poprawny ruch.
        while (incorrectInput) {
            System.out.println("Dostępne ruchy: " + board.currentPlayerMoves());    // Wydrukuj listę legalnych ruchów.
            System.out.println("Podaj współrzędne pionka którym chcesz się ruszyć.");
            System.out.print("kolumna/numer: ");

            input = scanner.nextLine();
            try {   // Spróbuj sparsować input jako numer pola.
                n = Integer.parseInt(input);
                if (board.isUserInputInvalid(n)) throw new InputMismatchException();
            } catch (NumberFormatException e) {     // Użytkownik prawdopodobnie podał współrzędną kolumny pola.
                col = input.charAt(0);
                System.out.print("rząd: ");
                try {   // Spróbuj sparsować input jako współrzędną rzędu pola.
                    row = scanner.nextInt() - 1;
                    scanner.nextLine();
                    if (board.isUserInputInvalid(col, row)) throw new InputMismatchException();
                    n = board.coordinatesToNumber(row, State.letterToNumber(col));
                } catch (InputMismatchException ex) {
                    System.out.println("BŁĄD 01: Niepoprawny input dla rzędu.");
                    continue;
                }
            } catch (InputMismatchException ex) {
                System.out.println("BŁĄD 02: Numer pola nie znajduje się w zakresie.");
                continue;
            }

            // Stwórz dynamiczny magazyn potencjalnych ruchów które może chcieć wykonać gracz.
            ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
            for (ArrayList<Integer> move : board.currentPlayerMoves())
                if (move.get(0) == n) moves.add(move);
            if (moves.isEmpty()) {  // Jeżeli magazyn ruchów jest pusty, oznacza to że ruch będzie niepoprawny.
                System.out.println("BŁĄD 03: Z podanego pola nie można wykonać ruchu.");
                continue;
            }
            int numberOfMoves = 1;

            // Będziemy przyjmować inputy do momentu określenia jednego poprawnego ruchu.
            while (moves.size() > 1 || numberOfMoves < moves.get(0).size()) {
                System.out.println("Podaj współrzędne pola do którego chcesz się poruszyć.");
                System.out.print("kolumna/numer: ");
                input = scanner.nextLine();
                try {   // Spróbuj sparsować input jako numer pola.
                    n = Integer.parseInt(input);
                    if (board.isUserInputInvalid(n)) throw new InputMismatchException();
                } catch (NumberFormatException e) {     // Użytkownik prawdopodobnie podał współrzędną kolumny pola.
                    col = input.charAt(0);
                    System.out.print("rząd: ");
                    try {   // Spróbuj sparsować input jako współrzędną rzędu pola.
                        row = scanner.nextInt() - 1;
                        scanner.nextLine();
                        if (board.isUserInputInvalid(col, row)) throw new InputMismatchException();
                        n = board.coordinatesToNumber(row, State.letterToNumber(col));
                    } catch (InputMismatchException ex) {
                        System.out.println("BŁĄD 04: Niepoprawny input dla rzędu.");
                        continue;
                    }
                } catch (InputMismatchException ex) {
                    System.out.println("BŁĄD 05: Numer pola nie znajduje się w zakresie.");
                    continue;
                }

                // Wymaż z magazynu wszystkie ruchy o które na pewno nie chodzi graczowi.
                for (ArrayList<Integer> move : board.currentPlayerMoves())
                    if (move.size() <= numberOfMoves || move.get(numberOfMoves) != n) moves.remove(move);
                if (moves.isEmpty()) {  // Jeżeli magazyn ruchów jest pusty, oznacza to że ruch będzie niepoprawny.
                    System.out.println("BŁĄD 06: Niedozwolony ruch.");
                    continue;
                }
                ++numberOfMoves;
            }
            incorrectInput = !board.submitUserInput(moves.get(0));    // Wykonaj jeden jedyny ruch.
        }
    }

}
