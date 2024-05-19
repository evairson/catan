
import model.geometry.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class LayoutTest {
    @Test
    public void testCubeCoCornerOffset() {
        Orientation orientation = new Orientation(0, Math.sqrt(3.0), Math.sqrt(3.0) / 2, 0, 3.0 / 2,
                Math.sqrt(3.0) / 3, -1.0 / 3, 0, 2.0 / 3);
        Point origin = new Point(10, 15);
        Point size = new Point(30, 30);
        Layout layout = new Layout(orientation, origin, size);

        Point offset = layout.cubeCoCornerOffset(layout, 0);

        assertEquals(30.0, offset.getX(), 0.001);
        assertEquals(0.0, offset.getY(), 0.001);
    }
}
