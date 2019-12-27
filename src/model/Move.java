package model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class Move {
    private Character move;
    private Coordinate from;
    private Coordinate to;
    private Set<Coordinate> hops;

    public Move() {
        this.hops = new HashSet<>();
    }

    public Move(Character move, Coordinate from, Coordinate to) {
        this.move = move;
        this.from = from;
        this.to = to;
        this.hops = new LinkedHashSet<>();
    }

    public Coordinate getFrom() {
        return from;
    }

    public void setFrom(Coordinate from) {
        this.from = from;
    }

    public Coordinate getTo() {
        return to;
    }

    public void setTo(Coordinate to) {
        this.to = to;
    }

    public Character getMove() {
        return move;
    }

    public void setMove(Character move) {
        this.move = move;
    }

    public Set<Coordinate> getHops() {
        return hops;
    }

    public void setHops(Set<Coordinate> hops) {
        this.hops = hops;
    }

    public void addHop(Coordinate hop) {
        this.hops.add(hop);
    }

    @Override
    public String toString() {
        return "Move{" +
                "move=" + move +
                ", from=" + from +
                ", to=" + to +
                ", hops=" + hops +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;

        Move move1 = (Move) o;

        if (getMove() != null ? !getMove().equals(move1.getMove()) : move1.getMove() != null) return false;
        if (getFrom() != null ? !getFrom().equals(move1.getFrom()) : move1.getFrom() != null) return false;
        return getTo() != null ? getTo().equals(move1.getTo()) : move1.getTo() == null;
    }

    @Override
    public int hashCode() {
        int result = getMove() != null ? getMove().hashCode() : 0;
        result = 31 * result + (getFrom() != null ? getFrom().hashCode() : 0);
        result = 31 * result + (getTo() != null ? getTo().hashCode() : 0);
        return result;
    }
}
