package edu.cuc.stephen.facedetector.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static String storagePath = "";
    private static final String DST_FOLDER_NAME = "StephenCamera";

    //初始化保存路径
    private static String initPath(){
        if(storagePath.equals("")){
            storagePath = parentPath.getAbsolutePath()+"/"+DST_FOLDER_NAME;
            File file = new File(storagePath);
            if (!file.exists()){
                file.mkdir();
            }
        }
        return storagePath;
    }
    //保存bitmap到sdcard
    public static void saveBitmap(Bitmap bitmap){
        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake + ".jpg";
        Log.e("Camera", "saveBitmap: jpegName=" + jpegName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(jpegName);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            Log.e("Camera", "saveBitmap成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
