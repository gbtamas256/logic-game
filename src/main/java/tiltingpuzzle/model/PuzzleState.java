package tiltingpuzzle.model;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import puzzle.State;

import java.util.*;

/**
 * State representation of the puzzle.
 */
public class PuzzleState implements State<Direction> {

    /**
     * Size of the board.
     */
    public static final int BOARD_SIZE = 5;

    /**
     * Index of the red ball.
     */
    public static final int RED_BALL = 0;

    /**
     * Index of the blue ball.
     */
    public static final int BLUE_BALL = 1;

    /**
     * Index of the red square.
     */
    public static final int RED_SQUARE = 2;

    /**
     * Index of the blue square.
     */
    public static final int BLUE_SQUARE = 3;

    private final ReadOnlyObjectWrapper<Position>[] positions;

    private final ReadOnlyBooleanWrapper solved;

    static final private ArrayList<Wall> walls = new ArrayList<>(
            Arrays.asList(new Wall(new Position(0, 1), new Position(0, 2)),
                    new Wall(new Position(0, 2), new Position(1, 2)),
                    new Wall(new Position(1, 4), new Position(2, 4)),
                    new Wall(new Position(2, 0), new Position(3, 0)),
                    new Wall(new Position(3, 2), new Position(4, 2)),
                    new Wall(new Position(4, 2), new Position(4, 3))));

    /**
     * Creates a {@code PuzzleState} object that is the initial state of the puzzle.
     */
    public PuzzleState() {
        this(new Position(2, 4),
                new Position(1, 4),
                new Position(3, 4),
                new Position(0, 4));
    }

    /**
     * Creates a {@code PuzzleState} object according to the specified positions. It expects an array of four
     * {@code Position} objects or four {@code Position} objects.
     *
     * @param positions the positions of the pieces
     */
    public PuzzleState(Position... positions) {
        checkPositions(positions);
        this.positions = new ReadOnlyObjectWrapper[4];
        for (var i = 0; i < 4; i++) {
            this.positions[i] = new ReadOnlyObjectWrapper<>(positions[i]);
        }
        solved = new ReadOnlyBooleanWrapper();
        solved.bind(this.positions[RED_BALL].isEqualTo(this.positions[RED_SQUARE])
                .and(this.positions[BLUE_BALL].isEqualTo(this.positions[BLUE_SQUARE])));
    }

    private void checkPositions(Position[] positions) {
        if (positions.length != 4) {
            throw new IllegalArgumentException();
        }
        for (var position : positions) {
            if (!isOnBoard(position)) {
                throw new IllegalArgumentException();
            }
        }
        if (positions[RED_BALL].equals(positions[BLUE_BALL])) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * {@return the position of the specified piece}
     *
     * @param index the index number of the piece you want
     */
    public Position getPosition(int index) {
        if (index > 3 || index < 0) {
            throw new IllegalArgumentException();
        }
        return positions[index].get();
    }

    public ReadOnlyObjectProperty<Position> positionProperty(int index) {
        return positions[index].getReadOnlyProperty();
    }

    private boolean isOnBoard(Position position) {
        return position.row() >= 0 && position.row() < BOARD_SIZE &&
                position.col() >= 0 && position.col() < BOARD_SIZE;
    }

    /**
     * {@return whether the puzzle is solved}
     */
    @Override
    public boolean isSolved() {
        return solved.get();
    }

    public ReadOnlyBooleanProperty solvedProperty() {
        return solved.getReadOnlyProperty();
    }

    /**
     * {@return whether moving in the specified direction will end up in a legal state.
     * It is illegal if the balls would collide.}
     *
     * @param direction the intended direction to move the balls in
     */
    @Override
    public boolean isLegalMove(Direction direction) {
        Position redEndPosition = new Position(getPosition(0).row(), getPosition(0).col());
        Position blueEndPosition = new Position(getPosition(1).row(), getPosition(1).col());
        while (!isBlocked(redEndPosition, redEndPosition.move(direction))) {
            redEndPosition = redEndPosition.move(direction);
        }
        while (!isBlocked(blueEndPosition, blueEndPosition.move(direction))) {
            blueEndPosition = blueEndPosition.move(direction);
        }
        return !redEndPosition.equals(blueEndPosition);
    }

    private boolean isBlocked(Position from, Position to) {
        if (from.equals(to)){
            throw new IllegalArgumentException();
        }
        if (!isOnBoard(to)) {
            return true;
        }
        for (var wall : walls) {
            if ((wall.position1().equals(from) || wall.position1().equals(to))
                    && (wall.position2().equals(from) || wall.position2().equals(to))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves the balls in the specified direction, if the move is legal.
     *
     * @param direction the direction the balls would move in
     * @throws IllegalArgumentException if the balls would collide
     */
    @Override
    public void makeMove(Direction direction) {
        if (isLegalMove(direction)) {
            switch (direction) {
                case UP -> moveBall(RED_BALL, Direction.UP);
                case RIGHT -> moveBall(RED_BALL, Direction.RIGHT);
                case DOWN -> moveBall(RED_BALL, Direction.DOWN);
                case LEFT -> moveBall(RED_BALL, Direction.LEFT);
            }
            switch (direction) {
                case UP -> moveBall(BLUE_BALL, Direction.UP);
                case RIGHT -> moveBall(BLUE_BALL, Direction.RIGHT);
                case DOWN -> moveBall(BLUE_BALL, Direction.DOWN);
                case LEFT -> moveBall(BLUE_BALL, Direction.LEFT);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void moveBall(int index, Direction direction) {
        Position newPosition = getPosition(index);
        while (!isBlocked(newPosition, newPosition.move(direction))) {
            newPosition = newPosition.move(direction);
        }
        positions[index].set(newPosition);
    }

    /**
     * {@return The set of directions it is legal to move in}
     */
    @Override
    public Set<Direction> getLegalMoves() {
        EnumSet legalMoves = EnumSet.noneOf(Direction.class);
        for (var direction: Direction.values()) {
            if (isLegalMove(direction)) {
                legalMoves.add(direction);
            }
        }
        return legalMoves;
    }

    /**
     * {@return a new {@code PuzzleState} with the same positions}
     */
    @Override
    public PuzzleState clone() {
        return new PuzzleState(getPosition(RED_BALL), getPosition(BLUE_BALL),
                getPosition(RED_SQUARE), getPosition(BLUE_SQUARE));
    }

    /**
     * Checks whether the objects are equal
     *
     * {@return a boolean according to the objects being equal}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof PuzzleState other)
                && getPosition(RED_BALL).equals(other.getPosition(RED_BALL))
                && getPosition(BLUE_BALL).equals(other.getPosition(BLUE_BALL))
                && getPosition(RED_SQUARE).equals(other.getPosition(RED_SQUARE))
                && getPosition(BLUE_SQUARE).equals(other.getPosition(BLUE_SQUARE));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition(RED_BALL), getPosition(BLUE_BALL),
                getPosition(RED_SQUARE), getPosition(BLUE_SQUARE));
    }

    /**
     * {@return a formatted version of the positions of the pieces}
     */
    @Override
    public String toString() {
        var sj = new StringJoiner(",", "[", "]");
        for (var i = 0; i < 4; i++) {
            sj.add(getPosition(i).toString());
        }
        return sj.toString();
    }
}
