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
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.FaceDetectionListener, Camera.PreviewCallback, CameraInterface.CameraOpenOverCallback{

    private static final int FIND_FACES = 0x115;
    private CameraInterface cameraInterface;
    private Context context;
    private Handler handler;
    private Camera.Face[] facesDetected;

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    private SurfaceHolder surfaceHolder;

    @Override
    public void draw(Canvas canvas) {
        Log.e("Camera", "surfaceView.draw()....");
        boolean isMirror = false;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setTextSize(32);
        //Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        //float fontHeight = fontMetrics.bottom - fontMetrics.descent;
        canvas.save();
        for(Camera.Face face : facesDetected){
            paint.setStrokeWidth(3);
            Log.e("Camera", "画出人脸！！！！！！");
            canvas.drawRect(face.rect, paint);
            //paint.setTextAlign(Paint.Align.LEFT);
            String text = String.format("%.2f", face.score);
            canvas.drawText(text, face.rect.left,
                    face.rect.top - 6, paint);
        }
        canvas.restore();
        super.draw(canvas);
    }

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

    //人脸探测侦听！
    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        if (faces != null && faces.length >= 1){
            Log.e("FACE", "找到" + faces.length + "张脸！");
            facesDetected = faces;
            Canvas canvas = surfaceHolder.lockCanvas();
            draw(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        cameraInterface.setFaceDetectionListener(this);
    }

    @Override
    public void cameraHasOpened() {
        CameraInterface.getInstance().doStartPreview(surfaceHolder, -1f);
    }
}
