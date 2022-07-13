public class PlayerComputer extends Player {
    private final Heuristic heuristic;

    public PlayerComputer () {
        super();
        this.isComputer = true;
        this.heuristic = new Heuristic((short) 0);
    }

    public PlayerComputer (Heuristic heuristic) {
        super();
        this.isComputer = true;
        this.heuristic = heuristic;
    }
}
