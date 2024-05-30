package tiltingpuzzle.model;

/**
 * Placement of the wall objects in the game. They are represented with
 * the two positions that the wall is placed in between.
 *
 * @param position1 the position on one side of the wall
 * @param position2 the position on the other side of the wall
 */
public record Wall(Position position1, Position position2) {

}
