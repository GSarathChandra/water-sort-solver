package solver.test;

import org.junit.jupiter.api.Test;
import solver.base.Move;
import solver.base.Color;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {

    @Test
    public void testMoveConstructorAndGetters() {
        Move move = new Move(1, Color.RED, 0, 1);
        assertEquals(1, move.getTopColorSize());
        assertEquals(Color.RED, move.getTopColor());
        assertEquals(0, move.getFromFlaskIndex());
        assertEquals(1, move.getToFlaskIndex());
    }

    @Test
    public void testGetShortVersion() {
        Move move = new Move(1, Color.RED, 0, 1);
        assertEquals("1@0->1", move.getShortVersion());
    }

    @Test
    public void testGetVerboseVersion() {
        Move move = new Move(1, Color.RED, 0, 1);
        assertEquals("RED from 0 to 1", move.getVerboseVersion());
    }

    @Test
    public void testGetUndoMove() {
        Move move = new Move(1, Color.RED, 0, 1);
        Move undoMove = move.getUndoMove();
        assertEquals(1, undoMove.getTopColorSize());
        assertEquals(Color.RED, undoMove.getTopColor());
        assertEquals(1, undoMove.getFromFlaskIndex());
        assertEquals(0, undoMove.getToFlaskIndex());
    }

    @Test
    public void testToString() {
        Move move = new Move(1, Color.RED, 0, 1);
        assertEquals("1@0->1", move.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        Move move1 = new Move(1, Color.RED, 0, 1);
        Move move2 = new Move(1, Color.RED, 0, 1);
        Move move3 = new Move(2, Color.RED, 0, 1);

        assertEquals(move1, move2);
        assertNotEquals(move1, move3);
        assertEquals(move1.hashCode(), move2.hashCode());
        assertNotEquals(move1.hashCode(), move3.hashCode());
    }
}
