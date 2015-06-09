package com.rainwatch;

import com.rainwatch.helper.FindClosestHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Created by Tim on 08-07-2014.
 */
public class RadarImage {

    public static final int MAX_DISTANCE_BETWEEN_TWO_MATCHING_CLOUDS = 20;
    private static final int SIZE_THESHOLD = MAX_DISTANCE_BETWEEN_TWO_MATCHING_CLOUDS;
    private static final int FIND_CLOSEST_SIMPLE = 0;
    private static final int FIND_CLOSEST_WEIGHTED_FRONTIER = 1;

    int findClosestCloudStrategy=FIND_CLOSEST_SIMPLE;//FIND_CLOSEST_WEIGHTED_FRONTIER;
    BufferedImage image;
    IPixelEvaluator pixelEvaluator;
    private HashMap<RainPixel, RainCloud> rainClouds = new HashMap();
    List<RainCloud> cloudList = new ArrayList<RainCloud>();

    public RadarImage(BufferedImage image, IPixelEvaluator pixelEvaluator) {
        this.image = image;
        this.pixelEvaluator = pixelEvaluator;
    }

    public void calc() {
        findRainClouds();
        pxoximityMerge();
        filterSmall();

    }

    private void pxoximityMerge() {
       CloudMerger merger=new CloudMerger(cloudList);
       cloudList=merger.merge();
    }

    private void filterSmall() {
        ArrayList<RainCloud> toRemove = new ArrayList<RainCloud>();
        for (RainCloud c : cloudList) {
            if (c.size() < SIZE_THESHOLD) {
                toRemove.add(c);
            }

        }
        cloudList.removeAll(toRemove);
    }

    private void findRainClouds() {
        int maxX = image.getWidth();
        int maxY = image.getHeight();
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                RainSeverity sev = pixelEvaluator.getSeverity(image, x, y);
                if (sev.isRain()) {
                    ArrayList<RainPixel> toCheck = calcPixelsToCheck(maxX, x, y);

                    Set<RainCloud> touchingClouds = findCloud(toCheck);
                    RainCloud existing = mergeClouds(touchingClouds);
                    RainPixel current = new RainPixel(x, y, sev);
                    if (existing != null) {
                        rainClouds.put(current, existing);
                        existing.add(current);
                        existing.ensureConnected(current);
                    } else {
                        RainCloud newCloud;
                        rainClouds.put(current, newCloud = new RainCloud(current));
                        cloudList.add(newCloud);
                    }
                }
            }
        }

    }

    private ArrayList<RainPixel> calcPixelsToCheck(int maxX, int x, int y) {
        ArrayList<RainPixel> toCheck = new ArrayList<RainPixel>();

        if (x > 0) {
            toCheck.add(new RainPixel(x - 1, y));
        }
        if (y > 0) {
            toCheck.add(new RainPixel(x, y - 1));
        }
        if (y > 0 && x > 0) {
            toCheck.add(new RainPixel(x - 1, y - 1));
        }
        if (y > 0 && x < maxX - 1) {
            toCheck.add(new RainPixel(x + 1, y - 1));
        }
        //check those with one pixel dist
        if (x > 1) {
            toCheck.add(new RainPixel(x - 2, y));
            if (y > 0) {
                toCheck.add(new RainPixel(x - 2, y - 1));
            }
        }
        if (y > 1) {
            toCheck.add(new RainPixel(x, y - 2));
            if (x > 0) {
                toCheck.add(new RainPixel(x - 1, y - 2));
            }
        }

        return toCheck;
    }

    private RainCloud mergeClouds(Set<RainCloud> touchingClouds) {
        if (touchingClouds.size() == 0)
            return null;
        if (touchingClouds.size() == 1)
            return touchingClouds.iterator().next();

        Iterator<RainCloud> it = touchingClouds.iterator();
        RainCloud first = it.next();
        while (it.hasNext()) {
            RainCloud toMerge = it.next();
            first.merge(toMerge);
            for (RainPixel p : toMerge.getPixels()) {
                rainClouds.put(p, first);
                cloudList.remove(toMerge);
            }
        }
        return first;
    }


    private Set<RainCloud> findCloud(ArrayList<RainPixel> toCheck) {
        Set<RainCloud> res = new HashSet<RainCloud>();
        for (RainPixel p : toCheck) {
            RainCloud existing = rainClouds.get(p);
            if (existing != null) {
                res.add(existing);
            }
        }
        return res;
    }

    public List<RainCloud> getCloudList() {
        return cloudList;
    }

    public void findSameClouds(RadarImage prev) {
        for (RainCloud c : cloudList) {
            RainCloud closest = prev.findClosest(c);
            c.setPrevious(closest);
            if(closest != null) {
                closest.setNext(c);
            }
        }
    }

    private RainCloud findClosest(RainCloud toCheck) {
        if(findClosestCloudStrategy==FIND_CLOSEST_SIMPLE) {
            return findClosestSimple(toCheck);
        }else{
            return findClosestWeightedFrontier(toCheck);
        }
    }

    private RainCloud findClosestWeightedFrontier(RainCloud toCheck) {
        double dist=Double.MAX_VALUE;
        RainCloud result=null;
        for(RainCloud c:cloudList){
            double d=c.calcFrontDistanceAndRemember(toCheck);

            if(d< MAX_DISTANCE_BETWEEN_TWO_MATCHING_CLOUDS && d<dist && c.aboutTheSameSize(toCheck)){
                d*=c.relativeSize(toCheck);
                dist=d;
                result=c;
            }
        }
        return result;
    }

    private RainCloud findClosestSimple(RainCloud toCheck) {
        double dist=Double.MAX_VALUE;
        RainCloud result=null;
        for(RainCloud c:cloudList){
           double d=c.estimateDistance(toCheck);
            if(d< MAX_DISTANCE_BETWEEN_TWO_MATCHING_CLOUDS && d<dist && c.aboutTheSameSize(toCheck)){
                d*=c.relativeSize(toCheck);
                dist=d;
                result=c;
            }
        }
        return result;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void paint() {
        Graphics2D g = image.createGraphics();
        for(RainCloud c:cloudList){
            c.paint(image,g);
        }
    }

    public void estimateVelocities(int numImagesToLookBack) {
        for(RainCloud c:cloudList){
            c.estimateBasicVelocity(numImagesToLookBack);
        }
    }


    public List<FindClosestHelper> findClosestWithVelocity(RainCloud futureCloud,int maxNumClouds, int timeFactor) {
        ArrayList<FindClosestHelper> temp=new ArrayList<FindClosestHelper>();
        for(RainCloud c:cloudList){
            double d=c.estimateDistanceWithVelocity(futureCloud,timeFactor);
            temp.add(new FindClosestHelper(c,d));
        }
        Collections.sort(temp);
        return temp.subList(0,Math.min(maxNumClouds,temp.size()-1));
    }
}
