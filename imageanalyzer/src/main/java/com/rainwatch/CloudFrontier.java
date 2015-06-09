package com.rainwatch;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tim on 30-07-2014.
 */
public class CloudFrontier {
    private RainCloud rainCloud;
    RainPixel cand = null;
    boolean hasBeenOutside;
    Set<RainPixel> front = new HashSet<RainPixel>();

    public CloudFrontier(RainCloud rainCloud) {
        this.rainCloud = rainCloud;
        RainPixel firstPixel = rainCloud.getRandomPixel();

        RainPixel temp;
        while ((temp = rainCloud.getPixel(firstPixel.getX(), firstPixel.getY() - 1)) != null) {
            firstPixel = temp;
        }


        front.add(firstPixel);
        RainPixel current = firstPixel;
        for(int i=0;i<10000;i++) {
            reset();
            discoverRecursively(current);
            if(cand==null || cand==firstPixel){
                break;
            }
            front.add(current=cand);
        }
    }

    public CloudFrontier(CloudFrontier frontier, CloudFrontier frontier1) {
        front.addAll(frontier.front);
        front.addAll(frontier1.front);
    }

    private void reset() {
        cand=null;
        hasBeenOutside=false;
    }

    private void discoverRecursively(RainPixel current) {
        eval(rainCloud.getPixel(current.getX() - 1, current.getY()));
        eval(rainCloud.getPixel(current.getX() - 1, current.getY() - 1));
        eval(rainCloud.getPixel(current.getX(), current.getY() - 1));
        eval(rainCloud.getPixel(current.getX() + 1, current.getY() - 1));
        eval(rainCloud.getPixel(current.getX() + 1, current.getY()));
        eval(rainCloud.getPixel(current.getX() + 1, current.getY() + 1));
        eval(rainCloud.getPixel(current.getX(), current.getY() + 1));
        eval(rainCloud.getPixel(current.getX() - 1, current.getY() + 1));
        eval(rainCloud.getPixel(current.getX() - 1, current.getY()));
    }

    private void eval(RainPixel temp) {
        //if (cand == null && temp != null && !front.contains(temp)) {
        //    cand = temp;
        //}
        if (temp == null) {
            hasBeenOutside = true;
        } else if (hasBeenOutside && !front.contains(temp)) {
            cand = temp;
            hasBeenOutside=false;
        }else{
            hasBeenOutside=false;
        }
    }

    public void paint(BufferedImage img) {
        for(RainPixel p:front) {
            img.setRGB(p.getX(), p.getY(), Color.ORANGE.getRGB());
        }
    }

    public double distance(CloudFrontier frontier) {
        double d=1000000;
        for(RainPixel p:front){
            for(RainPixel p2:frontier.front){
                double temp = p.distance(p2);
                if(temp < d){
                    d=temp;
                }
            }
        }
        return d;
    }
}
