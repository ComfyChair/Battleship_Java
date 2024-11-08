package battleship;

import java.util.*;


class GameEngine {
    private static final int MAX_SHIPS = 5;
    String[][] board;
    List<Ship> ships = new ArrayList<>(MAX_SHIPS);

    GameEngine() {
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
        markOnBoard(newShip);
        return true;
    }

    private void markOnBoard(Ship ship) {
        for (Coordinate part : ship.parts) {
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

    public boolean tryShot(Coordinate target) {
        if (board[target.getRowIndex()][target.col()].equals(BoardContent.SHIP.symbol)) {
            board[target.getRowIndex()][target.col()] = BoardContent.HIT.symbol;
            return true;
        } else {
            board[target.getRowIndex()][target.col()] = BoardContent.MISS.symbol;
            return false;
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
