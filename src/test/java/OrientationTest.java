
import model.geometry.Orientation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrientationTest {

    @Test
    public void testConstructor() {
        Orientation orientation = new Orientation(30, 1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(30, orientation.getStartAngle());
        assertEquals(1, orientation.getF0());
        assertEquals(2, orientation.getF1());
        assertEquals(3, orientation.getF2());
        assertEquals(4, orientation.getF3());
        assertEquals(5, orientation.getB0());
        assertEquals(6, orientation.getB1());
        assertEquals(7, orientation.getB2());
        assertEquals(8, orientation.getB3());
    }

    @Test
    public void testGettersAndSetters() {
        Orientation orientation = new Orientation(30, 1, 2, 3, 4, 5, 6, 7, 8);

        orientation.setStartAngle(60);
        orientation.setF0(1.1);
        orientation.setF1(2.2);
        orientation.setF2(3.3);
        orientation.setF3(4.4);
        orientation.setB0(5.5);
        orientation.setB1(6.6);
        orientation.setB2(7.7);
        orientation.setB3(8.8);

        assertEquals(60, orientation.getStartAngle());
        assertEquals(1.1, orientation.getF0());
        assertEquals(2.2, orientation.getF1());
        assertEquals(3.3, orientation.getF2());
        assertEquals(4.4, orientation.getF3());
        assertEquals(5.5, orientation.getB0());
        assertEquals(6.6, orientation.getB1());
        assertEquals(7.7, orientation.getB2());
        assertEquals(8.8, orientation.getB3());
    }
}
