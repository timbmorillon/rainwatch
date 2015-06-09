package com.rainwatch;

import com.sun.javafx.geom.Vec2d;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by Tim on 08-07-2014.
 */
public class RainCloud {
    private HashMap<RainPixel, RainPixel> pixels = new HashMap<RainPixel, RainPixel>();
    private Vec2d center;
    private RainCloud previous;
    private RainCloud next;
    private CloudFrontier frontier;

    private HashMap<RainCloud, Double> distances = new HashMap<RainCloud, Double>();

    private Vec2d velocity;
    private double nonCovered;
    private int connectionCountBackInTime;
    private int preferredImagesToLookBack;


    public RainCloud(RainPixel current) {
        pixels.put(current, current);
    }

    public RainCloud(RainCloud c1, RainCloud c2) {
        pixels.putAll(c1.pixels);
        pixels.putAll(c2.pixels);
        frontier = new CloudFrontier(c1.getFrontier(), c2.getFrontier());
    }

    public void add(RainPixel current) {
        pixels.put(current, current);
    }

    /*public Set<RainPixel> getPixels() {
        return pixels;
    }*/

    private void calcCenter() {
        double x = 0;
        double y = 0;

        for (RainPixel p : pixels.values()) {
            x += p.getX();
            y += p.getY();
        }
        center = new Vec2d( (x / pixels.size()),  (y / pixels.size()));
    }

    public Vec2d getCenter() {
        if (center == null) {
            calcCenter();
        }
        return center;
    }

    public void merge(RainCloud next) {
        pixels.putAll(next.pixels);
    }

    public int size() {
        return pixels.size();
    }

    public double estimateDistance(RainCloud toCheck) {
        return toCheck.getCenter().distance(getCenter());
    }

    public boolean aboutTheSameSize(RainCloud toCheck) {
        double perc = relativeSize(toCheck);
        return perc < AlgorithmConstants.SAME_CLOUD_PERCENTAGE_LIMIT;
    }

    public double relativeSize(RainCloud toCheck) {
        return ((double) Math.abs(toCheck.size() - size())) / ((double) toCheck.size());
    }

    public void setPrevious(RainCloud previous) {
        this.previous = previous;
    }

    public void paint(BufferedImage img, Graphics2D g) {

        for (RainPixel p : pixels.values()) {
            img.setRGB(p.getX(), p.getY(), Color.cyan.getRGB());
        }

        if (next != null) {
            paintDiff(img, next);
        }
        getFrontier().paint(img);
       // img.setRGB(getCenter().x, getCenter().getY(), Color.black.getRGB());

        if(next!=null){
            Line2D shape = new Line2D.Double(getCenter().x, getCenter().y, next.getCenter().x, next.getCenter().y);
            g.setColor(Color.PINK);
            g.draw(shape);
        }
        if (velocity != null) {

            try {
                Line2D shape = new Line2D.Double(getCenter().x, getCenter().y,getCenter().x+ (float) velocity.x,getCenter().y+ (float) velocity.y);
                g.setColor(Color.BLACK);
                g.draw(shape);
               // img.setRGB((int) (getCenter().getX() + velocity.x), (int) (getCenter().getY() + velocity.y), Color.PINK.getRGB());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(nonCovered>0){
            g.setColor(Color.WHITE);
            NumberFormat nf = DecimalFormat.getNumberInstance();
            g.drawString(nf.format(nonCovered),(int)getCenter().x,(int)getCenter().y);
        }

    }

    private void paintDiff(BufferedImage img, RainCloud other) {
        for (RainPixel p : other.pixels.values()) {
            if (!hasPixel(p)) {

                img.setRGB(p.getX(), p.getY(), AlgorithmConstants.diffColor.getRGB());
            }
        }

    }

    private boolean hasPixel(RainPixel p) {
        return pixels.get(p) != null;
    }

    public void setNext(RainCloud next) {
        this.next = next;
    }

    public RainCloud getNext() {
        return next;
    }

    public void ensureConnected(RainPixel current) {
        if (current.getX() > 0) {
            RainPixel toCheck = new RainPixel(current.getX() - 1, current.getY(), current.getSeverity());
            if (!hasPixel(toCheck)) {
                add(toCheck);
            }
        }
        if (current.getY() > 0) {
            RainPixel toCheck = new RainPixel(current.getX(), current.getY() - 1, current.getSeverity());
            if (!hasPixel(toCheck)) {
                add(toCheck);
            }
        }
        if (current.getY() > 0 && current.getX() > 0) {
            RainPixel toCheck = new RainPixel(current.getX() - 1, current.getY() - 1, current.getSeverity());
            if (!hasPixel(toCheck)) {
                add(toCheck);
            }
        }

    }

    public CloudFrontier getFrontier() {
        if (frontier == null) {
            frontier = new CloudFrontier(this);
        }
        return frontier;
    }

    public RainPixel getPixel(int x, int y) {
        return pixels.get(new RainPixel(x, y));
    }

    public RainPixel getRandomPixel() {
        return pixels.values().iterator().next();
    }

    public Collection<RainPixel> getPixels() {
        return pixels.values();
    }

    public double calcFrontDistanceAndRemember(RainCloud rainCloud) {
        Double dist = distances.get(rainCloud);
        if (dist == null) {
            dist = getFrontier().distance(rainCloud.getFrontier());
            distances.put(rainCloud, dist);
        }
        return dist;
    }

    public double estimatedRadius() {
        double r2 = (double) pixels.size() / Math.PI;
        return Math.sqrt(r2);
    }

    public void estimateBasicVelocity(int numImagesToLookBack) {
        RainCloud current = this;
        int i = 0;
        for (; i < numImagesToLookBack; i++) {
            if (current.previous != null) {
                current = current.previous;
            }else{
                break;
            }
        }
        if(current != this) {
            Vec2d v = current.getBasicVelocity(this);
            velocity = new Vec2d(v.x / (double) i, v.y / (double) i);
            connectionCountBackInTime =i;
            preferredImagesToLookBack=numImagesToLookBack;
        }
    }

    private Vec2d getBasicVelocity(RainCloud to) {
        Vec2d cc = to.getCenter();
        Vec2d pc = getCenter();
        return new Vec2d(cc.x - pc.x, cc.y - pc.y);
    }

    public double estimateDistanceWithVelocity(RainCloud futureCloud, int timeFactor) {

        Vec2d pos = getFuturePosition(timeFactor);
        Vec2d f = futureCloud.getCenter();
        return pos.distance(f);
    }

    private Vec2d getFuturePosition(int timeFactor) {
        if(velocity != null){
            double x=getCenter().x+velocity.x*timeFactor;
            double y=getCenter().y+velocity.y*timeFactor;
            return new Vec2d(x,y);
        }
        return getCenter();
    }

    public RainPixel getFuturePixel(int _x, int _y, int timeFactor) {
      if(velocity != null){
          double x=_x - velocity.x*timeFactor;
          double y=_y - velocity.y*timeFactor;
          return getPixel((int)x,(int)y);
      }
        return getPixel(_x,_y);
    }

    public void setNonCovered(double nonCovered) {
        this.nonCovered = nonCovered;
    }

    public double getNonCovered() {
        return nonCovered;
    }
}
