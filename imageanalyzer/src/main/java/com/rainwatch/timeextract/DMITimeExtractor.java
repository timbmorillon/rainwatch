package com.rainwatch.timeextract;

import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by Tim on 27-07-2014.
 */
public class DMITimeExtractor {

    public static Date extractFromFileName(String nanoTimeAsHex){
        BigInteger asInt = new BigInteger(nanoTimeAsHex,16);
        double d = asInt.doubleValue();
        d=d/1000; //to millis
        d-=68320700233l;
        Date time=new Date((long) d);
        return time;
    }
}
