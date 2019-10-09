package com.sonydafa.phoneUsage;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sonydafa.phoneUsage.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class Page3Fragment extends Fragment {
    private View view;
    private static long lastUpdateTime=System.currentTimeMillis();
    //传感器
    private SensorManager sensorManager;
    private SensorEventListener listener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //换算百分比
            float value=event.values[0]/5000*100;
            circular(value);
            Log.i("info","更新进度条,value="+value);
            View view = getView();
            if(view==null) return;
            TextView lightDegree=view.findViewById(R.id.lightDegree);
            String text="光照强度为: "+event.values[0]+"lux";
            lightDegree.setText(text);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page3, container, false);
        return view;
    }
    public void circular(float x)
    {
            CircularProgressBar circularProgressBar = getView().findViewById(R.id.circularProgressBar);
            // Set Progress
            //circularProgressBar.setProgress(65f);
            // or with animation
            circularProgressBar.setProgressWithAnimation(x, 300L); // =1s
            // Set Progress Max
            circularProgressBar.setProgressMax(200f);
            // Set ProgressBar Color
            //circularProgressBar.setProgressBarColor(Color.BLACK);
            // or with gradient
            circularProgressBar.setProgressBarColorStart(Color.GRAY);
            circularProgressBar.setProgressBarColorEnd(Color.RED);
            circularProgressBar.setProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);
            // Set background ProgressBar Color
            //circularProgressBar.setBackgroundProgressBarColor(Color.GRAY);
            // or with gradient
            circularProgressBar.setBackgroundProgressBarColorStart(Color.WHITE);
            circularProgressBar.setBackgroundProgressBarColorEnd(Color.GREEN);
            circularProgressBar.setBackgroundProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);
            // Set Width
            circularProgressBar.setProgressBarWidth(7f); // in DP
            circularProgressBar.setBackgroundProgressBarWidth(3f); // in DP
            // Other
            circularProgressBar.setRoundBorder(true);
            circularProgressBar.setStartAngle(180f);
            circularProgressBar.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);
    }
    @Override
    public void onStart() {
        Log.i("info","fragments 第三个视图");
        Button btn=getView().findViewById(R.id.compass);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SensorActivity.class);
                startActivity(intent);
            }
        });
        sensorManager=(SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager==null) return;
        Sensor lightSensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(listener,lightSensor,SensorManager.SENSOR_DELAY_UI);
        super.onStart();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(sensorManager!=null){
            sensorManager.unregisterListener(listener);
        }
    }
}
