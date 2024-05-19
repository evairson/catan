
import model.geometry.Point;
import model.tiles.TileEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TileEdgeTest {

    @BeforeEach
    public void resetIdClass() {
        TileEdge.resetIdClass();
    }

    @Test
    public void testConstructorWithStartAndEndPoints() {
        Point start = new Point(1, 1);
        Point end = new Point(2, 2);
        TileEdge edge = new TileEdge(start, end);
        assertEquals(start, edge.getStart());
        assertEquals(end, edge.getEnd());
        assertNotNull(edge.getTiles());
        assertTrue(edge.getTiles().isEmpty());
    }

    @Test
    public void testSetIdAndGetId() {
        TileEdge edge = new TileEdge();
        edge.setId(5);
        assertEquals(5, edge.getId());
    }

    @Test
    public void testAddIdClass() {
        assertEquals(0, TileEdge.getIdClass());
        TileEdge.addIdClass();
        assertEquals(1, TileEdge.getIdClass());
    }
}
