package com.rainwatch;

import java.util.List;

/**
 * Created by Tim on 30-07-2014.
 */
public class CloudMerger {
    private List<RainCloud> clouds;

    public CloudMerger(List<RainCloud> clouds) {

        this.clouds = clouds;
    }

    public List<RainCloud> merge(){
        return mergeRecursively(clouds);
    }

    public List<RainCloud> mergeRecursively(List<RainCloud> cloudList){
        for(RainCloud c1:cloudList){
            for(RainCloud c2:cloudList){
                if(c1 != c2){
                   double d= c1.calcFrontDistanceAndRemember(c2);
                   double c1_r=c1.estimatedRadius();
                   double c2_r=c2.estimatedRadius();
                   double r = Math.max(c1_r, c2_r);
                   if(d<(r*0.05)){
                       cloudList.remove(c1);
                       cloudList.remove(c2);
                       cloudList.add(new RainCloud(c1,c2));
                       return mergeRecursively(cloudList);
                   }
                }
            }
        }
        return cloudList;
    }
}
