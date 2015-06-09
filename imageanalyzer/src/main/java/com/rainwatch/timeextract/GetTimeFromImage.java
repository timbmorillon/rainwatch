package com.rainwatch.timeextract;

import com.github.axet.lookup.OCR;
import com.github.axet.lookup.common.ImageBinaryGrey;
import com.rainwatch.DMIOCR;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Tim on 11-10-2014.
 */
public class GetTimeFromImage {

    public ImageTimeInfo getTimeInfo(File img) throws IOException {

        OCR l = new OCR(0.90f);

        l.loadFontsDirectory(DMIOCR.class, new File("fonts"));

        l.loadFont(DMIOCR.class, new File("fonts", "font_1"));


            ImageBinaryGrey gimg = new ImageBinaryGrey(ImageIO.read(img));
        String str = l.recognize(gimg, 324, 452, 634, 476, l.getSymbols("font_1"));
            System.out.println(str);
        String[] split = str.split(" ");
        return new ImageTimeInfo(split[0],split[1],split[2], Calendar.getInstance().get(Calendar.MONTH));

    }
}
