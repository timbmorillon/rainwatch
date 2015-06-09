package com.rainwatch;

import java.awt.*;

/**
 * Created by Tim on 08-07-2014.
 */
public enum RainSeverity {

    NONE,LOW,MIDDLE,HIGH, LIGHTNING;

    public boolean isRain() {
        return this.compareTo(NONE)!=0;
    }
}
