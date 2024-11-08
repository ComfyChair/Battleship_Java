package battleship;

import battleship.BattleField.ShotResult;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Player playerOne = new Player(1, getBattleField(1));
        clearScreenOnEnter();
        Player playerTwo = new Player(2, getBattleField(2));
        clearScreenOnEnter();
        startGame(new Player[]{playerOne, playerTwo});
    }

    private static void startGame(Player[] players) {
        int currentPlayerNo = 0;
        ShotResult result = null;
        while (result != ShotResult.SINK_LAST) {
            // print current battlefields
            Player otherPlayer = players[(currentPlayerNo + 1) % 2];
            otherPlayer.battleField.printFogged();
            System.out.println("----------------------");
            players[currentPlayerNo].battleField.printRevealed();
            // shoot
            result = shoot(currentPlayerNo, otherPlayer.battleField);
            System.out.println(result.feedback);
            if (result != ShotResult.SINK_LAST) {
                clearScreenOnEnter();
            }
            currentPlayerNo = (currentPlayerNo + 1) % 2;
        }
        System.out.println(" You won. Congratulations!");
    }

    private static ShotResult shoot(int playerNo, BattleField battleField) {
        System.out.printf("\nPlayer %d, it's your turn:\n", playerNo + 1);
        Coordinate target = null;
        while (target == null) {
            Scanner scanner = new Scanner(System.in);
            target = Coordinate.fromString(scanner.next());
            if (target == null) {
                System.out.println("Error! You entered an invalid coordinate. Try again:\n");
            }
        }
        return battleField.tryShot(target);
    }

    private static BattleField getBattleField(int playerNo) {
        BattleField battleField = new BattleField();
        System.out.printf("Player %d, place your ships on the game field %n%n", playerNo);
        battleField.printRevealed();
        readShipCoordinates(battleField);
        return battleField;
    }

    private static void readShipCoordinates(BattleField battleField) {
        Scanner scanner = new Scanner(System.in);
        for (ShipType shipType : ShipType.values()) {
            System.out.printf("\nEnter the coordinates of the %s (%d cells): %n", shipType.name, shipType.length);
            boolean success = false;
            while (!success) {
                String[] coordString = scanner.nextLine().split(" ");
                if (coordString.length == 2) {
                    Coordinate start = Coordinate.fromString(coordString[0]);
                    Coordinate end = Coordinate.fromString(coordString[1]);
                    success = battleField.setShip(shipType, start, end);
                } else {
                    System.out.println("Please enter start and end coordinate of the ship, e.g. A1 A3:");
                }
            }
        }
    }

    private static void clearScreenOnEnter() {
        System.out.println("Press Enter and pass the move to another player");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        for (int i = 0; i < 50; ++i) System.out.println();
    }

    record Player(int playerNo, BattleField battleField){}
}
