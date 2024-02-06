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
}
