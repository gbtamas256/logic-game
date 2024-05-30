import org.junit.jupiter.api.Test;
import tiltingpuzzle.model.Direction;
import tiltingpuzzle.model.Position;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    Position position = new Position(0, 0);
    void assertPosition(int expectedRow, int expectedCol, Position position) {
        assertAll("position",
                () -> assertEquals(expectedRow, position.row()),
                () -> assertEquals(expectedCol, position.col())
        );
    }

    @Test
    void move() {
        assertPosition(position.row() - 1, position.col(), position.move(Direction.UP));
        assertPosition(position.row(), position.col() + 1, position.move(Direction.RIGHT));
        assertPosition(position.row() + 1, position.col(), position.move(Direction.DOWN));
        assertPosition(position.row(), position.col() - 1, position.move(Direction.LEFT));
    }
}
