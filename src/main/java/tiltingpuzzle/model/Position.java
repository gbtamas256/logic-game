package tiltingpuzzle.model;

/**
 * Represents a 2D position.
 *
 * @param row the row part of the coordinate
 * @param col the column part of the coordinate
 */
public record Position(int row, int col) {

    /**
     * {@return the new position after moving one in the given direction}
     *
     * @param direction a direction that specifies a change in the coordinates
     */
    public Position move(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }
}
