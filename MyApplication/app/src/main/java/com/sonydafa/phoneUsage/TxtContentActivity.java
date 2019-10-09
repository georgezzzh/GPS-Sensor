package com.sonydafa.phoneUsage;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

public class TxtContentActivity  extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.txt_content);
        FileTransfer fileTransfer = new FileTransfer(getApplicationContext());
        String txt = fileTransfer.readFileData(SensorActivity.fileName);
        TextView txtView = findViewById(R.id.txtContent);
        if(txt!=null)
            txtView.setText(txt);
        else
            Log.e("error","文件未读取到");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
