package com.example.android.quakereport;

/**
 * Created by sejal on 14-10-2017.
 */

public class earthquake {
    private double mag;
    private String loc;
    private long date;
    private String murl;

    earthquake(double magni,String loca,long dates,String url){
        mag=magni;
        loc=loca;
        date=dates;
        murl=url;
    }

    public double getMag(){
        return mag;
    }

    public String getLoc(){
        return loc;
    }

    public long getDate(){
        return date;
    }

    public String geturl(){
        return murl;
    }

}
