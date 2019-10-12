package com.sonydafa.phoneUsage;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileTransfer {
    private Context context;
    public FileTransfer(Context context) {
        this.context=context;
    }
    public void exportFile(String filename,String str){
        File files = context.getExternalFilesDir("");
        if(files==null){
            Log.e("error","Android/data/sonydafa/files获取失败");
            Toast.makeText(context,"Android/data/sonydafa/files获取失败",Toast.LENGTH_SHORT).show();
            return;
        }
        String path=files.getAbsolutePath()+"/"+filename;
        File file = new File(path);
        Log.i("info","导出的文件目录为:"+path);
        try(FileWriter fileWriter = new FileWriter(file, true)){
            fileWriter.append(str);
            }catch (IOException e){
            Toast.makeText(context,"Android/data/sonydafa/file导出文件失败",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
