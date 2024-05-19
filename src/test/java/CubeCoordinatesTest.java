import model.geometry.CubeCoordinates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CubeCoordinatesTest {

    @Test
    public void testConstructorValid() {
        assertDoesNotThrow(() -> new CubeCoordinates(1, -1, 0));
        assertDoesNotThrow(() -> new CubeCoordinates(2, -3, 1));
    }

    @Test
    public void testConstructorInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new CubeCoordinates(1, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new CubeCoordinates(2, 2, 2));
    }

    @Test
    public void testEquals() {
        CubeCoordinates c1 = new CubeCoordinates(1, -1, 0);
        CubeCoordinates c2 = new CubeCoordinates(1, -1, 0);
        CubeCoordinates c3 = new CubeCoordinates(1, 0, -1);
        assertTrue(c1.equals(c2));
        assertFalse(c1.equals(c3));
    }

    @Test
    public void testHashCode() {
        CubeCoordinates c1 = new CubeCoordinates(1, -1, 0);
        CubeCoordinates c2 = new CubeCoordinates(1, -1, 0);
        CubeCoordinates c3 = new CubeCoordinates(1, 0, -1);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1.hashCode(), c3.hashCode());
    }

    @Test
    public void testAdd() {
        CubeCoordinates c1 = new CubeCoordinates(1, -1, 0);
        CubeCoordinates c2 = new CubeCoordinates(2, -3, 1);
        CubeCoordinates result = c1.add(c2);
        assertEquals(new CubeCoordinates(3, -4, 1), result);
    }

    @Test
    public void testSubtract() {
        CubeCoordinates c1 = new CubeCoordinates(1, -1, 0);
        CubeCoordinates c2 = new CubeCoordinates(2, -3, 1);
        CubeCoordinates result = c1.subtract(c2);
        assertEquals(new CubeCoordinates(-1, 2, -1), result);
    }

    @Test
    public void testMultiply() {
        CubeCoordinates c = new CubeCoordinates(1, -1, 0);
        CubeCoordinates result = c.multiply(3);
        assertEquals(new CubeCoordinates(3, -3, 0), result);
    }

    @Test
    public void testLength() {
        CubeCoordinates c = new CubeCoordinates(3, -3, 0);
        assertEquals(3, c.length());
    }

    @Test
    public void testDistance() {
        CubeCoordinates c1 = new CubeCoordinates(1, -1, 0);
        CubeCoordinates c2 = new CubeCoordinates(2, -3, 1);
        assertEquals(2, c1.distance(c2));
    }

    @Test
    public void testDirectionValid() {
        CubeCoordinates c = new CubeCoordinates(1, -1, 0);
        for (int i = 0; i <= 5; i++) {
            assertNotNull(c.direction(i));
        }
    }

    @Test
    public void testDirectionInvalid() {
        CubeCoordinates c = new CubeCoordinates(1, -1, 0);
        assertThrows(IllegalArgumentException.class, () -> c.direction(-1));
        assertThrows(IllegalArgumentException.class, () -> c.direction(6));
    }

    @Test
    public void testNeighborValid() {
        CubeCoordinates c = new CubeCoordinates(1, -1, 0);
        for (int i = 0; i <= 5; i++) {
            assertNotNull(c.neighbor(i));
        }
    }

    @Test
    public void testNeighborInvalid() {
        CubeCoordinates c = new CubeCoordinates(1, -1, 0);
        assertThrows(IllegalArgumentException.class, () -> c.neighbor(-1));
        assertThrows(IllegalArgumentException.class, () -> c.neighbor(6));
    }

    @Test
    public void testGettersAndSetters() {
        CubeCoordinates c = new CubeCoordinates(1, -1, 0);
        assertEquals(1, c.getQ());
        assertEquals(-1, c.getR());
        assertEquals(0, c.getS());

        c.setQ(2);
        c.setR(-2);
        c.setS(0);

        assertEquals(2, c.getQ());
        assertEquals(-2, c.getR());
        assertEquals(0, c.getS());
    }

    @Test
    public void testToString() {
        CubeCoordinates c = new CubeCoordinates(1, -1, 0);
        assertEquals("(q: 1 r: -1 s: 0)", c.toString());
    }
}
