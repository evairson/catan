public class FractionalCubeCoordinates {
    private double q, r, s;

    public FractionalCubeCoordinates(double q, double r, double s) {
        this.q = q;
        this.r = r;
        this.s = s;
    }

    public double getQ() {
        return this.q;
    }

    public double getR() {
        return this.r;
    }

    public double getS() {
        return this.s;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void setS(double s) {
        this.s = s;
    }

    public CubeCoordinates round() {
        int q = (int) Math.round(this.q);
        int r = (int) Math.round(this.r);
        int s = (int) Math.round(this.s);
        double q_diff = Math.abs(q - this.q);
        double r_diff = Math.abs(r - this.r);
        double s_diff = Math.abs(s - this.s);
        if (q_diff > r_diff && q_diff > s_diff) {
            q = -r - s;
        } else if (r_diff > s_diff) {
            r = -q - s;
        } else {
            s = -q - r;
        }
        return new CubeCoordinates(q, r, s);
    }

    public double distance(FractionalCubeCoordinates b) {
        return (Math.abs(this.q - b.q) + Math.abs(this.r - b.r) + Math.abs(this.s - b.s)) / 2;
    }

    public CubeCoordinates findClosestNeighbour() {
        CubeCoordinates rounded = this.round();
        FractionalCubeCoordinates[] neighbours = new FractionalCubeCoordinates[6];
        neighbours[0] = new FractionalCubeCoordinates(rounded.getQ() + 1, rounded.getR() - 1, rounded.getS());
        neighbours[1] = new FractionalCubeCoordinates(rounded.getQ() + 1, rounded.getR(), rounded.getS() - 1);
        neighbours[2] = new FractionalCubeCoordinates(rounded.getQ(), rounded.getR() + 1, rounded.getS() - 1);
        neighbours[3] = new FractionalCubeCoordinates(rounded.getQ() - 1, rounded.getR() + 1, rounded.getS());
        neighbours[4] = new FractionalCubeCoordinates(rounded.getQ() - 1, rounded.getR(), rounded.getS() + 1);
        neighbours[5] = new FractionalCubeCoordinates(rounded.getQ(), rounded.getR() - 1, rounded.getS() + 1);

        double minDistance = Double.MAX_VALUE;
        CubeCoordinates closestNeighbour = new CubeCoordinates(0, 0, 0);
        for (int i = 0; i < 6; i++) {
            double distance = this.distance(neighbours[i]);
            if (distance < minDistance) {
                minDistance = distance;
                closestNeighbour = neighbours[i].round();
            }
        }
        return closestNeighbour;
    }
    public CubeCoordinates[] findTwoClosestNeighbours() {
        CubeCoordinates rounded = this.round();
        FractionalCubeCoordinates[] neighbours = new FractionalCubeCoordinates[6];
        neighbours[0] = new FractionalCubeCoordinates(rounded.getQ() + 1, rounded.getR() - 1, rounded.getS());
        neighbours[1] = new FractionalCubeCoordinates(rounded.getQ() + 1, rounded.getR(), rounded.getS() - 1);
        neighbours[2] = new FractionalCubeCoordinates(rounded.getQ(), rounded.getR() + 1, rounded.getS() - 1);
        neighbours[3] = new FractionalCubeCoordinates(rounded.getQ() - 1, rounded.getR() + 1, rounded.getS());
        neighbours[4] = new FractionalCubeCoordinates(rounded.getQ() - 1, rounded.getR(), rounded.getS() + 1);
        neighbours[5] = new FractionalCubeCoordinates(rounded.getQ(), rounded.getR() - 1, rounded.getS() + 1);

        double minDistance = Double.MAX_VALUE;
        CubeCoordinates closestNeighbour = new CubeCoordinates(0, 0, 0);
        CubeCoordinates secondClosestNeighbour = new CubeCoordinates(0, 0, 0);
        for (int i = 0; i < 6; i++) {
            double distance = this.distance(neighbours[i]);
            if (distance < minDistance) {
                minDistance = distance;
                secondClosestNeighbour = closestNeighbour;
                closestNeighbour = neighbours[i].round();
            }
        }
        CubeCoordinates[] result = {closestNeighbour, secondClosestNeighbour};
        return result;
    }

}
