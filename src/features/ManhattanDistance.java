package features;

import model.Coordinate;
import model.GameConstants;
import model.State;

import java.util.HashMap;
import java.util.Map;

/**
 * Calculates the manhattan distance between the destination coordinate and the position of a coin on the board.
 */
public class ManhattanDistance extends Feature {
    private Map<Coordinate, Double> map;

    public ManhattanDistance(State state) {
        super(state);
        this.map = new HashMap<>();
    }

    @Override
    public double getEvaluationFunctionValue(Coordinate position, Character currentPlayer) {
        if (map.containsKey(position)) {
            return map.get(position);
        } else {
            Coordinate target = GameConstants.DESTINATION_CORNER_FOR_BLACK;
            double value = Math.abs(target.getxCoordinate() - position.getxCoordinate()) + Math.abs(target.getyCoordinate() - position.getyCoordinate());
            map.put(position, value);
            return value;
        }
    }
}