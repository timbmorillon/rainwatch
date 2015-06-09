package com.rainwatch.helper;

import com.rainwatch.RainCloud;

/**
 * Created by Tim on 01-08-2014.
 */
public class FindClosestHelper implements Comparable{
    RainCloud rainCloud;
    Double distance;

    public FindClosestHelper(RainCloud rainCloud, double distance) {
        this.rainCloud = rainCloud;
        this.distance = distance;
    }

    public RainCloud getRainCloud() {
        return rainCloud;
    }

    public double getDistance() {
        return distance;
    }


    @Override
    public int compareTo(Object o) {
        return distance.compareTo(((FindClosestHelper)o).distance);
    }
}
