package features;

import model.Coordinate;
import model.GameConstants;
import model.State;

import java.util.HashMap;
import java.util.Map;

/**
 * This feature assigns weight to each cell on the board.
 */
public class CellValueFeature extends Feature {
    private final Map<Coordinate, Integer> map = new HashMap<>();

    public CellValueFeature(State state) {
        super(state);
        for (int i = 0; i < GameConstants.BOARD_SIZE; i++) {
            for (int j = 0; j < GameConstants.BOARD_SIZE; j++) {
                map.put(new Coordinate(j, i), 2);
            }
        }
        for (Coordinate position : GameConstants.WHITE_STARTING_POSITIONS) {
            map.put(position, -1);
        }
        for (Coordinate positon : GameConstants.BLACK_STARTING_POSITIONS) {
            map.put(positon, 3);
        }
    }

    @Override
    public double getEvaluationFunctionValue(Coordinate position, Character currentPlayer) {
        return map.get(position);
    }
}
