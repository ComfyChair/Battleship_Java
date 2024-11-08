package battleship;

import battleship.BattleField.ShotResult;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BattleField battleField = new BattleField();
        battleField.printRevealed();
        readShipCoordinates(battleField);
        startGame(battleField);
    }

    private static void startGame(BattleField battleField) {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("The game starts!");
        battleField.printFogged();
        takeShots(battleField);
        System.out.println(" You won. Congratulations!");
    }

    private static void takeShots(BattleField battleField) {
        ShotResult result = null;
        System.out.println("Take a shot!\n");
        while (result != ShotResult.SINK_LAST) {
            Coordinate target = null;
            while (target == null) {
                Scanner scanner = new Scanner(System.in);
                target = Coordinate.fromString(scanner.next());
                if (target == null) {
                    System.out.println("Error! You entered an invalid coordinate. Try again:\n");
                }
            }
            result = battleField.tryShot(target);
            battleField.printFogged();
            System.out.print(result.feedback);
            if (result != ShotResult.SINK_LAST) {
                System.out.println("  Try again:\n");
            }
        }
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
                    System.out.println("Please enter star and end coordinate of the ship, e.g. A1 A3:");
                }
            }
        }
    }
}
