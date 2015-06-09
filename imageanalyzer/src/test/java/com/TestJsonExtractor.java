package com;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Tim on 11-10-2014.
 */
public class TestJsonExtractor {
String s="[  \n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/7\\/9\\/f\\/f\\/e\\/54392213eff97.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/c\\/b\\/b\\/3\\/b\\/54392453b3bbc.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/f\\/3\\/c\\/d\\/5\\/543926c25dc3f.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/c\\/1\\/4\\/0\\/3\\/5439291e3041c.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/6\\/6\\/3\\/7\\/6\\/54392b7167366.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/a\\/e\\/e\\/d\\/c\\/54392dcccdeea.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/6\\/c\\/e\\/9\\/8\\/5439302789ec6.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/d\\/1\\/7\\/3\\/e\\/5439325ce371d.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/0\\/e\\/9\\/f\\/2\\/543934d42f9e0.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/d\\/d\\/b\\/8\\/4\\/5439370e48bdd.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/d\\/9\\/8\\/6\\/6\\/5439396c6689d.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/9\\/0\\/0\\/4\\/7\\/54393bc374009.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/a\\/9\\/1\\/e\\/3\\/54393e3f3e19a.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/5\\/9\\/2\\/d\\/9\\/5439408c9d295.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/4\\/e\\/6\\/0\\/b\\/543942f1b06e4.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/7\\/0\\/6\\/1\\/0\\/5439452401607.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/9\\/1\\/3\\/a\\/1\\/543947951a319.png\"\n" +
        "   },\n" +
        "   {  \n" +
        "      \"src\":\"\\/uploads\\/tx_dmidatastore\\/webservice\\/2\\/6\\/c\\/0\\/1\\/543949f610c62.png\"\n" +
        "   }\n" +
        "]";
    @Test
    public void test() throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        ArrayList<Map> l = objectMapper.readValue(s, ArrayList.class);
        for(Map m:l){
            Object val = m.get("src");
            System.out.println(val.toString());
        }
    }
}
