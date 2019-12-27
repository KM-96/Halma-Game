package model;

import java.util.HashMap;
import java.util.Map;

public class State implements Cloneable {

    //Storing which coordinate contains which value
    private Map<Coordinate, Character> positions;

    //Heuristic Value for this state
    private double value;

    private Move move;

    private boolean isGoalState = false;

    public boolean isGoalState() {
        return isGoalState;
    }

    public void setGoalState(boolean goalState) {
        isGoalState = goalState;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public State() {
        this.positions = new HashMap<>();
    }

    public Map<Coordinate, Character> getPositions() {
        return positions;
    }

    public void setPositions(Map<Coordinate, Character> positions) {
        this.positions = positions;
    }

    public void addCellToState(Coordinate coordinate, Character character) {
        positions.put(coordinate, character);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public Object clone() {
        State state = null;
        try {
            state = (State) super.clone();
            Map<Coordinate, Character> map = new HashMap<>();
            map.putAll(this.positions);
            state.setPositions(map);
        } catch (CloneNotSupportedException e) {
            state = new State();
        }
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;

        State state = (State) o;

        if (getValue() != state.getValue()) return false;
        return getPositions() != null ? getPositions().equals(state.getPositions()) : state.getPositions() == null;
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "State{" +
                "positions=" + positions +
                ", value=" + value +
                '}';
    }

    public void printPositions() {
        for (int i = 0; i < GameConstants.BOARD_SIZE; i++) {
            System.out.println();
            for (int j = 0; j < GameConstants.BOARD_SIZE; j++) {
                System.out.print(this.positions.get(new Coordinate(j, i)));
            }
        }
    }

    public String printPositionsForFile() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < GameConstants.BOARD_SIZE; i++) {
            stringBuilder.append("\n");
            for (int j = 0; j < GameConstants.BOARD_SIZE; j++) {
                stringBuilder.append(this.positions.get(new Coordinate(j, i)));
            }
        }
        return stringBuilder.toString();
    }
}
