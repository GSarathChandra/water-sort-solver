package solver.old;

import solver.base.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static solver.base.Color.*;
import static org.junit.jupiter.api.Assertions.*;
import static solver.old.FlaskGameStateOld.buildState;

public class FlaskGameStateOldTest {

    @Test
    public void testGetTopColorSize_onlyOneColorAndSolved(){
        FlaskGameStateOld state = new FlaskGameStateOld();
        Stack<Color> flask = new Stack<>();
        flask.push(RED); flask.push(RED); flask.push(RED); flask.push(RED);
        state.flasks = new ArrayList<>();
        state.flasks.add(flask);

        // Assert
        assertEquals(4, state.getTopColorSize(flask));
        assertTrue(state.isSolved());
    }

    @Test
    public void testGetTopColorSize_twoColorsWithBothContiguous(){
        FlaskGameStateOld state = new FlaskGameStateOld();
        Stack<Color> flask = new Stack<>();
        flask.push(PINK); flask.push(PINK); flask.push(RED); flask.push(RED);
        state.flasks = new ArrayList<>();
        state.flasks.add(flask);

        // Assert
        assertEquals(2, state.getTopColorSize(flask));
        assertFalse(state.isSolved());
    }

    @Test
    public void testGetTopColorSize_threeColorsWithTopContiguous(){
        FlaskGameStateOld state = new FlaskGameStateOld();
        Stack<Color> flask = new Stack<>();
        flask.push(ORANGE); flask.push(PINK); flask.push(RED); flask.push(RED);
        state.flasks = new ArrayList<>();
        state.flasks.add(flask);

        // Assert
        assertEquals(2, state.getTopColorSize(flask));
        assertFalse(state.isSolved());
    }

    @Test
    public void testGetTopColorSize_threeColorsWithMiddleContiguous(){
        FlaskGameStateOld state = new FlaskGameStateOld();
        Stack<Color> flask = new Stack<>();
        flask.push(ORANGE); flask.push(PINK); flask.push(PINK); flask.push(RED);
        state.flasks = new ArrayList<>();
        state.flasks.add(flask);

        // Assert
        assertEquals(1, state.getTopColorSize(flask));
        assertFalse(state.isSolved());
    }

    @Test
    public void testGetTopColorSize_emptyFlaskTreatedAsSolved(){
        FlaskGameStateOld state = new FlaskGameStateOld();
        Stack<Color> flask = new Stack<>();
        state.flasks = new ArrayList<>();
        state.flasks.add(flask);

        // Assert
        assertEquals(0, state.getTopColorSize(flask));
        assertTrue(state.isSolved());
    }

    @Test
    public void testGetNextMoves_validOneToTwo(){
        FlaskGameStateOld state = new FlaskGameStateOld();
        Stack<Color> one = new Stack<>();
        one.push(PINK); one.push(PINK); one.push(RED); one.push(RED);

        Stack<Color> two = new Stack<>();
        two.push(ORANGE); two.push(RED);

        state.flasks = new ArrayList<>();
        state.flasks.add(one);
        state.flasks.add(two);
        List<String> expectedMoves = List.of("0->1");

        // Assert
        assertEquals(2, state.getTopColorSize(one));
        assertEquals(1, state.getTopColorSize(two));
        assertFalse(state.isSolved());
        assertIterableEquals(expectedMoves, state.getNextMoves());
    }

    @Test
    public void testGetNextMoves_noValidMoves_moveCausesOverflow(){
        FlaskGameStateOld state = new FlaskGameStateOld();
        Stack<Color> one = new Stack<>();
        one.push(PINK); one.push(PINK); one.push(RED); one.push(RED);

        Stack<Color> two = new Stack<>();
        two.push(ORANGE); two.push(RED); two.push(RED);

        state.flasks = new ArrayList<>();
        state.flasks.add(one);
        state.flasks.add(two);
        List<String> expectedMoves = List.of();

        // Assert
        assertEquals(2, state.getTopColorSize(one));
        assertEquals(2, state.getTopColorSize(two));
        assertFalse(state.isSolved());
        assertIterableEquals(expectedMoves, state.getNextMoves());
    }

    @Test
    public void testGetNextMoves_validOneToTwo_overflowTwoToOne(){
        FlaskGameStateOld state = new FlaskGameStateOld();
        Stack<Color> one = new Stack<>();
        one.push(PINK); one.push(PINK); one.push(RED);

        Stack<Color> two = new Stack<>();
        two.push(ORANGE); two.push(RED); two.push(RED);

        state.flasks = new ArrayList<>();
        state.flasks.add(one);
        state.flasks.add(two);
        List<String> expectedMoves = List.of("0->1");

        // Assert
        assertEquals(1, state.getTopColorSize(one));
        assertEquals(2, state.getTopColorSize(two));
        assertFalse(state.isSolved());
        assertIterableEquals(expectedMoves, state.getNextMoves());
    }

    @Test
    public void testGetNextMoves_overFlowOneToTwo_validTwoToOne(){
        FlaskGameStateOld state = new FlaskGameStateOld();
        Stack<Color> one = new Stack<>();
        one.push(PINK); one.push(RED); one.push(RED);

        Stack<Color> two = new Stack<>();
        two.push(ORANGE); two.push(ORANGE); two.push(RED);

        state.flasks = new ArrayList<>();
        state.flasks.add(one);
        state.flasks.add(two);
        List<String> expectedMoves = List.of("1->0");

        // Assert
        assertEquals(2, state.getTopColorSize(one));
        assertEquals(1, state.getTopColorSize(two));
        assertFalse(state.isSolved());
        assertIterableEquals(expectedMoves, state.getNextMoves());
    }

    @Test
    public void testGetNextMoves_validBothWays(){
        FlaskGameStateOld state = new FlaskGameStateOld();
        Stack<Color> one = new Stack<>();
        one.push(PINK); one.push(RED);

        Stack<Color> two = new Stack<>();
        two.push(ORANGE); two.push(RED);

        state.flasks = new ArrayList<>();
        state.flasks.add(one);
        state.flasks.add(two);
        List<String> expectedMoves = List.of("0->1", "1->0");

        // Assert
        assertEquals(1, state.getTopColorSize(one));
        assertEquals(1, state.getTopColorSize(two));
        assertFalse(state.isSolved());
        assertIterableEquals(expectedMoves, state.getNextMoves());
    }

    @Test
    public void testGetNextMoves_moveToEmpty_valid(){
        FlaskGameStateOld state = new FlaskGameStateOld();
        Stack<Color> one = new Stack<>();
        one.push(PINK); one.push(RED); one.push(RED);

        Stack<Color> two = new Stack<>();

        state.flasks = new ArrayList<>();
        state.flasks.add(one);
        state.flasks.add(two);
        List<String> expectedMoves = List.of("0->1");

        // Assert
        assertEquals(2, state.getTopColorSize(one));
        assertEquals(0, state.getTopColorSize(two));
        assertFalse(state.isSolved());
        assertIterableEquals(expectedMoves, state.getNextMoves());
    }

    @Test
    public void testGetNextMoves_moveToEmpty_invalid(){
        FlaskGameStateOld state = new FlaskGameStateOld();
        Stack<Color> one = new Stack<>();
        one.push(PINK); one.push(PINK); one.push(PINK); one.push(PINK);

        Stack<Color> two = new Stack<>();

        state.flasks = new ArrayList<>();
        state.flasks.add(one);
        state.flasks.add(two);
        List<String> expectedMoves = List.of();

        // Assert
        assertEquals(4, state.getTopColorSize(one));
        assertEquals(0, state.getTopColorSize(two));
        assertTrue(state.isSolved());
        assertIterableEquals(expectedMoves, state.getNextMoves());
    }

    @Test
    public void testEquals_noEmptyFlasks_sameOrder(){
        FlaskGameStateOld firstState = buildState(
                List.of(
                        List.of(PINK, PINK, RED, RED),
                        List.of(RED, RED, PINK, PINK)
                ));
        FlaskGameStateOld secondState = buildState(
                List.of(
                        List.of(PINK, PINK, RED, RED),
                        List.of(RED, RED, PINK, PINK)
                ));

        // Assert
        assertTrue(firstState.equals(secondState));
    }

    @Test
    public void testEquals_emptyFlasks_valid(){
        FlaskGameStateOld firstState = buildState(
                List.of(
                        List.of(PINK, PINK, RED, RED),
                        List.of(RED, RED, PINK, PINK),
                        List.of()
                ));
        FlaskGameStateOld secondState = buildState(
                List.of(
                        List.of(PINK, PINK, RED, RED),
                        List.of(),
                        List.of(RED, RED, PINK, PINK)
                ));

        // Assert
        assertTrue(firstState.equals(secondState));
    }

    @Test
    public void testEquals_differentFlasks_valid(){
        FlaskGameStateOld firstState = buildState(
                List.of(
                        List.of(PINK, PINK, RED, RED),
                        List.of(RED, RED, PINK, PINK),
                        List.of()
                ));
        FlaskGameStateOld secondState = buildState(
                List.of(
                        List.of(GRAY, GRAY, RED, RED),
                        List.of(),
                        List.of(RED, RED, GRAY, GRAY)
                ));

        // Assert
        assertFalse(firstState.equals(secondState));
    }
}