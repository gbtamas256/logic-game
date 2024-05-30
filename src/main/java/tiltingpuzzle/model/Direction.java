package tiltingpuzzle.model;

/**
 * Represents the four directions the balls can move in.
 */
public enum Direction {

    UP(-1, 0),
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, -1);

    private final int rowChange;
    private final int colChange;

    Direction(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    /**
     * {@return the change in the row coordinate when moving in chosen the direction}
     */
    public int getRowChange() {
        return rowChange;
    }

    /**
     * {@return the change in the column coordinate when moving in chosen the direction}
     */
    public int getColChange() {
        return colChange;
    }

    /**
     * {@return the direction according to the coordinate changes}
     *
     * @param rowChange the change in the row coordinate
     * @param colChange the change in the column coordinate
     * @throws IllegalArgumentException if rowChange and colChange doesn't add up to one defined directions
     */

    public static Direction of(int rowChange, int colChange) {
        for (var direction : values()) {
            if (direction.rowChange == rowChange && direction.colChange == colChange) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }
}
