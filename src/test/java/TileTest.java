
import model.geometry.CubeCoordinates;
import model.tiles.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.TileType;

import static org.junit.jupiter.api.Assertions.*;

public class TileTest {

    @BeforeEach
    public void resetIdClass() {
        // Reset idClass to ensure consistent test results
        Tile.addIdClass();
    }

    @Test
    public void testConstructorWithQR() {
        Tile tile = new Tile(1, 2);
        assertEquals(1, tile.getQ());
        assertEquals(2, tile.getR());
        assertEquals(0, tile.getDiceValue());
        assertNull(tile.getResourceType());
        assertNotNull(tile.getEdges());
        assertNotNull(tile.getVertices());
    }

    @Test
    public void testConstructorWithQRAndDiceValue() {
        Tile tile = new Tile(1, 2, 8);
        assertEquals(1, tile.getQ());
        assertEquals(2, tile.getR());
        assertEquals(8, tile.getDiceValue());
        assertNull(tile.getResourceType());
        assertNotNull(tile.getEdges());
        assertNotNull(tile.getVertices());
    }

    @Test
    public void testConstructorWithQRDiceValueAndResourceType() {
        Tile tile = new Tile(1, 2, 8, TileType.WOOD);
        assertEquals(1, tile.getQ());
        assertEquals(2, tile.getR());
        assertEquals(8, tile.getDiceValue());
        assertEquals(TileType.WOOD, tile.getResourceType());
        assertNotNull(tile.getEdges());
        assertNotNull(tile.getVertices());
    }

    @Test
    public void testSetAndGetResourceType() {
        Tile tile = new Tile(1, 2);
        tile.setResourceType(TileType.CLAY);
        assertEquals(TileType.CLAY, tile.getResourceType());
    }

    @Test
    public void testSetAndGetDiceValue() {
        Tile tile = new Tile(1, 2);
        tile.setDiceValue(6);
        assertEquals(6, tile.getDiceValue());
    }

    @Test
    public void testGetCoordinates() {
        Tile tile = new Tile(1, 2);
        CubeCoordinates coordinates = tile.getCoordinates();
        assertEquals(new CubeCoordinates(1, 2, -3), coordinates);
    }

    @Test
    public void testEquals() {
        Tile tile1 = new Tile(1, 2);
        Tile tile2 = new Tile(1, 2);
        Tile tile3 = new Tile(2, 3);
        assertTrue(tile1.equals(tile2));
        assertFalse(tile1.equals(tile3));
    }

    @Test
    public void testToString() {
        Tile tile = new Tile(1, 2);
        assertEquals("(1, 2)", tile.toString());
    }

    @Test
    public void testGetId() {
        Tile tile = new Tile(1, 2);
        assertEquals(Tile.getIdClass() - 1, tile.getId());
    }
}
