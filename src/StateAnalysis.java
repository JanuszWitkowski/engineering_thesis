public class StateAnalysis {
    private final State board;
    private boolean needToCalculateParams;

    public StateAnalysis (State board) {
        this.board = board;
        this.needToCalculateParams = true;
        // ...
    }

    private void calculateParams () {
        needToCalculateParams = false;
        // ...
    }

    // ...
}
