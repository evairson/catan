package model.geometry;

public class Orientation {
    public double startAngle; // multiple of 60 degrees
    public double f0, f1, f2, f3;
    public double b0, b1, b2, b3;

    public Orientation(double startAngle, double f0, double f1, double f2, double f3, double b0, double b1, double b2, double b3) {
        this.startAngle = startAngle;
        this.f0 = f0;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.b0 = b0;
        this.b1 = b1;
        this.b2 = b2;
        this.b3 = b3;
    }

    public static Orientation POINTY= new Orientation(0.5, Math.sqrt(3.0), Math.sqrt(3.0) / 2.0, 0.0, 3.0 / 2.0, Math.sqrt(3.0) / 3.0, -1.0 / 3.0, 0.0, 2.0 / 3.0);
    public static Orientation FLAT= new Orientation(0.0, 3.0 / 2.0, 0.0, Math.sqrt(3.0) / 2.0, Math.sqrt(3.0), 2.0 / 3.0, 0.0, -1.0 / 3.0, Math.sqrt(3.0) / 3.0);
}