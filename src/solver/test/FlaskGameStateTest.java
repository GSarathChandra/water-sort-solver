package solver.test;

import java.util.List;

import org.junit.jupiter.api.Test;
import solver.base.FlaskGameState;
import solver.base.Move;

import static solver.base.Color.*;
import static org.junit.jupiter.api.Assertions.*;
import static solver.base.FlaskGameState.buildState;

public class FlaskGameStateTest {

    //TODO: Make all test inputs have 4 each of each colors.
    @Test
    public void testGetTopColorSize_onlyOneColorAndUnsolved(){
        FlaskGameState state = buildState(List.of(
                List.of(RED, RED)
        ));

        // Assert
        assertEquals(2, state.getTopColorSize(state.flasks.get(0)));
        assertFalse(state.isSolved());
    }

    @Test
    public void testGetTopColorSize_onlyOneColorAndSolved(){
        FlaskGameState state = buildState(List.of(
                List.of(RED, RED, RED, RED)
        ));

        // Assert
        assertEquals(4, state.getTopColorSize(state.flasks.get(0)));
        assertTrue(state.isSolved());
    }

    @Test
    public void testGetTopColorSize_twoColorsWithBothContiguous(){
        FlaskGameState state = buildState(List.of(
                List.of(PINK, PINK, RED, RED)
        ));

        // Assert
        assertEquals(2, state.getTopColorSize(state.flasks.get(0)));
        assertFalse(state.isSolved());
    }

    @Test
    public void testGetTopColorSize_threeColorsWithTopContiguous(){
        FlaskGameState state = buildState(List.of(
                List.of(ORANGE, PINK, RED, RED)
        ));

        // Assert
        assertEquals(2, state.getTopColorSize(state.flasks.get(0)));
        assertFalse(state.isSolved());
    }

    @Test
    public void testGetTopColorSize_threeColorsWithMiddleContiguous(){
        FlaskGameState state = buildState(List.of(
                List.of(ORANGE, PINK, PINK, RED)
        ));

        // Assert
        assertEquals(1, state.getTopColorSize(state.flasks.get(0)));
        assertFalse(state.isSolved());
    }

    @Test
    public void testGetTopColorSize_emptyFlaskTreatedAsSolved(){
        FlaskGameState state = buildState(List.of(
                List.of()
        ));

        // Assert
        assertEquals(0, state.getTopColorSize(state.flasks.get(0)));
        assertTrue(state.isSolved());
    }

    @Test
    public void testGetNextMoves_validOneToTwo(){
        FlaskGameState state = buildState(List.of(
                List.of(PINK, PINK, RED, RED),
                List.of(ORANGE, RED)
        ));
        List<Move> expectedMoves = List.of(new Move(2, RED, 0, 1));

        // Assert
        assertFalse(state.isSolved());
        assertIterableEquals(expectedMoves, state.getNextMoves());
    }

    @Test
    public void testGetNextMoves_noValidMoves_moveCausesOverflow(){
        FlaskGameState state = buildState(List.of(
                List.of(PINK, PINK, RED, RED),
                List.of(ORANGE, RED, RED)
        ));
        List<Move> expectedMoves = List.of();

        // Assert
        assertFalse(state.isSolved());
        assertIterableEquals(expectedMoves, state.getNextMoves());
    }

    @Test
    public void testGetNextMoves_validOneToTwo_overflowTwoToOne(){
        FlaskGameState state = buildState(List.of(
                List.of(PINK, PINK, RED),
                List.of(ORANGE, RED, RED)
        ));
        List<Move> expectedMoves = List.of(new Move(1, RED, 0, 1));

        // Assert
        assertFalse(state.isSolved());
        assertIterableEquals(expectedMoves, state.getNextMoves());
    }

    @Test
    public void testGetNextMoves_overFlowOneToTwo_validTwoToOne(){
        FlaskGameState state = buildState(List.of(
                List.of(PINK, RED, RED),
                List.of(ORANGE, ORANGE, RED)
        ));
        List<Move> expectedMoves = List.of(new Move(1, RED, 1, 0));

        // Assert
        assertFalse(state.isSolved());
        assertIterableEquals(expectedMoves, state.getNextMoves());
    }

    @Test
    public void testGetNextMoves_validBothWays(){
        FlaskGameState state = buildState(List.of(
                List.of(PINK, RED),
                List.of(ORANGE, RED)
        ));

        // Assert
        assertFalse(state.isSolved());
        if(state.removeReverseMoves()){
            assertIterableEquals(List.of(new Move(1, RED, 0, 1)), state.getNextMoves());
        } else {
            assertIterableEquals(List.of(new Move(1, RED, 0, 1), new Move(1, RED, 1, 0)), state.getNextMoves());
        }
    }

    @Test
    public void testGetNextMoves_moveToEmpty_valid(){
        FlaskGameState state = buildState(List.of(
                List.of(PINK, RED, RED),
                List.of()
        ));
        List<Move> expectedMoves = List.of(new Move(2, RED, 0, 1));

        // Assert
        assertFalse(state.isSolved());
        assertIterableEquals(expectedMoves, state.getNextMoves());
    }

    @Test
    public void testGetNextMoves_moveToEmpty_invalid_solvedElseWhere(){

        // When a color can be solved elsewhere, avoid moving to empty flask.
        // i.e., assertFalse(state.getNextMoves().contains("2@0->1"));
        FlaskGameState state = buildState(List.of(
                List.of(PINK, RED, RED),
                List.of(),
                List.of(RED, RED)
        ));
        List<Move> expectedMoves = List.of(new Move(2, RED, 0, 2));

        // Assert
        assertFalse(state.isSolved());
        assertIterableEquals(expectedMoves, state.getNextMoves());

    }

    @Test
    public void testGetNextMoves_disallowOnlyColorToEmpty(){
        // 'Move to empty' shouldn't be considered if top color is the only color in the flask.
        FlaskGameState state = buildState(List.of(
                List.of(PINK, PINK, PINK),
                List.of()
        ));
        List<Move> expectedMoves = List.of();

        // Assert
        assertFalse(state.isSolved());
        assertIterableEquals(expectedMoves, state.getNextMoves());
    }

    @Test
    public void testGetNextMoves_disallowFullToEmpty(){
        FlaskGameState state = buildState(List.of(
                List.of(PINK, PINK, PINK, PINK),
                List.of()
        ));
        List<Move> expectedMoves = List.of();

        // Assert
        assertTrue(state.isSolved());
        assertIterableEquals(expectedMoves, state.getNextMoves());
    }

    @Test
    public void testEquals_noEmptyFlasks_sameOrder(){
        FlaskGameState firstState = buildState(
                List.of(
                        List.of(PINK, PINK, RED, RED),
                        List.of(RED, RED, PINK, PINK)
                ));
        FlaskGameState secondState = buildState(
                List.of(
                        List.of(PINK, PINK, RED, RED),
                        List.of(RED, RED, PINK, PINK)
                ));

        // Assert
        assertEquals(firstState, secondState);
    }

    @Test
    public void testEquals_emptyFlasks_valid(){
        FlaskGameState firstState = buildState(
                List.of(
                        List.of(PINK, PINK, RED, RED),
                        List.of(RED, RED, PINK, PINK),
                        List.of()
                ));
        FlaskGameState secondState = buildState(
                List.of(
                        List.of(PINK, PINK, RED, RED),
                        List.of(),
                        List.of(RED, RED, PINK, PINK)
                ));

        // Assert
        assertEquals(firstState, secondState);
    }

    @Test
    public void testEquals_differentFlasks_valid(){
        FlaskGameState firstState = buildState(
                List.of(
                        List.of(PINK, PINK, RED, RED),
                        List.of(RED, RED, PINK, PINK),
                        List.of()
                ));
        FlaskGameState secondState = buildState(
                List.of(
                        List.of(GRAY, GRAY, RED, RED),
                        List.of(),
                        List.of(RED, RED, GRAY, GRAY)
                ));

        // Assert
        assertNotEquals(firstState, secondState);
    }
}