**page1Fragment.java是百度地图绘制轨迹**  
<img src="https://github.com/georgezzzh/GPS-Sensor/raw/master/readme_resource/baiduMap_sensor.png" alt="map_sensor" width="300"/>  

**page3Fragment.java是光线传感器**  
<img src="https://github.com/georgezzzh/GPS-Sensor/raw/master/readme_resource/light_sensor.png" alt="light_sensor" width="300"/>

**page2Fragment.java是指南针传感器**  
<img src="https://github.com/georgezzzh/GPS-Sensor/raw/master/readme_resource/compass_sensor.png" alt="compass_sensor" width="300"/>  

**注：有朋友反馈，下载下来无法直接使用，因为Android 6之后的动态权限问题，可以调整代码中权限的问题。**

[下载地址](https://github.com/georgezzzh/GPS-Sensor/blob/master/MyApplication3/app/release/app-release.apk?raw=true)

---
自行编译说明：


克隆整个项目到本地，选择GPS_sensor目录导入到android studio中。

以下方式适用于2023年3月8日下载的Android Studio Electric Eel | 2022.1.1 Patch 2。

编译说明：
1. 建议使用Android Studio 2022,中国大陆能够直连（无需网络代理）
2. 安装完Android studio之后，设置网络代理（开代理软件，设置好，具体百度），把gradle和Android SDK这些东西下载好
3. 导入GPS_Sensor之后，在Android Studio的左上角`Android`这里切换成`Project`(见下图), 然后在`app/libs`目录找到`BaiduLBS_Android.jar`，点击右键，选择Add as Library。
具体参考百度地图API文章https://lbsyun.baidu.com/index.php?title=androidsdk/guide/create-project/androidstudio

![](https://github.com/georgezzzh/GPS-Sensor/raw/master/readme_resource/dev01.png)

4. 开始编译，选择Build->Rebuild Project，或者直接连上手机运行。
