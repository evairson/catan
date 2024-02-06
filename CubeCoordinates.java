import java.util.ArrayList;

public class CubeCoordinates {
    private int q, r, s;

    public CubeCoordinates(int q, int r, int s) {
        if (q + r + s != 0) {
            throw new IllegalArgumentException("q + r + s must be 0");
        }
        this.q = q;
        this.r = r;
        this.s = s;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CubeCoordinates)) {
            return false;
        }
        CubeCoordinates c = (CubeCoordinates) obj;
        return (c.q == this.q && c.r == this.r && c.s == this.s);
    }

    public CubeCoordinates add(CubeCoordinates b) {
        return new CubeCoordinates(this.q + b.q, this.r + b.r, this.s + b.s);
    }

    public CubeCoordinates subtract(CubeCoordinates b) {
        return new CubeCoordinates(this.q - b.q, this.r - b.r, this.s - b.s);
    }

    public CubeCoordinates multiply(int k) {
        return new CubeCoordinates(this.q * k, this.r * k, this.s * k);
    }

    public int length() {
        return (Math.abs(this.q) + Math.abs(this.r) + Math.abs(this.s)) / 2;
    }

    public int distance(CubeCoordinates b) {
        return this.subtract(b).length();
    }

    public CubeCoordinates direction(int direction/* 0 to 5 */) {
        if (direction < 0 || direction > 5) {
            throw new IllegalArgumentException("Direction must be between 0 and 5");
        }
        return Constants.CubeCoordinatesConst.CubeDirections.get(direction);
    }

    public CubeCoordinates neighbor(int direction/* 0 to 5 */) {
        if (direction < 0 || direction > 5) {
            throw new IllegalArgumentException("Direction must be between 0 and 5");
        }
        return this.add(direction(direction));
    }

    /**
     * @return int return the s
     */
    public int getS() {
        return s;
    }

    /**
     * @param s the s to set
     */
    public void setS(int s) {
        this.s = s;
    }

    /**
     * @return int return the r
     */
    public int getR() {
        return r;
    }

    /**
     * @param r the r to set
     */
    public void setR(int r) {
        this.r = r;
    }

    /**
     * @return int return the q
     */
    public int getQ() {
        return q;
    }

    /**
     * @param q the q to set
     */
    public void setQ(int q) {
        this.q = q;
    }

    @Override
    public String toString() {
        return "(q: " + this.q + " r: " + this.r + " s: " + this.s + ")";
    }
}