package com.sonydafa.phoneUsage;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
/**
 * 利用加速度和磁场传感器
 * 指南针模块
 *
 */

public class Page2Fragment extends Fragment{

    ImageView compassCircle;
    View compassArrow;
    TextView compassInfo;
    private SensorManager sensorManager;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page2, container, false);
        return view;
    }
    @Override
    public void onStart() {
        Log.d("demo","是第三个视图 notify");

        compassInfo =getView().findViewById(R.id.compassInfo);
        compassCircle=getView().findViewById(R.id.sensorCompass);
        compassArrow=getView().findViewById(R.id.sensorArrow);

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager==null) return;
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        super.onStart();
    }
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        float[] accelerometerValues = new float[3];
        float[] magneticValues = new float[3];
        private float lastRotateDegree;

        @Override
        public void onSensorChanged(SensorEvent event) {
            //判断当前是加速度传感器还是地磁传感器
            switch (event.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    accelerometerValues = event.values.clone();
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    magneticValues = event.values.clone();
                    break;
            }
            float[] R = new float[9];
            float[] values = new float[3];
            //为R数组赋值
            SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticValues);
            //为values数组赋值，values中就已经包含手机在所有方向上旋转的弧度了
            SensorManager.getOrientation(R, values);
            //1.values[0]表示手机围绕Z轴旋转的弧度；
            //3.Math.toDegrees()把弧度转换成角度；
            //将计算出的旋转角度取反，用于旋转指南针背景图
            float rotateDegree = -(float)Math.toDegrees(values[0]);
            if(Math.abs(rotateDegree - lastRotateDegree) > 1){
                compassInfo.setText(degree2text((int)rotateDegree));
                RotateAnimation animation = new RotateAnimation(
                        lastRotateDegree, rotateDegree,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setFillAfter(true);
                compassCircle.startAnimation(animation);
                lastRotateDegree = rotateDegree;
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private float numFormat(float num){
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        String p=decimalFormat.format(num);
        return Float.parseFloat(p);
    }
    private String degree2text(int degree)
    {
        if(degree==0)
            return "正北";
        else if(0<degree && degree<90)
            return "北偏西"+(degree)+"°";
        else if(90==degree)
            return "正西";
        else if(90<degree && degree<180)
            return "南偏西"+(180-degree)+"°";
        else if(-90<degree && degree<0)
            return "北偏东"+(-degree)+"°";
        else if(-90==degree)
            return "正东";
        else if(-180<degree && degree<-90)
            return "南偏东"+(degree+180)+"°";
        else if(degree==-180)
            return "正南";
        else
            return "无法确定当前方向";
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

}
