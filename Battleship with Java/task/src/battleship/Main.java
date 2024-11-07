package battleship;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        game.printBoard();
        getShipCoordinates(game);
    }

    private static void getShipCoordinates(Game game) {
        Scanner scanner = new Scanner(System.in);
        for (ShipType shipType : ShipType.values()) {
            System.out.printf("Enter the coordinates of the %s (%d cells): %n", shipType.name, shipType.length);
            boolean success = false;
            while (!success) {
                String[] coordinates = scanner.nextLine().split(" ");
                success = game.setShip(shipType, coordinates);
            }
        }

    }
}
