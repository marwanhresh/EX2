package concrete;

import api.GeoLocation;

public class Coordinates implements GeoLocation {

    private double x;
    private double y;
    private double z;

    public Coordinates(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public double distance(GeoLocation g) {
        double d1 = g.x() - x;
        double d2 = g.y() - y;
        double d3 = g.z() - z;

        return Math.sqrt((d1 * d1) + (d2 * d2) + (d3 * d3));
    }
}
