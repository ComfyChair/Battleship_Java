package battleship;

import java.util.List;
import java.util.logging.Logger;

class Ship {
    Logger logger = Logger.getLogger(Ship.class.getName());
    ShipType shipType;
    Coordinate[] parts;

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
        assert(length == shipType.length);
        parts = new Coordinate[length];
        if (start.row() < end.row()) {
            for (int i = 0; i < length; i++) {
                parts[i] = new Coordinate((char) (start.row() + i), start.col());
            }
        } else {
            for (int i = 0; i < length; i++) {
                parts[i] = new Coordinate((char) (start.row() - i), start.col());
            }
        }
    }

    private void initHorizontally(Coordinate start, Coordinate end) {
        int length = 1 + Math.abs(start.col() - end.col());
        assert(length == shipType.length);
        parts = new Coordinate[length];
        if (start.col() < end.col()) {
            for (int i = 0; i < length; i++) {
                parts[i] = new Coordinate(start.row(), start.col() + i);
            }
        } else {
            for (int i = 0; i < length; i++) {
                parts[i] = new Coordinate(start.row(), start.col() - i);
            }
        }
    }

    boolean collides(List<Ship> shipList) {
        for (Ship otherShip : shipList) {
            for (Coordinate otherPart : otherShip.parts) {
                for (Coordinate part: parts) {
                    if (part.equals(otherPart) || part.adjacent(otherPart)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Coordinate part : parts) {
            result.append(part).append(" ");
        }
        return  result.toString();
    }

}
