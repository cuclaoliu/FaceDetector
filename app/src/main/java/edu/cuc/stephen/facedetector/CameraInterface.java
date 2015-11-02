package edu.cuc.stephen.facedetector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import edu.cuc.stephen.facedetector.util.CameraParameterUtil;
import edu.cuc.stephen.facedetector.util.FileUtil;
import edu.cuc.stephen.facedetector.util.ImageUtil;

public class CameraInterface {
    private Camera camera;
    private int defaultCameraId = 0;
    private Camera.Parameters parameters;
    private boolean isPreviewing = false;
    private float previewRate = -1f;
    private static CameraInterface cameraInterface;

    public interface CameraOpenOverCallback{
        void cameraHasOpened();
    }

    private CameraInterface(){}

    public static synchronized CameraInterface getInstance(){
        if(cameraInterface == null){
            cameraInterface = new CameraInterface();
        }
        return cameraInterface;
    }

    //打开Camera
    public void doOpenCamera(CameraOpenOverCallback callback, Camera.PreviewCallback previewCallback){
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for(int i=0; i < numberOfCameras; i++){
            Camera.getCameraInfo(i, cameraInfo);
            if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                defaultCameraId = i;
                //Toast.makeText(getApplicationContext(), "找到前置摄像头", Toast.LENGTH_LONG).show();
            }
        }
        if(numberOfCameras <= 0){
            //Toast.makeText(getApplicationContext(), getString(R.string.no_camera), Toast.LENGTH_LONG).show();
        }
        camera = Camera.open(defaultCameraId);
        callback.cameraHasOpened();
        camera.setPreviewCallback(previewCallback);
    }

    //开启预览
    public void doStartPreview(SurfaceHolder holder, float previewRate){
        if(isPreviewing){
            camera.stopPreview();
            return;
        }
        if(camera!=null){
            parameters = camera.getParameters();
            parameters.setPictureFormat(PixelFormat.JPEG);//存储格式
            CameraParameterUtil.getInstance().printSupportPictureSize(parameters);
            CameraParameterUtil.getInstance().printSupportPreviewSize(parameters);
            //设置PreviewSize和PictureSize
            Camera.Size pictureSize = CameraParameterUtil.getInstance().getProperSize(
                    parameters.getSupportedPictureSizes(), previewRate, 800);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);
            Camera.Size previewSize = CameraParameterUtil.getInstance().getProperSize(
                    parameters.getSupportedPreviewSizes(), previewRate, 800);
            parameters.setPreviewSize(previewSize.width, previewSize.height);

            camera.setDisplayOrientation(90);

            CameraParameterUtil.getInstance().printSupportFocusMode(parameters);
            List<String> focusModes = parameters.getSupportedFocusModes();
            if(focusModes.contains("continuous-video")){
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            camera.setParameters(parameters);

            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();      //开启预览
            } catch (IOException e) {
                e.printStackTrace();
            }

            isPreviewing = true;
            this.previewRate = previewRate;
            parameters = camera.getParameters();        //重新get一次
            Log.e("Camera", "最终设置：previewSize-- width="+parameters.getPreviewSize().width
                +", height="+parameters.getPreviewSize().height);
            Log.e("Camera", "最终设置：pictureSize-- width="+parameters.getPictureSize().width
                +", height="+parameters.getPreviewSize().height);
        }
    }

    //停止预览，释放Camera
    public void doStopCamera(){
        if(null != camera){
            camera.setPreviewCallback(null);
            camera.stopPreview();
            isPreviewing = false;
            previewRate = -1f;
            camera.release();
            camera = null;
        }
    }

    //拍照
    public void doTakePicture(){
        if(isPreviewing && (camera!=null)){
            //camera.takePicture(shutterCallback, null, jpegPictureCallback);
            camera.takePicture(shutterCallback, rawCallback, jpegPictureCallback);
        }
    }

    //为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量
    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            Log.e("Camera", "shutterCallback.onShutter()...");
        }
    };
    //拍摄的未压缩原数据的回调，可以为null
    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.e("Camera", "rawCallback.onPictureTaken()...");
        }
    };
    //对JPEG图像数据的回调，最重要的一个回调
    Camera.PictureCallback jpegPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.e("Camera", "jpegPictureCallback.onPictureTaken()...");
            Bitmap bitmap = null;
            if(null != data){
                //data是字节数据，将其解析成位图
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                camera.stopPreview();
                isPreviewing = false;
            }
            //保存图片到sdcard
            if(null != bitmap){
                //设置FOCUS_MODE_CONTINUOUS_VIDEO后，parameters.set("rotation", 90)失效
                //图片不能旋转了，故这里要旋转一下
                Bitmap rotateBitmap = ImageUtil.getRotateBitmap(bitmap, 90f);
                FileUtil.saveBitmap(rotateBitmap);
            }
            //再次进入预览
            camera.startPreview();
            isPreviewing = true;
        }
    };
}
