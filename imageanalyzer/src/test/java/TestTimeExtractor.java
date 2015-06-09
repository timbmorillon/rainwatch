import com.rainwatch.timeextract.DMITimeExtractor;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Tim on 27-07-2014.
 */
public class TestTimeExtractor {

    @Test
    public void testExtractor(){
        Date t = DMITimeExtractor.extractFromFileName("53d3fe8d96d63");
        System.out.println(t);

        t = DMITimeExtractor.extractFromFileName("53d400e13f5f3");
        System.out.println(t);

    }
}
