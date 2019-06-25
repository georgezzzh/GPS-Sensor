package com.example.myapplication;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page2Fragment extends Fragment {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page2, container, false);
        return view;
    }

    @Override
    public void onStart() {
        Log.i("demo","是第三个视图 notify");
        PieChart pc=getView().findViewById(R.id.pc);
        List<PieEntry> yVals = new ArrayList<>();
        List<AppUsage> dataSet = AppUsage.getDataSet();
        if(dataSet==null){
            Log.i("demoFatal","dataSet is null");
            return;
        }
        List<Integer> colors = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();  //xVals用来表示每个饼块上的内容
        String[] colorsRepo=new String[]{"#0066FF","#33FF00","#9966FF","#9900FF","#CC0066","#FF9900","#999999","#00CCFF"};
        int bigPartitionTime=0;
        int totalTime=0;
        for(int i=0;i<dataSet.size();i++){
            AppUsage appUsage = dataSet.get(i);
            int time=appUsage.getFrontTime();
            totalTime+=time;
            if(i>5) continue;
            bigPartitionTime+=time;
            yVals.add(new PieEntry(time,appUsage.getRealName()));
            xValues.add(appUsage.getRealName());
            colors.add(Color.parseColor(colorsRepo[i]));
        }
        //其余未算完算作其他
        if(dataSet.size()>6){
            Log.i("demo","others is:"+(totalTime-bigPartitionTime));
            yVals.add(new PieEntry(totalTime-bigPartitionTime,"Others"));
            xValues.add("Others");
            colors.add(Color.parseColor(colorsRepo[colorsRepo.length-1]));
        }

        PieDataSet pieDataSet = new PieDataSet(yVals, "");
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setDrawValues(true);
        pc.setData(pieData);
        pc.setUsePercentValues(true);//比例显示
        //pc.setDrawHoleEnabled(false);//关闭空洞
        pc.setHoleRadius(0.3f);
        pc.setRotationEnabled(false);
        pc.setDrawEntryLabels(false);

        Legend legend = pc.getLegend();
        legend.setWordWrapEnabled(true);
        Description description=new Description();
        description.setText("应用使用时长");
        pc.setDescription(description);
        pc.invalidate();
        super.onStart();
    }

}
