package edu.cuc.stephen.facedetector.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtil {
    //旋转Bitmap
    public static Bitmap getRotateBitmap(Bitmap bitmap, float rotateDegree){
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return rotateBitmap;
    }
}
