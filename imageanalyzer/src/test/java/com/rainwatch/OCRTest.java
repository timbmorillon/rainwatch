package com.rainwatch; /**
 * Created by Tim on 27-07-2014.
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.github.axet.lookup.Capture;
import com.github.axet.lookup.OCR;
import com.github.axet.lookup.common.ImageBinaryGrey;

import javax.imageio.ImageIO;

public class OCRTest {

    static String testSet="2";

    static public void main(String[] args) throws IOException {
        OCR l = new OCR(0.90f);

        l.loadFontsDirectory(DMIOCR.class, new File("fonts"));

        l.loadFont(DMIOCR.class, new File("fonts", "font_1"));

        String dir = "C:/Users/Tim/IdeaProjects/imageanalyzer/src/test/resources/com/testsets/";
        dir+=testSet;

        for(int i=1;i<15;i++) {
            String str = "";

            // recognize using only one family set and rectangle
            //ImageBinaryGrey img = new ImageBinaryGrey(Capture.load(OCRTest.class, ""+i+".png"));
            ImageBinaryGrey img = new ImageBinaryGrey(ImageIO.read(new File(dir + "/" + i + ".png")));
            str = l.recognize(img, 424, 452, 634, 476, l.getSymbols("font_1"));
            System.out.println(str);
        }
    }
}
