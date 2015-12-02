package edu.cuc.stephen.facedetector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.FaceDetectionListener, CameraInterface.CameraOpenOverCallback{

    private static final int FIND_FACES = 0x150;
    private static final int CAMERA_HAD_STARTED_PREVIEW = 0x151;
    private CameraInterface cameraInterface;
    protected Context context;
    protected Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CAMERA_HAD_STARTED_PREVIEW:
                    cameraInterface = CameraInterface.getInstance();
                    cameraInterface.setFaceDetectionListener(CameraSurfaceView.this);
                    Camera.Parameters parameters = cameraInterface.getParameters();
                    Camera.Size size = parameters.getPreviewSize();
                    int width = Math.min(getWidth(), getHeight());
                    int height = width * size.width / size.height;
                    CameraSurfaceView.this.setLayoutParams(new FrameLayout.LayoutParams(width, height));
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private Camera.Face[] facesDetected;
    private FaceView faceView;

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    private SurfaceHolder surfaceHolder;

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);//translucent半透明，transparent透明
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("Camera", "surfaceCreated...");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("Camera", "surfaceChanged...");
        Log.e("Camera", "surface    width=" + width + ", height=" + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("Camera", "surfaceDestroyed...");
        CameraInterface.getInstance().doStopCamera();
    }

    //人脸探测侦听！
    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        Log.e("Camera", "onFaceDetection()");
        if (faces != null && faces.length >= 1){
            Log.e("FACE", "找到" + faces.length + "张脸！");
            faceView.setFaces(faces);
            faceView.setVisibility(VISIBLE);
        }else {
            faceView.clearFaces();
            faceView.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void cameraHasOpened() {
        Log.e("Camera", "摄像头以打开......");
        CameraInterface.getInstance().doStartPreview(surfaceHolder, -1f);
        handler.sendEmptyMessageDelayed(CAMERA_HAD_STARTED_PREVIEW, 1500);
    }

    public void setFaceView(FaceView faceView) {
        this.faceView = faceView;
    }
}
