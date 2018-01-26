package com.example.android.asynctask;


public class EarthQuake {
    private double mMagnitute;
    private String mLocation;
    private long mTimeInMillisecond;
    private String mUrl;

    public EarthQuake(double magnitute, String location, long timeInMillisecond, String url) {
        mMagnitute = magnitute;
        mLocation = location;
        mTimeInMillisecond = timeInMillisecond;
        mUrl = url;
    }

    public double getMagnitute() {
        return mMagnitute;
    }

    public String getLocation() {
        return mLocation;
    }

    public long getTimeInMillisecond() {
        return mTimeInMillisecond;
    }

    public String getUrl() {
        return mUrl;
    }
}
