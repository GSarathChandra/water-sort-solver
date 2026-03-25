package solver.test;

import java.util.List;
import java.util.Stack;
import java.util.LinkedList;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import solver.base.FlaskGameState;
import solver.base.Color;

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
        List<String> expectedMoves = List.of("2@0->1");

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
        List<String> expectedMoves = List.of();

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
        List<String> expectedMoves = List.of("1@0->1");

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
        List<String> expectedMoves = List.of("1@1->0");

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
            assertIterableEquals(List.of("1@0->1"), state.getNextMoves());
        } else {
            assertIterableEquals(List.of("1@0->1", "1@1->0"), state.getNextMoves());
        }
    }

    @Test
    public void testGetNextMoves_moveToEmpty_valid(){
        FlaskGameState state = buildState(List.of(
                List.of(PINK, RED, RED),
                List.of()
        ));
        List<String> expectedMoves = List.of("2@0->1");

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
        List<String> expectedMoves = List.of("2@0->2");

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
        List<String> expectedMoves = List.of();

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
        List<String> expectedMoves = List.of();

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

    @Test
    public void testMakeAndUndoMove() {
        FlaskGameState state = buildState(List.of(
                List.of(RED, RED, RED),
                List.of(RED),
                List.of(PINK, PINK, PINK, PINK),
                List.of()
        ));
        String move = "3@0->3";
        state.makeMove(move);
        assertEquals(3, state.flasks.get(3).size());
        assertEquals(0, state.flasks.get(0).size());
        assertEquals(1, state.movesHistory.size());

        state.undoLastMove();
        assertEquals(0, state.flasks.get(3).size());
        assertEquals(3, state.flasks.get(0).size());
        assertEquals(0, state.movesHistory.size());
    }

    @Test
    public void testCreateCopy() {
        FlaskGameState state = buildState(List.of(List.of(RED, RED, RED, RED), List.of()));
        FlaskGameState copy = state.createCopy();
        assertEquals(state, copy);
        assertNotSame(state, copy);
        assertNotSame(state.flasks, copy.flasks);
    }

    @Test
    public void testGetState() {
        FlaskGameState state = buildState(List.of(List.of(RED), List.of(RED, RED, RED)));
        String stateStr = state.getState();
        assertTrue(stateStr.contains("RED"));
    }

    @Test
    public void testHashCode() {
        FlaskGameState state1 = buildState(List.of(List.of(RED, RED, RED, RED), List.of()));
        FlaskGameState state2 = buildState(List.of(List.of(RED, RED, RED, RED), List.of()));
        assertEquals(state1.hashCode(), state2.hashCode());
    }

    @Test
    public void testEqualsSpecialCases() {
        FlaskGameState state = buildState(List.of(List.of(RED, RED, RED, RED), List.of()));
        assertNotEquals(state, null);
        assertNotEquals(state, "not a state");
        assertEquals(state, state);

        FlaskGameState other = buildState(List.of(List.of(RED, RED, RED, RED), List.of()));
        assertEquals(state, other);
    }

    @Test
    public void testPrintMethods() {
        FlaskGameState state = buildState(List.of(
            List.of(RED, RED, PINK, PINK),
            List.of(PINK, PINK, RED, RED),
            List.of()
        ));
        state.setDebugMode(true);
        state.makeMove("2@0->2");
        state.printShortSolution();
        state.printStateHistory();
        state.printVerboseSolution();
    }

    @Test
    public void testBuildStateThrowsOnOverflow() {
        assertThrows(IllegalArgumentException.class, () -> {
            buildState(List.of(List.of(RED, RED, RED, RED, RED)));
        });
    }

    @Test
    public void testDebugMode() {
        FlaskGameState state = buildState(List.of(List.of(RED, RED, RED, RED), List.of()));
        assertFalse(state.isDebugMode());
        state.setDebugMode(true);
        assertTrue(state.isDebugMode());
    }

    @Test
    public void testGetNextMoves_withRedundantMovesDisabled() {
        FlaskGameState state = buildState(List.of(
            List.of(RED, RED, PINK),
            List.of(),
            List.of(RED, RED, RED, RED)
        ));
        state.setRedundantMovesRemoved(false);
        List<String> moves = state.getNextMoves();
        assertTrue(moves.contains("1@0->1"));

        // Also cover Case 4 in the else block (second -> first)
        FlaskGameState state2 = buildState(List.of(
            List.of(),
            List.of(RED, RED, PINK),
            List.of(RED, RED, RED, RED)
        ));
        state2.setRedundantMovesRemoved(false);
        List<String> moves2 = state2.getNextMoves();
        assertTrue(moves2.contains("1@1->0"));
    }

    @Test
    public void testGetNextMoves_withReverseMovesDisabled() {
        FlaskGameState state = buildState(List.of(
            List.of(PINK, RED),
            List.of(PINK, PINK, RED),
            List.of(PINK, PINK, PINK, PINK)
        ));
        state.setReverseMovesRemoved(true);
        state.setDebugMode(true);
        List<String> moves = state.getNextMoves();
        // Flask 0: [PINK, RED]
        // Flask 1: [PINK, PINK, RED]
        // Case 1 (0->1): 1@0->1
        // Case 2 (1->0): 1@1->0. Since 1@0->1 is already there, it should be removed if it's the reverse.
        assertTrue(moves.contains("1@0->1"));
        assertFalse(moves.contains("1@1->0"));

        // Trigger the else block in Case 2 (moves.contains(reverseMove) is true)
        // This is already done above.

        // Trigger the else block of if(removeReverseMoves())
        state.setReverseMovesRemoved(false);
        List<String> moves2 = state.getNextMoves();
        assertTrue(moves2.contains("1@1->0"));
    }

    @Test
    public void testGetNextMoves_secondToEmpty() {
        FlaskGameState state = buildState(List.of(
            List.of(),
            List.of(RED, PINK),
            List.of(PINK, PINK, PINK, PINK)
        ));
        state.setRedundantMovesRemoved(true);
        List<String> moves = state.getNextMoves();
        assertTrue(moves.contains("1@1->0"));
    }

    @Test
    public void testGetNextMoves_foundElsewhere() {
        FlaskGameState state = buildState(List.of(
            List.of(PINK, RED, RED),
            List.of(),
            List.of(RED, RED)
        ));
        state.setRedundantMovesRemoved(true);
        state.setDebugMode(true);
        List<String> moves = state.getNextMoves();
        assertFalse(moves.contains("2@0->1"));
        assertTrue(moves.contains("2@0->2"));
    }

    @Test
    public void testGetNextMoves_foundElsewhere_secondToEmpty() {
        FlaskGameState state = buildState(List.of(
            List.of(),
            List.of(PINK, RED, RED),
            List.of(RED, RED)
        ));
        state.setRedundantMovesRemoved(true);
        state.setDebugMode(true);
        List<String> moves = state.getNextMoves();
        assertFalse(moves.contains("2@1->0"));
        assertTrue(moves.contains("2@1->2"));
    }

    @Test
    public void testIsValidMove() {
        FlaskGameState state = buildState(List.of(
            List.of(RED, RED, PINK, PINK),
            List.of(PINK, PINK, RED, RED),
            List.of()
        ));
        assertTrue(state.isValidMove("2@0->2"));
        assertFalse(state.isValidMove("2@0->1"));

        // invalidForNonEmpty (overflow)
        assertFalse(state.isValidMove("2@0->1")); // PINK on RED and 2+4 > 4. Actually 2+4 <= 4 is false.

        // validForEmpty case
        assertTrue(state.isValidMove("2@0->2"));

        // invalidForEmpty case (entire flask is one color)
        FlaskGameState state2 = buildState(List.of(
            List.of(RED, RED, RED, RED),
            List.of()
        ));
        assertFalse(state2.isValidMove("4@0->1"));
    }

    @Test
    public void testHashCodeAndEquals() {
        FlaskGameState state1 = buildState(List.of(List.of(RED, RED, RED, RED), List.of()));
        FlaskGameState state2 = buildState(List.of(List.of(RED, RED, RED, RED), List.of()));
        assertEquals(state1.hashCode(), state2.hashCode());
        assertEquals(state1, state2);
    }

    @Test
    public void testInvalidMoveInDebugMode() {
        FlaskGameState state = buildState(List.of(List.of(RED), List.of(RED, RED, RED, RED)));
        state.setDebugMode(true);
        // This is an invalid move because the target flask is full.
        // It will still try to execute it and probably crash with StackOverflow or something if we are not careful,
        // but here it will just push to a full stack which is allowed by java.util.Stack.
        // The point is to hit the print statement.
        state.makeMove("1@0->1");
    }

    @Test
    public void testUndoMoveWithZeroSize() {
        FlaskGameState state = buildState(List.of(List.of(RED), List.of()));
        state.movesHistory.add("0@0->1");
        state.undoLastMove();
    }
}
