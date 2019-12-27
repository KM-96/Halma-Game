package game;

import exceptions.FileException;
import features.Heuristic;
import model.*;
import util.FileWriter;

import java.util.*;

public class Agent {

    private final Board board;

    private Heuristic heuristic;

    public Agent(Board board) {
        this.board = board;
        this.heuristic = new Heuristic();
    }

    /**
     * Returns the base coordinates of a player
     *
     * @param currentPlayer
     * @return
     */
    private Set<Coordinate> getHomeCoordinates(Character currentPlayer) {
        return currentPlayer == GameConstants.BLACK_PLAYER ? GameConstants.BLACK_STARTING_POSITIONS : GameConstants.WHITE_STARTING_POSITIONS;
    }

    /**
     * Generates all the possible legal moves for a particular state of the board
     *
     * @param state
     * @param currentPlayer
     * @return
     */
    private List<Move> getMoves(State state, Character currentPlayer) {
        List<Move> allmoves = new ArrayList<>();
        Player player = getPositionsOfPiecesAtHome(state, currentPlayer);
        allmoves = getMoves(player, state, currentPlayer);
        if (allmoves.size() > 0) {
            allmoves = removeMovesGoingTowardsHomeBase(allmoves, currentPlayer);
        }
        if (allmoves.size() == 0) {
            Player p = getPositionsOfCurrentPlayer(state, currentPlayer);
            allmoves = getMoves(p, state, currentPlayer);
        }
        Collections.shuffle(allmoves);
        return allmoves;
    }

    /**
     * Get all the positions of a player at his own base
     *
     * @param state
     * @param currentPlayer
     * @return
     */
    private Player getPositionsOfPiecesAtHome(State state, Character currentPlayer) {
        Player player = new Player(currentPlayer);
        Set<Coordinate> homePositions = getHomeCoordinates(currentPlayer);
        for (Coordinate position : homePositions) {
            if (state.getPositions().get(position) == currentPlayer) {
                player.addPosition(position);
            }
        }
        return player;
    }

    /**
     * Generates all the adjacent and jump moves
     *
     * @param player
     * @param state
     * @param currentPlayer
     * @return
     */
    private List<Move> getMoves(Player player, State state, Character currentPlayer) {
        List<Move> allMoves = new ArrayList<>();
        for (Coordinate position : player.getPositions()) {
            List<Move> moves = getJumpsForAPosition(state, position, new HashSet<>(), currentPlayer);
            allMoves.addAll(moves);
            moves = getSingleMovesForAPosition(state, position, currentPlayer);
            allMoves.addAll(moves);
        }
        return allMoves;
    }

    private List<Move> getSingleMovesForAPosition(State state, Coordinate position, Character currentPlayer) {
        return getSingleMovesForAPosition(state, position, currentPlayer, GameConstants.ALLOWED_MOVES);
    }

    /**
     * Generate adjacent moves
     *
     * @param state
     * @param position
     * @param currentPlayer
     * @param allowedMoves
     * @return
     */
    private List<Move> getSingleMovesForAPosition(State state, Coordinate position, Character currentPlayer, int[][] allowedMoves) {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < allowedMoves.length; i++) {
            Coordinate newPosition = new Coordinate(position.getxCoordinate() + allowedMoves[i][0], position.getyCoordinate() + allowedMoves[i][1]);
            if (!isLegalPosition(position, newPosition, currentPlayer)) {
                continue;
            }
            if (state.getPositions().get(newPosition) == GameConstants.EMPTY_SPACE) {
                Move move = new Move(GameConstants.ADJACENT_CELL, position, newPosition);
                moves.add(move);
            }
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
        return moves;
    }

    /**
     * Check if a particular position is legal for a given player
     *
     * @param oldPosition
     * @param position
     * @param currentPlayer
     * @return
     */
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

    private List<Move> removeMovesGoingTowardsHomeBase(List<Move> moves, Character currentPlayer) {
        List<Move> movesOutsideTheCamp = new ArrayList<>();
        List<Move> movesInsideTheCampButAwayFromBase = new ArrayList<>();
        Set<Coordinate> homeCoordinates = getHomeCoordinates(currentPlayer);
        for (Move move : moves) {
            if (homeCoordinates.contains(move.getTo())) {
                if (currentPlayer == GameConstants.BLACK_PLAYER) {
                    if (move.getTo().getxCoordinate() >= move.getFrom().getxCoordinate() && move.getTo().getyCoordinate() >= move.getFrom().getyCoordinate()) {
                        movesInsideTheCampButAwayFromBase.add(move);
                    }
                } else {
                    if (move.getTo().getxCoordinate() <= move.getFrom().getxCoordinate() && move.getTo().getyCoordinate() <= move.getFrom().getyCoordinate()) {
                        movesInsideTheCampButAwayFromBase.add(move);
                    }
                }
            } else {
                movesOutsideTheCamp.add(move);
            }
        }
        return movesOutsideTheCamp.isEmpty() ? movesInsideTheCampButAwayFromBase : movesOutsideTheCamp;
    }

    /**
     * Get all the positions of a player on the board
     *
     * @param state
     * @param currentPlayer
     * @return
     */
    private Player getPositionsOfCurrentPlayer(State state, Character currentPlayer) {
        Player player = new Player(currentPlayer);
        for (Map.Entry<Coordinate, Character> entry : state.getPositions().entrySet()) {
            if (entry.getValue() == currentPlayer) {
                player.addPosition(entry.getKey());
            }
        }
        return player;
    }


    /**
     * Checking if a particular state of the board is a goal state
     *
     * @param state
     * @param currentPlayer
     * @return
     */
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
            state.setGoalState(true);
            state.setValue(Integer.MIN_VALUE / 2 + currentDepth);
            return state;
        }
        //Depth cutoff test
        if (currentDepth == maxDepth) {
            //Calculating the evaluation function value of the state and set it in the variable
            state.setValue(evaluateBoardPosition(state, currentPlayer));
            return state;
        }
        currentDepth++;
        State result = (State) state.clone();
        result.setValue(Integer.MIN_VALUE);
        Character playerForMin = currentPlayer == GameConstants.BLACK_PLAYER ? GameConstants.WHITE_PLAYER : GameConstants.BLACK_PLAYER;
        List<Move> allMoves = getMoves(result, currentPlayer);
        for (Move move : allMoves) {
            State childState = applyMoveOnState(state, move, currentPlayer);
            childState = minValue(childState, alpha, beta, currentDepth, playerForMin, maxDepth);
            if ((childState.getValue() > result.getValue())) {
                result = childState;
                result.setMove(move);
            }
            if (result.getValue() >= beta) {
                return result;
            }
            alpha = Math.max(alpha, (int) result.getValue());
        }
        return result;
    }

    private State minValue(State state, int alpha, int beta, int currentDepth, Character currentPlayer, int maxDepth) throws CloneNotSupportedException {
        //Terminal Test
        if (isGoalState(state, this.board.getCurrentPlayer())) {
            state.setGoalState(true);
            state.setValue(Integer.MAX_VALUE / 2 - currentDepth);
            return state;
        }
        //Depth cutoff test
        if (currentDepth == maxDepth) {
            //Calculating the evaluation function value of the state and set it in the variable
            state.setValue(evaluateBoardPosition(state, currentPlayer));
            return state;
        }
        currentDepth++;
        State result = (State) state.clone();
        result.setValue(Integer.MAX_VALUE);
        Character playerForMax = currentPlayer == GameConstants.BLACK_PLAYER ? GameConstants.WHITE_PLAYER : GameConstants.BLACK_PLAYER;
        List<Move> allMoves = getMoves(result, currentPlayer);
        for (Move move : allMoves) {
            State childState = applyMoveOnState(state, move, currentPlayer);
            childState = maxValue(childState, alpha, beta, currentDepth, playerForMax, maxDepth);
            if ((childState.getValue() < result.getValue())) {
                result = childState;
                result.setMove(move);
            }
            if (result.getValue() <= alpha) {
                return result;
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

    private Move minimax(int maxDepth) throws CloneNotSupportedException {
        int currentDepth = 0;
        return maxValue(this.board.getState(), Integer.MIN_VALUE, Integer.MAX_VALUE, currentDepth, this.board.getCurrentPlayer(), maxDepth).getMove();
    }

    /**
     * Starts the game and then writes the output of the move to the output file
     *
     * @throws FileException
     * @throws CloneNotSupportedException
     */
    public void playGame() throws FileException, CloneNotSupportedException {
        int maxDepth = calculateMaxDepth();
        Move move = this.minimax(maxDepth);
        FileWriter fileWriter = new FileWriter("output.txt");
        fileWriter.writeToFile(move);

        //This is only used during the execution of the master agent. Not related to the actual submission of the project
        //ToDo: Remove the below lines of code before pushing to vocareum
        FileWriter fileWriter1 = new FileWriter(".\\resources\\game.txt");
        Character p = this.board.getCurrentPlayer() == GameConstants.BLACK_PLAYER ? GameConstants.WHITE_PLAYER : GameConstants.BLACK_PLAYER;
        fileWriter1.temp(p, applyMoveOnState(this.board.getState(), move, this.board.getCurrentPlayer()).printPositionsForFile());
    }


    /**
     * Calculates max depth based on the remaining time
     *
     * @return
     */
    private int calculateMaxDepth() {
        int maxDepth = 2;
        if (GameConstants.SINGLE_MODE.equals(board.getChoice())) {
            maxDepth = board.getTimeRemaining() < 4 ? 1 : 2;
        } else {
            if (board.getTimeRemaining() < 15) {
                maxDepth = 1;
            } else if (board.getTimeRemaining() < 120) {
                maxDepth = 2;
            } else {
                maxDepth = 3;
            }
        }
        return maxDepth;
    }
}