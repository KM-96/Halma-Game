package features;

import model.Coordinate;
import model.GameConstants;
import model.State;

import java.util.HashMap;
import java.util.Map;

/**
 * Calculates the euclidean distance between a given point and the position of a piece on the board
 */
public class EuclideanDistance extends Feature {
    private Map<Coordinate, Double> map;

    public EuclideanDistance(State state) {
        super(state);
        this.map = new HashMap<>();
    }

    @Override
    public double getEvaluationFunctionValue(Coordinate position, Character currentPlayer) {
        if (map.containsKey(position)) {
            return map.get(position);
        } else {
            Coordinate target = GameConstants.DESTINATION_CORNER_FOR_BLACK;
            double value = Math.sqrt(((position.getxCoordinate() - target.getxCoordinate()) * (position.getxCoordinate() - target.getxCoordinate())) +
                    ((position.getyCoordinate() - target.getyCoordinate()) * (position.getyCoordinate() - target.getyCoordinate())));
            map.put(position, value);
            return value;
        }
    }
}
