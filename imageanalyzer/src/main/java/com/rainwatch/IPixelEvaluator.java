package com.rainwatch;

import java.awt.image.BufferedImage;

/**
 * Created by Tim on 08-07-2014.
 */
public interface IPixelEvaluator {



    RainSeverity getSeverity(BufferedImage img,int x, int y);
}
