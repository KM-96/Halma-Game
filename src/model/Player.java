package model;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private Set<Coordinate> positions;

    private Character color;

    public Player(Character color) {
        this.color = color;
        this.positions = new HashSet<>();
    }

    public Set<Coordinate> getPositions() {
        return positions;
    }

    public void addPosition(Coordinate position) {
        this.positions.add(position);
    }

    public Character getColor() {
        return color;
    }

    public void setColor(Character color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (getPositions() != null ? !getPositions().equals(player.getPositions()) : player.getPositions() != null)
            return false;
        return getColor() != null ? getColor().equals(player.getColor()) : player.getColor() == null;
    }

    @Override
    public int hashCode() {
        int result = getPositions() != null ? getPositions().hashCode() : 0;
        result = 31 * result + (getColor() != null ? getColor().hashCode() : 0);
        return result;
    }
}
