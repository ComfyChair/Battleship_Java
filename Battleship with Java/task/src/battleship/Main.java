package battleship;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine();
        gameEngine.printRevealed();
        readShipCoordinates(gameEngine);
        startGame(gameEngine);
    }

    private static void startGame(GameEngine gameEngine) {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("The game starts!");
        gameEngine.printFogged();
        System.out.println("Take a shot!");
        Coordinate target = null;
        while (target == null) {
            Scanner scanner = new Scanner(System.in);
            target = Coordinate.fromString(scanner.next());
            if (target == null) {
                System.out.println("Error! You entered an invalid coordinate! Try again:");
            }
        }
        boolean isHit = gameEngine.tryShot(target);
        gameEngine.printFogged();
        String feedback = isHit ? "You hit a ship!" : "You missed!";
        System.out.println(feedback);
        gameEngine.printRevealed();
    }

    private static void readShipCoordinates(GameEngine gameEngine) {
        Scanner scanner = new Scanner(System.in);
        for (ShipType shipType : ShipType.values()) {
            System.out.printf("\nEnter the coordinates of the %s (%d cells): %n", shipType.name, shipType.length);
            boolean success = false;
            while (!success) {
                String[] coordString = scanner.nextLine().split(" ");
                Coordinate start = Coordinate.fromString(coordString[0]);
                Coordinate end = Coordinate.fromString(coordString[1]);
                success = gameEngine.setShip(shipType, start, end);
            }
        }
    }
}
