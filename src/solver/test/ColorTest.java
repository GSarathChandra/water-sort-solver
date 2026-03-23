package solver.test;

import org.junit.jupiter.api.Test;
import solver.base.Color;

import static org.junit.jupiter.api.Assertions.*;

public class ColorTest {

    @Test
    public void testGetName() {
        assertEquals("red", Color.RED.getName());
        assertEquals("orange", Color.ORANGE.getName());
    }

    @Test
    public void testEnumValues() {
        for (Color color : Color.values()) {
            assertNotNull(color.getName());
        }
    }

    @Test
    public void testValueOf() {
        assertEquals(Color.RED, Color.valueOf("RED"));
    }
}
