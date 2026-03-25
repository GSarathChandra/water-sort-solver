package solver.test;

import org.junit.jupiter.api.Test;
import solver.base.Move;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {

    @Test
    public void testMoveConstructorAndGetters() {
        String moveStr = "1@0->1";
        Move move = new Move(moveStr);
        assertEquals(moveStr, move.getMove());
    }

    @Test
    public void testSetMove() {
        Move move = new Move("1@0->1");
        move.setMove("2@1->0");
        assertEquals("2@1->0", move.getMove());
    }

    @Test
    public void testGetShortVersion() {
        Move move = new Move("1@0->1");
        // topColorSize, fromFlaskIndex, toFlaskIndex are all 0 by default for now
        // because they are not set in the constructor yet (TODO in Move.java)
        assertEquals("0@0->0", move.getShortVersion());
    }

    @Test
    public void testGetVerboseVersion() {
        Move move = new Move("1@0->1");
        // topColor is null by default
        assertEquals("null from 0 to 0\n", move.getVerboseVersion());
    }

    @Test
    public void testGetUndoMove() {
        String moveStr = "1@0->1";
        Move move = new Move(moveStr);
        Move undoMove = move.getUndoMove();
        assertEquals(moveStr, undoMove.getMove());
    }

    @Test
    public void testToString() {
        Move move = new Move("1@0->1");
        assertEquals("Move{move='1@0->1'}", move.toString());
    }
}
