
import model.geometry.CubeCoordinates;
import model.geometry.FractionalCubeCoordinates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FractionalCubeCoordinatesTest {

    @Test
    public void testConstructor() {
        FractionalCubeCoordinates fcc = new FractionalCubeCoordinates(1.5, -0.5, -1.0);
        assertEquals(1.5, fcc.getQ());
        assertEquals(-0.5, fcc.getR());
        assertEquals(-1.0, fcc.getS());
    }

    @Test
    public void testGettersAndSetters() {
        FractionalCubeCoordinates fcc = new FractionalCubeCoordinates(1.5, -0.5, -1.0);
        fcc.setQ(2.0);
        fcc.setR(-1.0);
        fcc.setS(-1.0);
        assertEquals(2.0, fcc.getQ());
        assertEquals(-1.0, fcc.getR());
        assertEquals(-1.0, fcc.getS());
    }

    @Test
    public void testRound() {
        FractionalCubeCoordinates fcc = new FractionalCubeCoordinates(1.5, -0.5, -1.0);
        CubeCoordinates rounded = fcc.round();
        assertEquals(new CubeCoordinates(2, -1, -1), rounded);
    }

    @Test
    public void testDistance() {
        FractionalCubeCoordinates fcc1 = new FractionalCubeCoordinates(1.0, -1.0, 0.0);
        FractionalCubeCoordinates fcc2 = new FractionalCubeCoordinates(0.0, 1.0, -1.0);
        assertEquals(2, fcc1.distance(fcc2));
    }

    @Test
    public void testFindClosestNeighbour() {
        FractionalCubeCoordinates fcc = new FractionalCubeCoordinates(1.2, -0.8, -0.4);
        CubeCoordinates closestNeighbour = fcc.findClosestNeighbour();
        assertEquals(new CubeCoordinates(2, -1, -1), closestNeighbour);
    }

    @Test
    public void testFindTwoClosestNeighbours() {
        FractionalCubeCoordinates fcc = new FractionalCubeCoordinates(1.2, -0.8, -0.4);
        CubeCoordinates[] closestNeighbours = fcc.findTwoClosestNeighbours();
        assertEquals(2, closestNeighbours.length);
        assertEquals(new CubeCoordinates(2, -1, -1), closestNeighbours[0]);
        assertEquals(new CubeCoordinates(2, -2, 0), closestNeighbours[1]);
    }
}
