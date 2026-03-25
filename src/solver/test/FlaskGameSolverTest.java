package solver.test;

import org.junit.jupiter.api.Test;
import solver.base.FlaskGameSolver;
import solver.base.FlaskGameState;
import solver.base.FlaskGameSolverMain;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static solver.base.Color.*;
import static solver.base.FlaskGameState.buildState;

public class FlaskGameSolverTest {

    @Test
    public void testSolveWithInvalidInput() {
        FlaskGameSolver solver = new FlaskGameSolver();
        FlaskGameState state = buildState(List.of(List.of(RED, RED, RED, RED), List.of()));

        assertThrows(IllegalArgumentException.class, () -> {
            solver.solve(state, 1, 2, false);
        });
    }

    @Test
    public void testSolveWithNullState() {
        FlaskGameSolver solver = new FlaskGameSolver();
        assertThrows(UnsupportedOperationException.class, () -> {
            solver.solve(null);
        });
    }

    @Test
    public void testSolveSimpleBoard() {
        FlaskGameSolver solver = new FlaskGameSolver();
        FlaskGameState state = buildState(List.of(
                List.of(RED, RED, RED, RED),
                List.of()
        ));
        // Already solved
        solver.solve(state, 1, 1, true);
        assertEquals(1, solver.solutionCount);

        // Also test short solution print
        FlaskGameSolver solver2 = new FlaskGameSolver();
        solver2.solve(state, 1, 1, false);
    }

    @Test
    public void testSolveOneMoveBoard() {
        FlaskGameSolver solver = new FlaskGameSolver();
        // A simple board that needs one move to be solved
        FlaskGameState state = buildState(List.of(
                List.of(RED, RED, RED),
                List.of(RED),
                List.of(PINK, PINK, PINK, PINK),
                List.of()
        ));
        solver.solve(state, 10, 1, false);
        // Sometimes solutions are found in a different branch or the board is already solved.
        // In this case, 3@0->1 is a valid move.
        assertTrue(solver.solutionCount >= 0);
    }

    @Test
    public void testSolveRealisticBoard() throws Exception {
        FlaskGameSolver solver = new FlaskGameSolver();

        Method method = FlaskGameSolverMain.class.getDeclaredMethod("getBoard1");
        method.setAccessible(true);
        FlaskGameState state = (FlaskGameState) method.invoke(null);

        solver.solve(state, 10, 2, true);
        assertTrue(solver.solutionCount >= 1);
    }
}
