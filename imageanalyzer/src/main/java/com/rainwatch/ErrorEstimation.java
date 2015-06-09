package com.rainwatch;

import com.rainwatch.helper.FindClosestHelper;

import java.util.Collection;
import java.util.List;

/**
 * Created by Tim on 01-08-2014.
 */
public class ErrorEstimation {
    private final RadarImage imageWithCalculatedVelocities;
    private final RadarImage theFuture;

    public ErrorEstimation(RadarImage imageWithCalculatedVelocities,RadarImage theFuture) {
        this.imageWithCalculatedVelocities = imageWithCalculatedVelocities;
        this.theFuture = theFuture;
    }

    public void estimateError(int timeFactor){
        List<RainCloud> futureClouds = theFuture.getCloudList();
        for(RainCloud f:futureClouds){
           List<FindClosestHelper> closestCloudsWithVelocity = imageWithCalculatedVelocities.findClosestWithVelocity(f,5,timeFactor);
           evaluateCoverage(f,closestCloudsWithVelocity,timeFactor);
        }
    }

    private void evaluateCoverage(RainCloud f, List<FindClosestHelper> closestCloudsWithVelocity, int timeFactor) {
        Collection<RainPixel> pixels = f.getPixels();
        int nonCovered=0;
        for(RainPixel p:pixels){
           if(!isCovered(p,closestCloudsWithVelocity,timeFactor)){
               nonCovered++;
           }
        }
        double perc = (double)nonCovered/(double)pixels.size();
        f.setNonCovered(perc);
        System.out.println("Noncovered: " + perc*100);
    }

    private boolean isCovered(RainPixel p, List<FindClosestHelper> closestCloudsWithVelocity, int timeFactor) {
        for(FindClosestHelper h:closestCloudsWithVelocity){
            RainPixel inFuture = h.getRainCloud().getFuturePixel(p.getX(), p.getY(), timeFactor);
            if(inFuture != null)
                return true;
        }
        return false;
    }
}
