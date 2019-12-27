package features;

import model.Coordinate;
import model.State;

public abstract class Feature {

    protected final State state;

    protected double score;

    public Feature(final State state) {
        this.state = state;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public abstract double getEvaluationFunctionValue(Coordinate position, Character currentPlayer);
}
