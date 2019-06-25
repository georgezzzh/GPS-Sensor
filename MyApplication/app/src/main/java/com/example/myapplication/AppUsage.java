package com.example.myapplication;

import android.graphics.drawable.Drawable;

import java.util.List;

public class AppUsage implements Comparable {
    private String packageName;
    private String realName;
    private int frontTime;

    private static List<AppUsage> dataSet;
    //总使用时常
    public AppUsage(){}
    public static void setDataSet(List<AppUsage> dataSet) {
        AppUsage.dataSet = dataSet;
    }
    public static List<AppUsage> getDataSet(){
        return dataSet;
    }
    public AppUsage(String packageName, String realName, int frontTime) {
        this.packageName = packageName;
        this.realName = realName;
        this.frontTime = frontTime;
    }

    @Override
    public String toString() {
        return  realName + '\t' + frontTime + "s";
    }
    public String getEntireInfo(){
        return packageName + '\t' + realName + '\t' + frontTime + " s";
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setFrontTime(int frontTime) {
        this.frontTime = frontTime;
    }


    public String getPackageName() {
        return packageName;
    }

    public String getRealName() {
        return realName;
    }

    public int getFrontTime() {
        return frontTime;
    }


    @Override
    public int compareTo(Object o) {
        AppUsage other=(AppUsage) o;
        //return Long.valueOf(frontTime).compareTo(other.frontTime);
        return Integer.valueOf(other.frontTime).compareTo(frontTime);
    }
}
