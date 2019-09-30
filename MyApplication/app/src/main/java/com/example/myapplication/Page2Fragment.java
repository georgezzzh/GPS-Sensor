package com.example.myapplication;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page2Fragment extends Fragment {
    private View view;
    private Map<String,String> color4app=new HashMap<>();
    //初始化块，应用与主题色的映射
    {
        color4app.put("com.tencent.mobileqq","#CC0000");
        color4app.put("com.zhihu.android","#0066FF");
        color4app.put("com.android.chrome","#FFCC33");
        color4app.put("com.bilibili.app.in","#FF6699");
        color4app.put("com.tencent.mm","#00FF66");
        color4app.put("com.google.android.youtube","#FF0033");
        color4app.put("com.taobao.taobao","#FF6600");
        color4app.put("com.tencent.androidqqmail","#FFFF66");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page2, container, false);
        return view;
    }
    @Override
    public void onStart() {
        Log.d("demo","是第三个视图 notify");
        PieChart pc=getView().findViewById(R.id.pc);
        List<PieEntry> yVals = new ArrayList<>();
        List<AppUsage> dataSet = Tools.getDataSet();
        if(dataSet==null){
            Log.e("fatal","dataSet is null");
            super.onStart();
            return;
        }
        List<Integer> colors = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();  //xVals用来表示应用的名称
        String[] colorsRepo=new String[]{"#33FF00","#9966FF","#9900FF","#CC0066","#FF9900","#999999","#00CCFF"};
        int bigPartitionTime=0;
        int totalTime=0;
        for(int i=0;i<dataSet.size();i++){
            AppUsage appUsage = dataSet.get(i);
            int time=appUsage.getFrontTime();
            totalTime+=time;
            if(i>5) continue;
            bigPartitionTime+=time;
            if(appUsage.getRealName().equals(""))
                yVals.add(new PieEntry(time,"已删除应用"));
            else
                yVals.add(new PieEntry(time,appUsage.getRealName()));
            xValues.add(appUsage.getRealName());
            String color=color4app.get(appUsage.getPackageName());
            if(color==null||color.equals(""))
                colors.add(Color.parseColor(colorsRepo[i%colorsRepo.length]));
            else
                colors.add(Color.parseColor(color));
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
