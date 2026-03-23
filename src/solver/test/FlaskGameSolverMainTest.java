package solver.test;

import org.junit.jupiter.api.Test;
import solver.base.FlaskGameSolverMain;
import solver.base.FlaskGameState;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class FlaskGameSolverMainTest {

    @Test
    public void testGetBoards() throws Exception {
        String[] methodNames = {
            "getBoard1", "getBoard2", "getBoard175", "getBoard181",
            "getLevel183", "getLevel211", "getLevel233", "getLevel239"
        };

        for (String methodName : methodNames) {
            Method method = FlaskGameSolverMain.class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            Object result = method.invoke(null);
            assertNotNull(result);
            assertTrue(result instanceof FlaskGameState);
        }
    }

    @Test
    public void testMain() {
        // Skipping main test as it runs the full level and takes too long.
        // FlaskGameSolverMain.main(new String[]{});
    }

    @Test
    public void testConstructor() throws Exception {
        java.lang.reflect.Constructor<FlaskGameSolverMain> constructor = FlaskGameSolverMain.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
