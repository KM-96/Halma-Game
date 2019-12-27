package test;

import exceptions.FileException;
import features.Heuristic;
import model.*;
import util.FileWriter;

import java.util.*;

public class RandomMinMaxAgent {

    private final Board board;

    private Heuristic heuristic;

    public RandomMinMaxAgent(Board board) {
        this.board = board;
        this.heuristic = new Heuristic();
    }

    private Set<Move> getMoves(State state, Character currentPlayer) {
        Set<Move> allmoves = new HashSet<>();
        Player player = getPositionsOfPiecesAtHome(state, currentPlayer);
        allmoves = getMoves(player, state, currentPlayer);
        if (allmoves.size() == 0) {
            Player p = getPositionsOfCurrentPlayer(state, currentPlayer);
            allmoves = getMoves(p, state, currentPlayer);
        }
        return allmoves;
    }

    private Player getPositionsOfPiecesOutsideHome(State state, Character currentPlayer) {
        Player player = new Player(currentPlayer);
        Set<Coordinate> destinationPositions = GameConstants.BLACK_PLAYER == currentPlayer ? GameConstants.WHITE_STARTING_POSITIONS : GameConstants.BLACK_STARTING_POSITIONS;
        for (Map.Entry<Coordinate, Character> entry : state.getPositions().entrySet()) {
            if (entry.getValue() == currentPlayer && !destinationPositions.contains(entry.getKey())) {
                player.addPosition(entry.getKey());
            }
        }
        return player;
    }

    private Player getPositionsOfPiecesAtHome(State state, Character currentPlayer) {
        Player player = new Player(currentPlayer);
        Set<Coordinate> homePositions = GameConstants.BLACK_PLAYER == currentPlayer ? GameConstants.BLACK_STARTING_POSITIONS : GameConstants.WHITE_STARTING_POSITIONS;
        for (Coordinate position : homePositions) {
            if (state.getPositions().get(position) == currentPlayer) {
                player.addPosition(position);
            }
        }
        return player;
    }

    private Set<Move> getMoves(Player player, State state, Character currentPlayer) {
        Set<Move> allMoves = new HashSet<>();
        for (Coordinate position : player.getPositions()) {
            List<Move> moves = getSingleMovesForAPosition(state, position, currentPlayer);
            allMoves.addAll(moves);
            moves = getJumpsForAPosition(state, position, new HashSet<>(), currentPlayer);
            allMoves.addAll(moves);
        }
        return allMoves;
    }

    private List<Move> getSingleMovesForAPosition(State state, Coordinate position, Character currentPlayer) {
        List<Move> moves = new ArrayList<>();
        try {
            for (int i = 0; i < GameConstants.ALLOWED_MOVES.length; i++) {
                Coordinate newPosition = new Coordinate(position.getxCoordinate() + GameConstants.ALLOWED_MOVES[i][0], position.getyCoordinate() + GameConstants.ALLOWED_MOVES[i][1]);
                if (!isLegalPosition(position, newPosition, currentPlayer)) {
                    continue;
                }
                if (state.getPositions().get(newPosition) == GameConstants.EMPTY_SPACE) {
                    Move move = new Move(GameConstants.ADJACENT_CELL, position, newPosition);
                    moves.add(move);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moves;
    }


    /**
     * Generating the jumps for a particular piece on the board for a player
     *
     * @param state
     * @param position
     * @param visited
     * @param currentPlayer
     * @return
     */
    private List<Move> getJumpsForAPosition(State state, Coordinate position, Set<Coordinate> visited, Character currentPlayer) {
        List<Move> moves = new ArrayList<>();
        try {
            for (int i = 0; i < GameConstants.ALLOWED_MOVES.length; i++) {
                visited.add(position);
                Coordinate newPosition = new Coordinate(position.getxCoordinate() + GameConstants.ALLOWED_MOVES[i][0], position.getyCoordinate() + GameConstants.ALLOWED_MOVES[i][1]);
                if (isLegalPosition(position, newPosition, currentPlayer) && state.getPositions().get(newPosition) != GameConstants.EMPTY_SPACE) {
                    Coordinate jumpPosition = new Coordinate(newPosition.getxCoordinate() + GameConstants.ALLOWED_MOVES[i][0], newPosition.getyCoordinate() + GameConstants.ALLOWED_MOVES[i][1]);
                    if (isLegalPosition(position, jumpPosition, currentPlayer) && !visited.contains(jumpPosition) && state.getPositions().get(jumpPosition) == GameConstants.EMPTY_SPACE) {
                        Move move = new Move(GameConstants.JUMP_TO_CELL, position, jumpPosition);
                        move.addHop(jumpPosition);
                        moves.add(move);
                        visited.add(jumpPosition);
                        state = applyMoveOnState(state, move, currentPlayer);
                        List<Move> childMoves = (getJumpsForAPosition(state, jumpPosition, visited, currentPlayer));
                        for (Move childMove : childMoves) {
                            childMove.addHop(childMove.getFrom());
                            childMove.setFrom(position);
                        }
                        moves.addAll(childMoves);
                        state = undoMoveOnState(state, move, currentPlayer);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moves;
    }

    private boolean isLegalPosition(Coordinate oldPosition, Coordinate position, Character currentPlayer) {
        int maxCoordinate = GameConstants.BOARD_SIZE - 1;
        //Check if position is a valid position on the board
        boolean boardPositionCheck = (position.getxCoordinate() <= maxCoordinate && position.getxCoordinate() >= 0 && position.getyCoordinate() <= maxCoordinate && position.getyCoordinate() >= 0);
        if (!boardPositionCheck) {
            return boardPositionCheck;
        }
        //For the black player checking
        //1. a piece has reached destination camp but is making a move outside the camp
        //2. a piece is outside the black camp but is making a move inside the camp
        if (currentPlayer == GameConstants.BLACK_PLAYER) {
            if (checkIfPieceIsMovingOutOfDestinationCamp(GameConstants.WHITE_STARTING_POSITIONS, oldPosition, position)
                    || checkIfPieceIsMoingIntoSourceCamp(GameConstants.BLACK_STARTING_POSITIONS, oldPosition, position)) {
                return false;
            }
        }
        //Checking the above conditions for white player
        if (currentPlayer == GameConstants.WHITE_PLAYER) {
            if (checkIfPieceIsMovingOutOfDestinationCamp(GameConstants.BLACK_STARTING_POSITIONS, oldPosition, position)
                    || checkIfPieceIsMoingIntoSourceCamp(GameConstants.WHITE_STARTING_POSITIONS, oldPosition, position)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfPieceIsMovingOutOfDestinationCamp(Set<Coordinate> destinationCoordinates, Coordinate oldPosition, Coordinate newPosition) {
        return destinationCoordinates.contains(oldPosition) && !destinationCoordinates.contains(newPosition);
    }

    private boolean checkIfPieceIsMoingIntoSourceCamp(Set<Coordinate> sourceCoordinates, Coordinate oldPosition, Coordinate newPosition) {
        return !sourceCoordinates.contains(oldPosition) && sourceCoordinates.contains(newPosition);
    }

    private Player getPositionsOfCurrentPlayer(State state, Character currentPlayer) {
        Player player = new Player(currentPlayer);
        for (Map.Entry<Coordinate, Character> entry : state.getPositions().entrySet()) {
            if (entry.getValue() == currentPlayer) {
                player.addPosition(entry.getKey());
            }
        }
        return player;
    }


    //Checking if a particular state of the board is a goal state
    public boolean isGoalState(State state, Character currentPlayer) {
        Set<Coordinate> targets = currentPlayer == GameConstants.BLACK_PLAYER ? GameConstants.WHITE_STARTING_POSITIONS : GameConstants.BLACK_STARTING_POSITIONS;
        for (Coordinate target : targets) {
            if (state.getPositions().get(target) != currentPlayer) {
                return false;
            }
        }
        return true;
    }


    private State maxValue(State state, int alpha, int beta, int currentDepth, Character currentPlayer, int maxDepth) throws CloneNotSupportedException {
        //Terminal test
        if (isGoalState(state, this.board.getCurrentPlayer())) {
            System.out.println("Goal state identified for player " + this.board.getCurrentPlayer() + "in maxValue function!");
            state.setValue(Integer.MIN_VALUE);
            return state;
        }
        //Depth cutoff test
        if (currentDepth == maxDepth) {
            //Calculating the evaluation function value of the state and set it in the variable

            state.setValue(Math.random());
//            System.out.println(maxDepth + ". MAx Utility State found: Value = " + state.getValue());
            return state;
        }
        currentDepth++;
        State result = (State) state.clone();
        result.setValue(Integer.MIN_VALUE);
        Character playerForMin = currentPlayer == GameConstants.BLACK_PLAYER ? GameConstants.WHITE_PLAYER : GameConstants.BLACK_PLAYER;
        Set<Move> allMoves = getMoves(result, currentPlayer);
        for (Move move : allMoves) {
            State childState = applyMoveOnState(state, move, currentPlayer);
            childState = minValue(childState, alpha, beta, currentDepth, playerForMin, maxDepth);
            if ((childState.getValue() > result.getValue())) {
                result = childState;
                result.setMove(move);
            }
            alpha = Math.max(alpha, (int) result.getValue());
        }
        return result;
    }

    private State minValue(State state, int alpha, int beta, int currentDepth, Character currentPlayer, int maxDepth) throws CloneNotSupportedException {
        //Terminal Test
        if (isGoalState(state, this.board.getCurrentPlayer())) {
            state.setValue(Integer.MAX_VALUE);
            System.out.println("Goal state identified for player " + this.board.getCurrentPlayer() + "in minValue function!");
            return state;
        }
        //Depth cutoff test
        if (currentDepth == maxDepth) {

            //Calculating the evaluation function value of the state and set it in the variable
            state.setValue(Math.random());
            return state;
        }
        currentDepth++;
        State result = (State) state.clone();
        result.setValue(Integer.MAX_VALUE);
        Character playerForMax = currentPlayer == GameConstants.BLACK_PLAYER ? GameConstants.WHITE_PLAYER : GameConstants.BLACK_PLAYER;
        Set<Move> allMoves = getMoves(result, currentPlayer);
        for (Move move : allMoves) {
            State childState = applyMoveOnState(state, move, currentPlayer);
            childState = maxValue(childState, alpha, beta, currentDepth, playerForMax, maxDepth);
            if ((childState.getValue() < result.getValue())) {
                result = childState;
                result.setMove(move);
            }
            beta = Math.min(beta, (int) result.getValue());
        }
        return result;
    }

    private State applyMoveOnState(State state, Move move, Character currentPlayer) {
        State childState = (State) state.clone();
        state.setMove(move);
        childState.getPositions().put(move.getFrom(), GameConstants.EMPTY_SPACE);
        childState.getPositions().put(move.getTo(), currentPlayer);
        return childState;
    }

    private State undoMoveOnState(State state, Move move, Character currentPlayer) {
        State childState = (State) state.clone();
        childState.getPositions().put(move.getFrom(), currentPlayer);
        childState.getPositions().put(move.getTo(), GameConstants.EMPTY_SPACE);
        return childState;
    }

    private double evaluateBoardPosition(State state, Character currentPlayer) {
        heuristic.setState(state);
        heuristic.setCurrentPlayer(currentPlayer);
        return heuristic.calculateScore();
    }

    private Move minimax(int maxDepth) {
        Move move = null;
        try {
            int currentDepth = 0;
//            if (this.board.getCurrentPlayer() == GameConstants.WHITE_PLAYER) {
            move = maxValue(this.board.getState(), Integer.MIN_VALUE, Integer.MAX_VALUE, currentDepth, this.board.getCurrentPlayer(), maxDepth).getMove();
//            } else {
//                move = minValue(this.board.getState(), Integer.MIN_VALUE, Integer.MAX_VALUE, currentDepth, this.board.getCurrentPlayer(), maxDepth).getMove();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return move;
    }

    public boolean playGame() throws FileException {
        //ToDo: Decide a strategy based on the game mode!
        //ToDo: Calculate how deep you have to search in the tree based on the remaining time
        int maxDepth = 2;
        if (board.getTimeRemaining() < 1) {
            maxDepth = 1;
        }
        FileWriter fileWriter = new FileWriter("output.txt");
        Move move = this.minimax(maxDepth);
        fileWriter.writeToFile(move);

        //ToDo: Remove the below lines of code before pushing to vocareum
        FileWriter fileWriter1 = new FileWriter("G:\\Masters CS\\Study\\Fall 2019\\Artificial Intelligence\\Homework\\Homework2\\homework2 - Using Hashmap version - Copy\\resources\\game.txt");
        Character p = this.board.getCurrentPlayer() == GameConstants.BLACK_PLAYER ? GameConstants.WHITE_PLAYER : GameConstants.BLACK_PLAYER;
        fileWriter1.temp(p, applyMoveOnState(this.board.getState(), move, this.board.getCurrentPlayer()).printPositionsForFile());
        if (isGoalState(applyMoveOnState(this.board.getState(), move, this.board.getCurrentPlayer()), this.board.getCurrentPlayer())) {
            return true;
        }
        return false;
    }
}
