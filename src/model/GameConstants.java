package model;

import java.util.HashSet;
import java.util.Set;

public class GameConstants {
    public static final String SINGLE_MODE = "SINGLE";
    public static final String GAME_MODE = "GAME";
    public static final int BOARD_SIZE = 16;
    public static final Character WHITE_PLAYER = 'W';
    public static final Character BLACK_PLAYER = 'B';
    public static final Character EMPTY_SPACE = '.';
    public static final int[][] ALLOWED_MOVES = {{1, 1}, {1, 0}, {0, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}, {-1, 1}};
    public static final Character JUMP_TO_CELL = 'J';
    public static final Character ADJACENT_CELL = 'E';

    public static final Coordinate DESTINATION_CORNER_FOR_WHITE = new Coordinate(0, 0);
    public static final Coordinate DESTINATION_CORNER_FOR_BLACK = new Coordinate(BOARD_SIZE - 1, BOARD_SIZE - 1);

    public static final Set<Coordinate> BLACK_STARTING_POSITIONS = new HashSet<>();
    public static final Set<Coordinate> WHITE_STARTING_POSITIONS = new HashSet<>();

    static {
        BLACK_STARTING_POSITIONS.add(new Coordinate(0, 0));
        BLACK_STARTING_POSITIONS.add(new Coordinate(1, 0));
        BLACK_STARTING_POSITIONS.add(new Coordinate(0, 1));
        BLACK_STARTING_POSITIONS.add(new Coordinate(2, 0));
        BLACK_STARTING_POSITIONS.add(new Coordinate(3, 0));
        BLACK_STARTING_POSITIONS.add(new Coordinate(4, 0));
        BLACK_STARTING_POSITIONS.add(new Coordinate(1, 1));
        BLACK_STARTING_POSITIONS.add(new Coordinate(2, 1));
        BLACK_STARTING_POSITIONS.add(new Coordinate(3, 1));
        BLACK_STARTING_POSITIONS.add(new Coordinate(4, 1));
        BLACK_STARTING_POSITIONS.add(new Coordinate(0, 2));
        BLACK_STARTING_POSITIONS.add(new Coordinate(1, 2));
        BLACK_STARTING_POSITIONS.add(new Coordinate(2, 2));
        BLACK_STARTING_POSITIONS.add(new Coordinate(3, 2));
        BLACK_STARTING_POSITIONS.add(new Coordinate(0, 3));
        BLACK_STARTING_POSITIONS.add(new Coordinate(1, 3));
        BLACK_STARTING_POSITIONS.add(new Coordinate(2, 3));
        BLACK_STARTING_POSITIONS.add(new Coordinate(0, 4));
        BLACK_STARTING_POSITIONS.add(new Coordinate(1, 4));

        WHITE_STARTING_POSITIONS.add(new Coordinate(15, 15));
        WHITE_STARTING_POSITIONS.add(new Coordinate(14, 15));
        WHITE_STARTING_POSITIONS.add(new Coordinate(13, 15));
        WHITE_STARTING_POSITIONS.add(new Coordinate(12, 15));
        WHITE_STARTING_POSITIONS.add(new Coordinate(11, 15));
        WHITE_STARTING_POSITIONS.add(new Coordinate(15, 14));
        WHITE_STARTING_POSITIONS.add(new Coordinate(14, 14));
        WHITE_STARTING_POSITIONS.add(new Coordinate(13, 14));
        WHITE_STARTING_POSITIONS.add(new Coordinate(12, 14));
        WHITE_STARTING_POSITIONS.add(new Coordinate(11, 14));
        WHITE_STARTING_POSITIONS.add(new Coordinate(15, 13));
        WHITE_STARTING_POSITIONS.add(new Coordinate(14, 13));
        WHITE_STARTING_POSITIONS.add(new Coordinate(13, 13));
        WHITE_STARTING_POSITIONS.add(new Coordinate(12, 13));
        WHITE_STARTING_POSITIONS.add(new Coordinate(15, 12));
        WHITE_STARTING_POSITIONS.add(new Coordinate(14, 12));
        WHITE_STARTING_POSITIONS.add(new Coordinate(13, 12));
        WHITE_STARTING_POSITIONS.add(new Coordinate(15, 11));
        WHITE_STARTING_POSITIONS.add(new Coordinate(14, 11));
    }

}
