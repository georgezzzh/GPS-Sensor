package com.sonydafa.phoneUsage;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//import org.joda.time.DateTime;

public class Tools {
    private static final String thisPackageName="com.sonydafa.myapplication";
    //保存的数据集合
    private static List<AppUsage> dataSet=new LinkedList<>();

    public static List<AppUsage> getDataSet() {
        return dataSet;
    }
    public static String sec2hourWithMin(int second){
        if(second<60) return second+" s";
        else if(second>60 && second<60*60) return second/60+" m "+second%60+" s";
        else{
            int h=second/3600;
            second=second%3600;
            int min=second/60;
            return h+" h "+min+" m "+second%60+" s";
        }
    }
    public static void getTimePerHour()
    {
        //划分为24个区间，一个小时一个区间
        /*
        DateTime[] array=new DateTime[24];
        DateTime aux=new DateTime();
        for(int i=0;i<24;i++){
            array[i] = new DateTime(aux.getYear(),aux.getMonthOfYear(),aux.getDayOfMonth(),i,0,0);
        }
        for(DateTime tmp:array){
            Log.i("demo",tmp.getMillis()+"");
            Log.i("demo",new Date(tmp.getMillis()).toString());
        }
        */
    }
    //return instance of AppUsage,which include AppName,package name, during time
    public static List<AppUsage> getAllAppUsage(Context context){
        //已经统计过了，重新统计
        if(dataSet.size()!=0)
            dataSet.clear();
        long statisticStart=System.currentTimeMillis();
        UsageStatsManager usm=(UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long start =calendar.getTime().getTime();
        long end=System.currentTimeMillis();
        if(usm==null){
            Log.e("fatal","usm(UsageStatsManger) is null");
            return null;
        }
        UsageEvents usageEvents = usm.queryEvents(start, end);
        Map<String,Integer> allApp=getAllAppUseTime(usageEvents);
        PackageManager pm = context.getPackageManager();
        for(Map.Entry entry:allApp.entrySet()){
            int time=(Integer) entry.getValue()/(1000);
            String packageName=entry.getKey().toString();
            String appRealName="";
            try {
                ApplicationInfo appInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                appRealName=pm.getApplicationLabel(appInfo).toString();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            AppUsage temp=new AppUsage(packageName,appRealName,time);
            dataSet.add(temp);
        }
        Collections.sort(dataSet);
        long statisticEnd=System.currentTimeMillis();
        Log.i("demo","统计时长的耗时:"+(statisticEnd-statisticStart)+"ms");
        //打印一下应用信息
        for(AppUsage tmp:dataSet)
            Log.i("appTime",tmp.toString());
        //设置AppUsage中的dataSet，后续做饼图时会用到
        //AppUsage.setDataSet(dataSet);
        return dataSet;
    }
    //返回 {"AppName":AppTime}
    private static Map<String,Integer> getAllAppUseTime(UsageEvents usageEvents){
        Map<String,Long> openTime=new HashMap<>();
        Map<String,Integer> allApp=new HashMap<>();
        while (usageEvents.hasNextEvent()){
            UsageEvents.Event event = new UsageEvents.Event();
            usageEvents.getNextEvent(event);
            String packageName;
            long timeStamp;
            switch (event.getEventType()) {
                case UsageEvents.Event.MOVE_TO_FOREGROUND:
                    packageName = event.getPackageName();
                    timeStamp = event.getTimeStamp();
                    openTime.put(packageName,timeStamp);
                    String startInfo="应用为:"+packageName+"\t出现在前台的时刻为:"+new Date(timeStamp);
                    break;
                case UsageEvents.Event.MOVE_TO_BACKGROUND:
                    packageName = event.getPackageName();
                    timeStamp = event.getTimeStamp();
                    long openTimeStamp=timeStamp;
                    if(openTime.get(packageName)!=null)
                        openTimeStamp=openTime.get(packageName);
                    int diff=(int)(timeStamp-openTimeStamp);
                    int thisAppTotalTime=allApp.get(packageName)!=null?allApp.get(packageName):0;
                    allApp.put(packageName,thisAppTotalTime+diff);
                    String endInfo="应用为:"+packageName+"\t出现在后台的时刻为:"+new Date(timeStamp);
                    break;
            }
        }
        //加上本应用的最后时常，因为没有推出到后台，所以while循环中无法统计到
         if(openTime.get(thisPackageName)==null||allApp.get(thisPackageName)==null){
             Log.i("demoFatal","This packageName has error");
             return allApp;
         }
         return allApp;
    }
}
