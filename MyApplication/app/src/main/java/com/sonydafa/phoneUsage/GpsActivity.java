package com.sonydafa.phoneUsage;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GpsActivity extends Activity {
    private  MapView mMapView;
    public LocationClient mLocationClient;
    public BDAbstractLocationListener myListener;
    private BaiduMap mBaiduMap;
    FileTransfer fileTransfer;
    //latitude & longitude
    private double lat;
    private double lon;
    private String fileName="gps_info.csv";
    //
    List<LatLng> points;
    //添加当前位置的小红旗
    private void setMarker() {
        Log.v("map","setMarker : lat : "+ lat+" lon : " + lon);
        //定义Maker坐标点
        LatLng point = new LatLng(lat, lon);
        fileTransfer.exportFile(fileName,System.currentTimeMillis()+","+lat+","+lon+"\n");
        //构建Marker图标
        /*
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.flag);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        */
        //
        //构建折线点坐标
        points.add(point);
        if(points.size()<2)
            return;
        //设置折线的属性
        OverlayOptions mOverlayOptions = new PolylineOptions()
                .width(10)
                .color(0xAAFF0000)
                .points(points);
        Overlay mPolyline = mBaiduMap.addOverlay(mOverlayOptions);
        mBaiduMap.setMyLocationEnabled(true);
        //清除之前绘制的点
        LatLng last_latLng = points.get(points.size() - 1);
        points.clear();
        points.add(last_latLng);

    }
    //设置整张地图的显示
    private void setUserMapCenter() {
        Log.v("baiduMap","set UserMapCenter lat : " + lat+" lon : " + lon);
        LatLng latLng = new LatLng(lat,lon);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().target(latLng).zoom(18).build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            setMarker();
            setUserMapCenter();
            Log.v("baiduMap","onReceiveLocation: lat : " + lat+" lon : " + lon);
            Toast.makeText(getApplicationContext(),"GPS发生变化,lat:"+lat+"lon:"+lon,Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 该函数设置了位置地图的选项
     */
    private void initLocationOption() {
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        LocationClientOption locationOption = new LocationClientOption();
        //声明LocationClient类实例并配置定位参数
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd09ll");
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(1000);
        //可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(true);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(locationOption);
        //开始定位
        mLocationClient.start();
    }
    private void fileInit(){
        fileTransfer = new FileTransfer(getApplicationContext());
        getApplicationContext().deleteFile(fileName);
        File dir=getApplicationContext().getExternalFilesDir("");
        if(dir!=null){
            String path = dir.getAbsoluteFile() + "/" + fileName;
            Log.i("map","gps_file path is:"+path);
            File gps_file = new File(path);
            if(gps_file.exists()){
                boolean delStatus = gps_file.delete();
                File file1 = new File(path);
            }
            fileTransfer.exportFile(fileName,"time,latitude,longitude\n");
        }else{
            Log.e("map","获取/Android/data/sonydafa/files出错");
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.txt_content);
        //类成员变量的实例化
        mMapView =findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        myListener= new MyLocationListener();
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        points = new ArrayList<>();
        //初始化文件读写,若存在，则删除
        fileInit();
        initLocationOption();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
}