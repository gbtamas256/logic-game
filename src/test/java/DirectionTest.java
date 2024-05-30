import org.junit.jupiter.api.Test;
import tiltingpuzzle.model.Direction;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

    @Test
    void of() {
        assertSame(Direction.UP, Direction.of(-1, 0));
        assertSame(Direction.RIGHT, Direction.of(0, 1));
        assertSame(Direction.DOWN, Direction.of(1, 0));
        assertSame(Direction.LEFT, Direction.of(0, -1));
    }

    @Test
    void ofShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Direction.of(0, 0));
    }
}
