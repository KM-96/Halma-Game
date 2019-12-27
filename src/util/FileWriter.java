package util;

import exceptions.FileException;
import model.Coordinate;
import model.GameConstants;
import model.Move;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class FileWriter {

    private java.io.FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private String location;

    public FileWriter(String location) throws FileException {
        try {
            this.location = location;
            this.fileWriter = new java.io.FileWriter(location);
            this.bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            throw new FileException(e.getMessage(), e);
        }
    }

    public void writeToFile(Move move) throws FileException {
        try {
            if (move.getMove() == GameConstants.ADJACENT_CELL) {
                String output = getOutputString(GameConstants.ADJACENT_CELL, move.getFrom(), move.getTo());
                bufferedWriter.write(output);
            } else {
                Set<Coordinate> list = new LinkedHashSet<>();
                list.addAll(move.getHops());
                Coordinate[] coordinates = (Coordinate[]) list.toArray(new Coordinate[move.getHops().size() + 1]);
                coordinates[coordinates.length - 1] = move.getFrom();
                StringBuilder output = new StringBuilder();
                for (int i = coordinates.length - 1; i > 0; i--) {
                    output.append(getOutputString(GameConstants.JUMP_TO_CELL, coordinates[i], coordinates[i - 1]));
                    output.append("\n");
                }
                bufferedWriter.write(output.toString().trim());
            }
        } catch (Exception e) {
            throw new FileException(e.getMessage(), e);
        } finally {
            try {
                this.bufferedWriter.close();
            } catch (IOException e) {
                throw new FileException(e.getMessage(), e);
            }
        }
    }

    public void temp(Character p, String pos) throws FileException {
        try {
            bufferedWriter.write("GAME");
            bufferedWriter.newLine();
            bufferedWriter.write(p);
            bufferedWriter.newLine();
            bufferedWriter.write("300");
            bufferedWriter.write(pos);
        } catch (Exception e) {
            throw new FileException(e.getMessage(), e);
        } finally {
            try {
                this.bufferedWriter.close();
            } catch (IOException e) {
                throw new FileException(e.getMessage(), e);
            }
        }
    }

    private String getOutputString(Character type, Coordinate from, Coordinate to) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(type + " ");
        stringBuilder.append(from.getxCoordinate() + "," + from.getyCoordinate() + " ");
        stringBuilder.append(to.getxCoordinate() + "," + to.getyCoordinate());
        return stringBuilder.toString();
    }
}
