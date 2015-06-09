import com.rainwatch.TestStrings;
import com.rainwatch.linkextract.DMILinkExtract;
import com.rainwatch.timeextract.GetTimeFromImage;
import com.rainwatch.timeextract.ImageTimeInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.*;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by Tim on 27-07-2014.
 */
public class TestDMIHTMLExtractor {
    public static String testsetsdir = "C:\\projects\\skywatch\\imageanalyzer\\src\\test\\resources\\com\\testsets";

    private void doTheTest(String html) {
        List<String> imgs = DMILinkExtract.images(html);
        for (String l : imgs) {
            //String tHex = DMILinkExtract.extractTimeInHex(l);
            //Date t = DMITimeExtractor.extractFromFileName(tHex);
            //System.out.println(""+(t.getTime()-refTime.getTime()));
            System.out.println(l);
        }
    }


    public static void main(String[] args) {
        try {
            downloadTestSet(10);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void downloadTestSet(int number) throws IOException {
        GetTimeFromImage timeExtractor = new GetTimeFromImage();
        String url = "http://www.dmi.dk/vejr/maalinger/radar-nedboer/";
        HttpClient conn = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        HttpResponse res = conn.execute(get);
        char[] html = IOUtils.toCharArray(res.getEntity().getContent());
        testsetsdir += number;

        FileUtils.forceMkdir(new File(testsetsdir));
        String theHtml = new String(html);
        System.out.println(theHtml);
        List<String> imgs = DMILinkExtract.images(theHtml);
        for (String l : imgs) {
            //String tHex = DMILinkExtract.extractTimeInHex(l);
            //Date t = DMITimeExtractor.extractFromFileName(tHex);
            //System.out.println(""+(t.getTime()-refTime.getTime()));
            System.out.println(l);
            int lastSlash = l.lastIndexOf("/");
            String imgName = l.substring(lastSlash + 1);

            String tempDest = testsetsdir + "/" + imgName;// count + ".png";
            File imgFile = new File(tempDest);
            if (imgFile.exists()) {
                continue;
            }
            HttpClient c = new DefaultHttpClient();
            HttpGet g = new HttpGet("http://www.dmi.dk/" + l);
            HttpResponse response = c.execute(g);
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                BufferedInputStream bis = new BufferedInputStream(instream);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imgFile));
                int inByte;
                while ((inByte = bis.read()) != -1) {
                    bos.write(inByte);
                }
                bis.close();
                bos.close();

            }
            ImageTimeInfo time = timeExtractor.getTimeInfo(imgFile);
            ObjectMapper om = new ObjectMapper();
            String jsonFile = tempDest.substring(0, tempDest.length() - 3) + "json";
            om.writeValue(new File(jsonFile), time);
        }
    }

}
