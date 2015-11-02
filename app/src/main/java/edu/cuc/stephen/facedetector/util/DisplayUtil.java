package edu.cuc.stephen.facedetector.util;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;

public class DisplayUtil {
    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue/scale + 0.5f);
    }

    //获取屏幕高度和宽度，单位px
    public static Point getScreenMetrics(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        Log.e("Camera", "Screen-- width="+screenWidth+",height="+screenHeight+", densityDpi="+displayMetrics.densityDpi);
        return new Point(screenWidth, screenHeight);
    }

    //获取屏幕长宽比
    public static float getScreenRate(Context context){
        Point point = getScreenMetrics(context);
        float height = point.y;
        float width = point.x;
        return height/width;
    }
}
