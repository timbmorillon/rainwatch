package com.rainwatch;

/**
 * Created by Tim on 08-07-2014.
 */
public class RainPixel {
    int x;
    int y;
    RainSeverity severity;


    public RainPixel(int x, int y, RainSeverity severity) {
        this.x = x;
        this.y = y;
        this.severity = severity;
    }

    public RainPixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public RainSeverity getSeverity() {
        return severity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RainPixel rainPixel = (RainPixel) o;

        if (x != rainPixel.x) return false;
        if (y != rainPixel.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public double distance(RainPixel p) {
        int xDiff = x - p.getX();
        int yDiff = y - p.getY();
        return Math.sqrt(xDiff*xDiff+yDiff*yDiff);

    }
}
