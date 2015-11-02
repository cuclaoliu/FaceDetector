package edu.cuc.stephen.facedetector.util;

import android.hardware.Camera;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraParameterUtil {

    private CameraSizeComparator sizeComparator = new CameraSizeComparator();
    private static CameraParameterUtil cameraParameterUtil = null;
    private CameraParameterUtil(){}

    public static CameraParameterUtil getInstance(){
        if(cameraParameterUtil == null){
            cameraParameterUtil = new CameraParameterUtil();
            return cameraParameterUtil;
        }else
            return cameraParameterUtil;
    }

    public Camera.Size getProperSize(List<Camera.Size> list, float th, int minWidth){
        Collections.sort(list, sizeComparator);

        int i=0;
        for (Camera.Size size : list){
            if ((size.width >= minWidth) && equalRate(size, th)){
                Log.e("Camera", "Preview Size: w = " + size.width + "h = "+size.width);
                break;
            }
            i++;
        }
        if(i == list.size()){
            i = 0;      //如果没找到，就选用最小的size
        }
        return list.get(i);
    }

    public boolean equalRate(Camera.Size size, float rate){
        float r = (float) size.width / (float) size.height;
        if(Math.abs(r - rate) < 0.03){
            return true;
        }else
            return false;
    }

    public class CameraSizeComparator implements Comparator<Camera.Size>{
        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if(lhs.width == rhs.width){
                return 0;
            }else if(lhs.width > rhs.width) {
                return 1;
            }else{
                return -1;
            }
        }
    }

    //打印支持的previewSizes
    public void printSupportPreviewSize(Camera.Parameters parameters){
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        for(Camera.Size size : previewSizes){
            Log.e("Camera", "previewSizes: width="+size.width+",height="+size.height);
        }
    }
    //打印支持的 pictureSize
    public void printSupportPictureSize(Camera.Parameters parameters){
        List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
        for(Camera.Size size : pictureSizes){
            Log.e("Camera", "pictureSizes: width="+size.width+",height="+size.height);
        }
    }
    //打印支持的聚焦模式
    public void printSupportFocusMode(Camera.Parameters parameters){
        List<String> focusModes = parameters.getSupportedFocusModes();
        for(String mode : focusModes){
            Log.e("Camera", "focusMode--"+mode);
        }
    }
}
