package com.rainwatch;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by Tim on 08-07-2014.
 */
public class DMIPixelEvaluator implements IPixelEvaluator {
    static HashMap<Color,RainSeverity> severities= new HashMap<Color, RainSeverity>();

    static{
        //fff0c8,ffe8b0,f0dc96,f5cf64
        //fac132,fab400,ffa000,f68900
        //ee7200,e65b00,de4500,d72e00,cf1700,c80000
        severities.put(new Color(0xff,0xf0,0xc8),RainSeverity.LOW);
        severities.put(new Color(0xff,0xe8,0xb0),RainSeverity.LOW);
        severities.put(new Color(0xf0,0xdc,0x96),RainSeverity.LOW);
        severities.put(new Color(0xf5,0xcf,0x64),RainSeverity.LOW);

        severities.put(new Color(0xfa,0xc1,0x32),RainSeverity.MIDDLE);
        severities.put(new Color(0xfa,0xb4,0x00),RainSeverity.MIDDLE);
        severities.put(new Color(0xff,0xa0,0x00),RainSeverity.MIDDLE);
        severities.put(new Color(0xf6,0x89,0x00),RainSeverity.MIDDLE);

        severities.put(new Color(0xee,0x72,0x00),RainSeverity.HIGH);
        severities.put(new Color(0xe6,0x5b,0x00),RainSeverity.HIGH);
        severities.put(new Color(0xde,0x45,0x00),RainSeverity.HIGH);
        severities.put(new Color(0xd7,0x2e,0x00),RainSeverity.HIGH);
        severities.put(new Color(0xcf,0x17,0x00),RainSeverity.HIGH);
        severities.put(new Color(0xc8,0x00,0x00),RainSeverity.HIGH);

        severities.put(new Color(0x06,0x06,0xa6),RainSeverity.LIGHTNING);
        severities.put(new Color(0x64,0x64,0xff),RainSeverity.LIGHTNING);
        severities.put(new Color(0x8c,0xa0,0xfe),RainSeverity.LIGHTNING);





    }
    @Override
    public RainSeverity getSeverity(BufferedImage img,int x, int y) {
        Color c = new Color(img.getRGB(x, y));
        RainSeverity temp = severities.get(c);
        if(temp != null){
            return temp;
        }
        return RainSeverity.NONE;
    }
}
