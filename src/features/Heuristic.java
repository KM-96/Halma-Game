package features;

import model.Coordinate;
import model.GameConstants;
import model.Player;
import model.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Heuristic {
    private List<Feature> features;

    private State state;

    private Character currentPlayer;

    public Heuristic() {
        features = new ArrayList<>();
        features.add(new ManhattanDistance(state));
        features.add(new CellValueFeature(state));
    }

    private Player getPositions(Character currentPlayer) {
        Player player = new Player(currentPlayer);
        for (Map.Entry<Coordinate, Character> entry : state.getPositions().entrySet()) {
            if (entry.getValue() == currentPlayer) {
                player.addPosition(entry.getKey());
            }
        }
        return player;
    }

    public double calculateScore() {
        return calculateScore(GameConstants.WHITE_PLAYER) - calculateScore(GameConstants.BLACK_PLAYER);
    }

    private double calculateScore(Character currentPlayer) {
        Player player = getPositions(currentPlayer);
        for (Feature feature : features) {
            double score = 0.0;
            for (Coordinate position : player.getPositions()) {
                score = score + feature.getEvaluationFunctionValue(position, currentPlayer);
            }
            feature.setScore(score);
        }
        double score = 0.0;
        for (Feature feature : features) {
            score = score + feature.getScore();
        }
        return score;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setCurrentPlayer(Character currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

}
