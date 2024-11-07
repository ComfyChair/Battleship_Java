package battleship;

record Coordinate(Character row, int col) {
    Boolean exists() {
        return row >= 'A' && row <= 'J' && col >= 1 && col <= 10;
    }

    int getRowIndex() {
        return row - 'A' + 1;
    }

    Boolean isInLineWith(Coordinate other) {
        return other.row == row || other.col == col;
    }

    public boolean isAdjacent(Coordinate other) {
        return (Math.abs(row - other.row) == 1 && col == other.col) ||
                (Math.abs(col - other.col) == 1 && row == other.row);
    }

    static Coordinate fromString(String inString) {
        Character row = inString.charAt(0);
        int col;
        try {
            col = Integer.parseInt(inString.substring(1));
            Coordinate coordinate = new Coordinate(row, col);
            if (coordinate.exists()) {
                return new Coordinate(row, col);
            }
        } catch (Exception e) {
            System.out.println("Sorry mate, not a coordinate: " + inString);
        }
        return null;
    }

    @Override
    public String toString() {
        return row + String.valueOf(col);
    }
}
