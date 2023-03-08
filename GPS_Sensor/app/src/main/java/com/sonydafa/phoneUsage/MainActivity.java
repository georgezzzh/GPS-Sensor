package com.sonydafa.phoneUsage;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gps_sensor.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    //设置碎片化界面
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentManager = getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.container, new Page1Fragment(getApplicationContext()));
                    transaction.commit();
                    Log.i("demo","page1");
                    return true;
                case R.id.navigation_dashboard:
                    transaction.replace(R.id.container, new Page2Fragment());
                    transaction.commit();
                    Log.i("demo","page2");
                    return true;
                case R.id.navigation_notifications:
                    transaction.replace(R.id.container, new Page3Fragment());
                    transaction.commit();
                    Log.i("demo","page3");
                    return true;
            }
            return false;
        }
    };
    private void setDefaultFragment() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        Context applicationContext = getApplicationContext();
        transaction.replace(R.id.container, new Page1Fragment(applicationContext)).commit();
        Log.i("demo","初始化页面完成");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //进行菜单管理
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefaultFragment();
        BottomNavigationView navigation =findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
