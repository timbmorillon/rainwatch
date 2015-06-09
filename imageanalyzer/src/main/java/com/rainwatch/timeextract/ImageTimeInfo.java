package com.rainwatch.timeextract;

/**
 * Created by Tim on 11-10-2014.
 */
public class ImageTimeInfo {
    String dayInMonth;
    String year;
    String time;
    private int month;

    public ImageTimeInfo(String dayInMonth, String year, String time, int month) {
        this.dayInMonth = dayInMonth;
        this.year = year;
        this.time = time;
        this.month = month;
    }

    public String getDayInMonth() {
        return dayInMonth;
    }

    public String getYear() {
        return year;
    }

    public String getTime() {
        return time;
    }

    public int getMonth() {
        return month;
    }
}
