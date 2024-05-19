
import model.geometry.Point;
import model.tiles.TileVertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TileVertexTest {

    @BeforeEach
    public void resetIdClass() {
        TileVertex.resetIdClass();
    }

    @Test
    public void testConstructor() {
        TileVertex vertex = new TileVertex();
        assertNotNull(vertex.getTiles());
        assertTrue(vertex.getTiles().isEmpty());
    }

    @Test
    public void testSetAndGetId() {
        TileVertex vertex = new TileVertex();
        vertex.setId(5);
        assertEquals(5, vertex.getId());
    }

    @Test
    public void testSetAndGetCoordinates() {
        TileVertex vertex = new TileVertex();
        Point point = new Point(1, 2);
        vertex.setCoordinates(point);
        assertEquals(point, vertex.getCoordinates());
    }

    @Test
    public void testGetIdClass() {
        assertEquals(0, TileVertex.getIdClass());
        TileVertex.addIdClass();
        assertEquals(1, TileVertex.getIdClass());
    }
}
