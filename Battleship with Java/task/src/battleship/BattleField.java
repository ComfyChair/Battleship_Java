package battleship;

import java.util.*;
import java.util.logging.Logger;


class BattleField {
    Logger logger = Logger.getLogger(BattleField.class.getName());
    private String[][] board;
    private final List<Ship> ships = new ArrayList<>();
    private final Map<Coordinate, Ship> shipCoordinates = new HashMap<>();

    BattleField() {
        initBoard();
    }

    private void initBoard() {
        board = new String[11][11];
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j == 0) {
                    // corner piece first row = empty
                    board[i][j] = " ";
                } else if (i == 0) {
                    // column numbering 1-10
                    board[i][j] = String.valueOf(j);
                } else if (j == 0) {
                    // row "numbering" A-J
                    board[i][j] = Character.toString('A' + i - 1);
                } else {
                    board[i][j] = BoardContent.WATER.symbol;
                }
            }
        }
    }

    void printFogged() {
        System.out.println();
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (board[i][j].equals(BoardContent.WATER.symbol)
                        || board[i][j].equals(BoardContent.SHIP.symbol)) {
                    System.out.print(BoardContent.WATER.symbol + " ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    void printRevealed() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    boolean setShip(ShipType shipType, Coordinate start, Coordinate end) {
        if (!isLocationOk(start, end)) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        }
        int length = 1 + Math.max(Math.abs(start.row() - end.row()), Math.abs(start.col() - end.col()));
        if (!isLengthOk(length, shipType)) {
            System.out.printf("Error! Wrong length of the %s! Try again:\n", shipType.name);
            return false;
        }
        Ship newShip = new Ship(shipType, start, end);
        if (newShip.collides(ships)) {
            System.out.println("Error! Collision! Try again:");
            return false;
        }
        ships.add(newShip);
        newShip.getParts().forEach(part -> shipCoordinates.put(part, newShip));
        logger.info(shipCoordinates.toString());
        markOnBoard(newShip);
        return true;
    }

    private void markOnBoard(Ship ship) {
        for (Coordinate part : ship.getParts()) {
            board[(part.getRowIndex())][part.col()] = BoardContent.SHIP.symbol;
        }
        printRevealed();
    }

    private boolean isLengthOk(int length, ShipType shipType) {
        return length == shipType.length;
    }

    private boolean isLocationOk(Coordinate start, Coordinate end) {
        return start != null && end != null && start.isInLineWith(end);
    }

    public ShotResult tryShot(Coordinate target) {
        ShotResult feedback;
        String cellContent = board[target.getRowIndex()][target.col()];
        if (cellContent.equals(BoardContent.WATER.symbol) || cellContent.equals(BoardContent.MISS.symbol)) {
            board[target.getRowIndex()][target.col()] = BoardContent.MISS.symbol;
            feedback = ShotResult.MISS;
        } else {
            board[target.getRowIndex()][target.col()] = BoardContent.HIT.symbol;
            Ship hitShip = shipCoordinates.get(target);
            boolean isAfloat = hitShip.setDamaged(target);
            if (isAfloat) {
                feedback = ShotResult.HIT;
            } else {
                ships.remove(hitShip);
                if (ships.isEmpty()) {
                    feedback = ShotResult.SINK_LAST;
                } else {
                    feedback = ShotResult.SINK;
                }
            }
        }
        return feedback;
    }

    enum ShotResult {
        HIT("You hit a ship!"),
        MISS("You missed!"),
        SINK("You sank a ship!"),
        SINK_LAST("You sank the last ship.");
        final String feedback;
        ShotResult(String feedback) {
            this.feedback = feedback;
        }
    }

    enum BoardContent {
        WATER("~"), SHIP("O"), MISS("M"), HIT("X");
        final String symbol;
        BoardContent(String symbol){
            this.symbol = symbol;
        }
    }
}
