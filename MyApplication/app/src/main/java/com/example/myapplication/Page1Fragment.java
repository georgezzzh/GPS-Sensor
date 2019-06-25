package com.example.myapplication;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;

public class Page1Fragment extends Fragment {
    private View view;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater  inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page1, container, false);
        return view;
    }
    @Override
    public void onStart() {
        refreshPage();
        Log.i("demo","第一个view");
        super.onStart();
    }
    Page1Fragment(Context context){
        this.context=context;
    }
    private Context getApplicationContext(){
        return this.context;
    }
    private  void refreshPage(){

        Context applicationContext = getApplicationContext();
        List<AppUsage> dataSet =Tools.getAllAppUsage(applicationContext);
        AppUsage.setDataSet(dataSet);
        LinearLayout mainLinerLayout=getView().findViewById(R.id.main_body);
        mainLinerLayout.removeAllViews();
        int totalTimeUsage=0;
        PackageManager pm = getApplicationContext().getPackageManager();
        for (AppUsage appUsage:dataSet){
            if(appUsage.getFrontTime()==0) continue;
            totalTimeUsage+=appUsage.getFrontTime();
            TextView textView=new TextView(getApplicationContext());
            textView.setText(appUsage.getRealName()+"\t"+Tools.sec2hourWithMin(appUsage.getFrontTime()));
            ApplicationInfo appInfo = null;
            Drawable icon=null;
            try {
                appInfo = pm.getApplicationInfo(appUsage.getPackageName(), PackageManager.GET_META_DATA);
                icon = appInfo.loadIcon(pm);
                if(icon==null)
                    Log.i("demoFatal","app ICON is null");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if(icon!=null)
                icon.setBounds(0,0,120,120);
            textView.setCompoundDrawables(icon,null,null,null);
            textView.setHeight(140);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTextColor(Color.BLACK);
            textView.setCompoundDrawablePadding(100);
            mainLinerLayout.addView(textView);
        }

        TextView view1=new TextView(applicationContext);
        view1.setText("总使用时间为:"+Tools.sec2hourWithMin(totalTimeUsage));
        view1.setTextColor(Color.BLACK);
        view1.setHeight(150);
        mainLinerLayout.addView(view1,0);
    }
}
