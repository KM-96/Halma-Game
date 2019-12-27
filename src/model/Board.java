package model;

import java.util.Map;

public class Board {
    //setting game type 'SINGLE' or 'GAME'
    private String choice;

    //The color that is making the current move
    private Character currentPlayer;

    //Time available to make a move
    private float timeRemaining;

    //Player playing white
    private Map<Character, Player> players;

    //Current state of the board
    private State state;

    public Board() {
        this.state = new State();
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public Character getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Character currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public float getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(float timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public Map<Character, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Character, Player> players) {
        this.players = players;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
