package com.rainwatch;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Tim on 28-07-2014.
 */
public class CloudConnectionAnalyzer {


    private List<RadarImage> images;

    public CloudConnectionAnalyzer(List<RadarImage> images) {
        this.images = images;
    }

    public void analyze(){

        findSameClouds();
        estimateVelocities();

        deduceMissingVelocities();
    }

    private void deduceMissingVelocities() {
        for(int i=0;i<images.size();i++){
            RadarImage img = images.get(i);
            //img.deduceMissingVelocities(new ImageSeries());
        }
    }

    private void estimateVelocities() {
        for(RadarImage img:images){
            img.estimateVelocities(AlgorithmConstants.NUM_IMAGES_TO_LOOK_BACK);
        }
    }

    private void findSameClouds() {
        for(int i=1;i<images.size();i++){
            RadarImage prev = images.get(i - 1);
            RadarImage current = images.get(i);
            current.findSameClouds(prev);
        }
    }

    public void paint(int dataset, String testsetsdir) throws IOException {
        int i=1;
        for(RadarImage img:images){
            img.paint();
            String dir=testsetsdir+ "/"+dataset+"_res/";
            FileUtils.forceMkdir(new File(dir));
            ImageIO.write(img.getImage(), "png", new File(dir + i + ".png"));
            i++;
        }
    }
}
