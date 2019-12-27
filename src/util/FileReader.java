package util;

import exceptions.FileException;
import model.Board;
import model.Coordinate;
import model.GameConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader {

    private File file;
    private BufferedReader bufferedReader;
    private String location;

    public FileReader(String location) throws FileException {
        try {
            this.location = location;
            this.file = new File(this.location);
            this.bufferedReader = new BufferedReader(new java.io.FileReader(file));
        } catch (FileNotFoundException e) {
            throw new FileException(e.getMessage(), e);
        }
    }

    public Board readFile() {
        Board board = new Board();
        try {
            //setting game type 'SINGLE' or 'GAME'
            board.setChoice(this.bufferedReader.readLine());

            //The color that is making the current move
            board.setCurrentPlayer(this.bufferedReader.readLine().charAt(0));

            //Time available to make a move
            board.setTimeRemaining(Float.parseFloat(this.bufferedReader.readLine()));

            //Mark the positions of Black, White and empty places on the board
            for (int i = 0; i < GameConstants.BOARD_SIZE; i++) {
                String line = this.bufferedReader.readLine();
                for (int j = 0; j < GameConstants.BOARD_SIZE; j++) {
                    board.getState().addCellToState(new Coordinate(j, i), line.charAt(j));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return board;
    }
}