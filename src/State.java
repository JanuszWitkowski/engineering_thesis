import java.util.ArrayList;

public class State {
    private final int dimension;

    public State () {
        this.dimension = 8;
    }

    public State (int dimension) {
        this.dimension = dimension;
    }

    public boolean gameOver() {
        return false;
    }

    public ArrayList<State> getChildren () {
        ArrayList<State> children = new ArrayList<>();
        return children;
    }
}
