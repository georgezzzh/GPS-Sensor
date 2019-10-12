package com.sonydafa.phoneUsage;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.fragment.app.Fragment;

import com.sonydafa.phoneUsage.R;

import java.util.ArrayList;
import java.util.HashMap;

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
        Log.d("homePage","第一个view");
        super.onStart();
    }
    Page1Fragment(Context context){
        this.context=context;
    }
    private  void refreshPage(){
        ArrayList<HashMap<String, Object>> dataSet= new ArrayList<>();
        int defaultIcon=R.drawable.android;
        Context context = getContext();
        int totalTimeUsage=0;
        PackageManager pm = context.getPackageManager();
        for (AppUsage appUsage:Tools.getAllAppUsage(context)){

            HashMap<String,Object>pairs=new HashMap<>();
            if(appUsage.getFrontTime()==0) continue;
            totalTimeUsage+=appUsage.getFrontTime();
            if(appUsage.getRealName()!=null && !appUsage.getRealName().equals("")){
                pairs.put("title",appUsage.getRealName());
                pairs.put("spanTime",Tools.sec2hourWithMin(appUsage.getFrontTime()));
                try {
                    ApplicationInfo appInfo = pm.getApplicationInfo(appUsage.getPackageName(), PackageManager.GET_META_DATA);
                    Drawable drawable = appInfo.loadIcon(pm);
                    pairs.put("picture",drawable);
                }catch(PackageManager.NameNotFoundException e){
                    e.printStackTrace();
                }
            }
            else{
                pairs.put("title", "已卸载的应用");
                pairs.put("spanTime",Tools.sec2hourWithMin(appUsage.getFrontTime()));
                //默认已经卸载过的icon
                pairs.put("picture",defaultIcon);
            }
            //Log.d("homePage",pairs.toString());
            dataSet.add(pairs);
        }
        HashMap<String,Object>pairs=new HashMap<>();
        pairs.put("title","Screen");
        pairs.put("picture",R.drawable.screen);
        pairs.put("spanTime",Tools.sec2hourWithMin(totalTimeUsage));
        dataSet.add(0,pairs);
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), dataSet, R.layout.photo_item, new String[]{"picture", "title","spanTime"}, new int[]{R.id.image, R.id.title, R.id.timeSpan});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String s) {
                if (view instanceof ImageView && data instanceof Drawable) {
                    ImageView iv = (ImageView) view;
                    iv.setImageDrawable((Drawable) data);
                    return true;
                } else
                    return false;
            }
        });
        ListView listView=getView().findViewById(R.id.list_view);
        listView.setAdapter(simpleAdapter);;
        //super.onStart();
    }
}
