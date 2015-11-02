package edu.cuc.stephen.facedetector;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    private CameraInterface cameraInterface;
    private Context context;

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
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("Camera", "surfaceDestroyed...");
        CameraInterface.getInstance().doStopCamera();
    }
}
