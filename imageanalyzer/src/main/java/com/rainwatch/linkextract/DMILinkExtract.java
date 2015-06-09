package com.rainwatch.linkextract;


import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tim on 27-07-2014.
 */
public class DMILinkExtract {

    public static List<String> images(String html){
        ArrayList<String> result=new ArrayList<String>();
        String url=resolveURL(html);
        //url+= "&tx_dmiafghanistan_afghanistan%5DstreamId%5D=3&tx_dmiafghanistan_afghanistan%5DnumberOfImages%5D=18";
        url+= "&tx_dmiafghanistan_afghanistan[streamId]=3&tx_dmiafghanistan_afghanistan[numberOfImages]=20";
        HttpClient c=new DefaultHttpClient();
        HttpGet get=new HttpGet("http://www.dmi.dk"+url);
        try {
            HttpResponse res = c.execute(get);
            String json = IOUtils.toString(res.getEntity().getContent());
            ObjectMapper objectMapper=new ObjectMapper();
            ArrayList<Map> l = objectMapper.readValue(json, ArrayList.class);
            for(Map m:l){
                Object val = m.get("src");
                System.out.println(val.toString());
                result.add(val.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String resolveURL(String html) {
        Pattern pattern =Pattern.compile("var ajaxDataRequestUrl = \"\\S*\"");
        Matcher m = pattern.matcher(html);
        while(m.find()){
            String s=m.group();
            s=s.replace("var ajaxDataRequestUrl =","");
            s=s.replaceAll("\"","");
            s=s.replaceAll(" ","");
            return s;
        }
        return null;
    }

    public static String extractTimeInHex(String fullLink){
        int lastSlash = fullLink.lastIndexOf("/");
        int lastDot=fullLink.lastIndexOf('.');
        String s=fullLink.substring(lastSlash+1,lastDot);
        return s;
    }
}
