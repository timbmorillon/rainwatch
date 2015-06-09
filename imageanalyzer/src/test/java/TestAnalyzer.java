import com.rainwatch.*;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Tim on 07-07-2014.
 */
public class TestAnalyzer {

    /*@Test
    public void tst() throws IOException {
        BufferedImage bi = ImageIO.read(getClass().getResource("/com/rainwatch/ocr.png"));


        RadarImage analyzer = new RadarImage(bi,new DMIPixelEvaluator());
        analyzer.calc();


        assertTrue(analyzer.getCloudList().size()>0);

        ArrayList<RainCloud> clouds = analyzer.getCloudList();
        setToBlack(bi,clouds);
        ImageIO.write(bi,"png",new File("test.png"));
    }*/

    /*@Test
   public void tstWLightning() throws IOException {
        List<RadarImage> images=new ArrayList();

        for(int i=1;i<16;i++) {
            BufferedImage bi = ImageIO.read(getClass().getResource("/com/rainwatch/"+i+".png"));


            RadarImage analyzer = new RadarImage(bi, new DMIPixelEvaluator());
            analyzer.calc();
            images.add(analyzer);

            assertTrue(analyzer.getCloudList().size() > 0);



        }
        CloudConnectionAnalyzer analyzer = new CloudConnectionAnalyzer(images);
        analyzer.analyze();

        for(int i=4;i<images.size();i++){
            ErrorEstimation errorEstimation=new ErrorEstimation(images.get(i-1),images.get(i));
            errorEstimation.estimateError(1);
        }

        analyzer.paint(dataset, TestDMIHTMLExtractor.testsetsdir);

    }*/

    @Test
    public void tstSingleSet() throws IOException {
        tstGeneral(10);
    }

    public void tstGeneral(int dataset) throws IOException {
        List<RadarImage> images=new ArrayList();

        for(int i=1;i<20;i++) {
            BufferedImage bi = ImageIO.read(getClass().getResource("/com/testsets/"+dataset+"/"+i+".png"));
            RadarImage analyzer = new RadarImage(bi, new DMIPixelEvaluator());
            analyzer.calc();
            images.add(analyzer);
            assertTrue(analyzer.getCloudList().size() > 0);
        }
        CloudConnectionAnalyzer analyzer = new CloudConnectionAnalyzer(images);
        analyzer.analyze();

        for(int i=4;i<images.size();i++){
            ErrorEstimation errorEstimation=new ErrorEstimation(images.get(i-1),images.get(i));
            errorEstimation.estimateError(1);
        }

        analyzer.paint(dataset,TestDMIHTMLExtractor.testsetsdir);

    }


}
