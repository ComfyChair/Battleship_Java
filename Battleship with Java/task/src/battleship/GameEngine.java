package battleship;

import java.util.*;


class GameEngine {
    private static final int MAXSHIPS = 5;
    String[][] board;
    List<Ship> ships = new ArrayList<>(MAXSHIPS);
    Set<Coordinate> shipPositions = new HashSet<>();
    Set<Coordinate> receivedShots = new HashSet<>();

    GameEngine() {
        initBoard();
    }

    private void initBoard() {
        board = new String[11][11];
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j == 0) {
                    board[i][j] = " ";
                } else if (i == 0) {
                    board[i][j] = String.valueOf(j);
                } else if (j == 0) {
                    board[i][j] = Character.toString('A' + i - 1);
                } else {
                    board[i][j] = "~";
                }
            }
        }
    }

    void printBoard() {
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
        shipPositions.addAll(Arrays.asList(newShip.parts));
        markOnBoard(newShip);
        return true;
    }

    private void markOnBoard(Ship ship) {
        for (Coordinate part : ship.parts) {
            board[(part.getRowIndex())][part.col()] = "O";
        }
        printBoard();
    }

    private boolean isLengthOk(int length, ShipType shipType) {
        return length == shipType.length;
    }

    private boolean isLocationOk(Coordinate start, Coordinate end) {
        return start != null && end != null && start.isInLineWith(end);
    }

    public boolean tryShot(Coordinate target) {
        receivedShots.add(target);
        if (shipPositions.contains(target)) {
            board[target.getRowIndex()][target.col()] = "X";
            return true;
        } else {
            board[target.getRowIndex()][target.col()] = "M";
            return false;
        }
    }
}