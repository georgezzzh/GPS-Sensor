package com.sonydafa.phoneUsage;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileTransfer {
    private Context context;
    public FileTransfer(Context context) {
        this.context=context;
    }
    public void exportFile(String filename){
        String path= Environment.getExternalStorageDirectory().getPath()+"/sonydafa/"+filename;
        File file = new File(path);
        if(!file.exists())
            file.getParentFile().mkdirs();
        Log.i("info","导出的文件目录为:"+path);
        try(FileOutputStream fileOutputStream = new FileOutputStream(file);){
                String content=readFileData(filename);
                if(content!=null){
                    fileOutputStream.write(content.getBytes());
                    Toast.makeText(context,"文件导出成功,在根目录查看",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"文件为空",Toast.LENGTH_SHORT).show();
                }

            }catch (IOException e){
                e.printStackTrace();
            }
    }
    public void writeFileData(String filename,String message) {
        FileOutputStream fileOutputStream = null;
        Log.i("info","写入文件的路径为:"+filename);
        try {
            //清空文件
            fileOutputStream = context.openFileOutput(filename, context.MODE_APPEND);
            byte[] bytes = message.getBytes();
            fileOutputStream.write(bytes);
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String readFileData(String filename){
        String result="";
        try{
            FileInputStream fileInputStream = context.openFileInput(filename);
            int length = fileInputStream.available();
            byte[] bytes = new byte[length];
            fileInputStream.read(bytes);
            result=new String(bytes);
            fileInputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

}
