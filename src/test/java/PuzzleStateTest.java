import org.junit.jupiter.api.Test;
import tiltingpuzzle.model.Direction;
import tiltingpuzzle.model.Position;
import tiltingpuzzle.model.PuzzleState;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class PuzzleStateTest {

    PuzzleState initialState = new PuzzleState(); // the original initial state

    PuzzleState winningState = new PuzzleState(new Position(3, 4),
            new Position(0, 4),
            new Position(3, 4),
            new Position(0, 4)); // the goal state

    PuzzleState randomState = new PuzzleState(new Position(2, 0),
            new Position(1, 0),
            new Position(3, 4),
            new Position(0, 4)); // a non-goal state


    @Test
    void constructor() {
        PuzzleState state = new PuzzleState();
        for (var i = 0; i < 4; i++) {
            assertEquals(initialState.getPosition(i), state.getPosition(i));
        }
    }

    @Test
    void constructorWithParameters() {
        var positions = new Position[] {new Position(2, 4),
                new Position(1, 4),
                new Position(3, 4),
                new Position(0, 4)
        };
        PuzzleState state = new PuzzleState(positions);
        for (var i = 0; i < 4; i++) {
            assertEquals(positions[i], state.getPosition(i));
        }
    }

    @Test
    void isSolved() {
        assertFalse(initialState.isSolved());
        assertTrue(winningState.isSolved());
        assertFalse(randomState.isSolved());
    }

    @Test
    void isLegalMoveInitialState() {
        assertTrue(initialState.isLegalMove(Direction.UP));
        assertTrue(initialState.isLegalMove(Direction.RIGHT));
        assertTrue(initialState.isLegalMove(Direction.DOWN));
        assertTrue(initialState.isLegalMove(Direction.LEFT));
    }

    @Test
    void isLegalMoveWinningState() {
        assertTrue(winningState.isLegalMove(Direction.UP));
        assertTrue(winningState.isLegalMove(Direction.RIGHT));
        assertTrue(winningState.isLegalMove(Direction.DOWN));
        assertTrue(winningState.isLegalMove(Direction.LEFT));
    }

    @Test
    void isLegalMoveRandomState() {
        assertFalse(randomState.isLegalMove(Direction.UP));
        assertTrue(randomState.isLegalMove(Direction.RIGHT));
        assertFalse(randomState.isLegalMove(Direction.DOWN));
        assertTrue(randomState.isLegalMove(Direction.LEFT));
    }

    @Test
    void makeMoveLeftInitialState() {
        var state = initialState.clone();
        state.makeMove(Direction.LEFT);
        assertEquals(state.getPosition(0), initialState.getPosition(0).move(Direction.LEFT)
                .move(Direction.LEFT)
                .move(Direction.LEFT)
                .move(Direction.LEFT));
        assertEquals(state.getPosition(1), initialState.getPosition(1).move(Direction.LEFT)
                .move(Direction.LEFT)
                .move(Direction.LEFT)
                .move(Direction.LEFT));
        assertEquals(initialState.getPosition(2), state.getPosition(2));
        assertEquals(initialState.getPosition(3), state.getPosition(3));
    }

    @Test
    void makeMoveDownInitialState() {
        var state = initialState.clone();
        state.makeMove(Direction.DOWN);
        assertEquals(state.getPosition(0), initialState.getPosition(0).move(Direction.DOWN)
                .move(Direction.DOWN));
        assertEquals(state.getPosition(1), initialState.getPosition(1));
        assertEquals(initialState.getPosition(2), state.getPosition(2));
        assertEquals(initialState.getPosition(3), state.getPosition(3));
    }

    @Test
    void makeMoveUpRandomState() {
        var state = randomState.clone();
        assertThrows(IllegalArgumentException.class, () -> state.makeMove(Direction.UP));
    }

    @Test
    void getLegalMoves() {
        assertEquals(EnumSet.allOf(Direction.class), initialState.getLegalMoves());
        assertEquals(EnumSet.allOf(Direction.class), winningState.getLegalMoves());
        assertEquals(EnumSet.of(Direction.LEFT, Direction.RIGHT), randomState.getLegalMoves());
    }

    @Test
    void testClone() {
        var clonedState = initialState.clone();
        assertTrue(clonedState.equals(initialState));
        assertNotSame(clonedState, initialState);
    }

    @Test
    void testEquals() {
        assertTrue(initialState.equals(initialState));

        var clonedState = initialState.clone();
        clonedState.makeMove(Direction.LEFT);
        assertFalse(clonedState.equals(initialState));

        assertFalse(initialState.equals(null));
        assertFalse(initialState.equals("string"));
        assertFalse(initialState.equals(2));
        assertFalse(initialState.equals(randomState));
    }

    @Test
    void testHashCode() {
        assertTrue(initialState.hashCode() == initialState.hashCode());
        assertTrue(initialState.hashCode() == initialState.clone().hashCode());
    }

    @Test
    void testToString() {
        assertEquals("[Position[row=2, col=4],Position[row=1, col=4],Position[row=3, col=4],Position[row=0, col=4]]"
                , initialState.toString());
        assertEquals("[Position[row=3, col=4],Position[row=0, col=4],Position[row=3, col=4],Position[row=0, col=4]]"
                , winningState.toString());
        assertEquals("[Position[row=2, col=0],Position[row=1, col=0],Position[row=3, col=4],Position[row=0, col=4]]"
                , randomState.toString());
    }
}