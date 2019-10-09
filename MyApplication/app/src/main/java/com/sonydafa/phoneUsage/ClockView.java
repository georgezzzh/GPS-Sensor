package com.sonydafa.phoneUsage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.annotation.Nullable;


@SuppressLint("AppCompatCustomView")
public class ClockView extends ImageView {

    private float mWidth,mHeight;
    private Paint outCirclePaint,degreePaint;
    private String[] orientation=new String[]{"E","S","W","N"};
    public ClockView(Context context) {
        this(context,null);
    }
    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }
    private void initData(){
        outCirclePaint = new Paint();
        outCirclePaint.setStrokeWidth(4);
        outCirclePaint.setAntiAlias(true);
        outCirclePaint.setDither(true);
        //设置空心
        outCirclePaint.setStyle(Paint.Style.STROKE);
        degreePaint = new Paint();
        degreePaint.setStrokeWidth(2);
        //抗锯齿
        degreePaint.setAntiAlias(true);
    }
    @Override
    protected void onDraw(Canvas canvas) {

        //第一步，画一个外面的表盘（圆)
        canvas.drawCircle(mWidth/2,mHeight/2,mWidth/2,outCirclePaint);
        //画刻度,通过旋转画布的方法
        int cnt=24;
        for (int i = 0; i <= cnt;i++){
            String degree = String.valueOf(i*15);
            if (i==6||i==12||i==18 || i==24){
                //特殊角度换成方向显示"W","E","N","S"
                degree=orientation[i/6-1];
                degreePaint.setStrokeWidth(3);
                degreePaint.setTextSize(30);
                canvas.drawLine(mWidth/2,mHeight/2-mWidth/2,mWidth/2,mHeight/2-mWidth/2+30,degreePaint);
                degreePaint.setColor(Color.RED);
                canvas.drawText(degree,
                        mWidth/2-degreePaint.measureText(degree)/2,
                        mHeight/2-mWidth/2 + 60,
                        degreePaint);
            }else if(i!=0){
                    degreePaint.setStrokeWidth(2);
                    degreePaint.setTextSize(20);
                    degreePaint.setColor(Color.BLACK);
                    canvas.drawLine(mWidth/2,mHeight/2-mWidth/2,mWidth/2,mHeight/2-mWidth/2+15,degreePaint);
                    canvas.drawText(degree,
                            mWidth/2-degreePaint.measureText(degree)/2,
                            mHeight/2-mWidth/2 + 40,
                            degreePaint);
            }
            canvas.rotate((float)360/cnt,mWidth/2,mHeight/2);
        }
        canvas.save();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
    }
}