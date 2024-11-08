package battleship;

import java.util.*;

class Ship {
    private final ShipType shipType;
    /**
     * Map representing state of ship parts: true means functional, false damaged
     */
    private final Map<Coordinate, Boolean> parts = new HashMap<>();

    Ship(ShipType shipType, Coordinate start, Coordinate end) {
        this.shipType = shipType;
        initParts(start, end);
    }

    private void initParts(Coordinate start, Coordinate end) {
        if (start.row() == end.row()) {
            initHorizontally(start, end);
        } else {
            initVertically(start, end);
        }
    }

    private void initVertically(Coordinate start, Coordinate end) {
        int length = 1 + Math.abs(start.row() - end.row());
        assert (length == shipType.length);
        if (start.row() < end.row()) {
            for (int i = 0; i < length; i++) {
                parts.put(new Coordinate((char) (start.row() + i), start.col()), true);
            }
        } else {
            for (int i = 0; i < length; i++) {
                parts.put(new Coordinate((char) (start.row() - i), start.col()), true);
            }
        }
    }

    private void initHorizontally(Coordinate start, Coordinate end) {
        int length = 1 + Math.abs(start.col() - end.col());
        assert (length == shipType.length);
        if (start.col() < end.col()) {
            for (int i = 0; i < length; i++) {
                parts.put(new Coordinate(start.row(), start.col() + i), true);
            }
        } else {
            for (int i = 0; i < length; i++) {
                parts.put(new Coordinate(start.row(), start.col() - i), true);
            }
        }
    }

    boolean collides(List<Ship> shipSet) {
        for (Ship otherShip : shipSet) {
            for (Coordinate otherPart : otherShip.parts.keySet()) {
                for (Coordinate part : parts.keySet()) {
                    if (part.equals(otherPart) || part.isAdjacent(otherPart)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Set<Coordinate> getParts() {
        return parts.keySet();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Ship(");
        for (Coordinate part : parts.keySet()) {
            result.append(part).append(" ");
        }
        result.append(")");
        return result.toString();
    }

    /**
     * @param target coordinate where the ship was hit
     * @return true if any part of the ship is undamaged, false otherwise
     */
    public boolean setDamaged(Coordinate target) {
        parts.replace(target, false);
        return parts.containsValue(true);
    }
}
