package edu.cuc.stephen.facedetector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by stephen on 15-11-4.
 */
public class FaceView extends View{
    private Context context;
    private Paint paint;

    public void clearFaces() {
        faces = null;
        invalidate();
    }

    public void setFaces(Camera.Face[] faces) {
        this.faces = faces;
        invalidate();
    }

    private Camera.Face[] faces;
    private RectF rect = new RectF();

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(faces == null || faces.length < 1) {
            return;
        }
        Log.e("Camera", "faceView.onDraw()....");
        boolean isMirror = false;
        int id = CameraInterface.getInstance().getCameraId();
        //float fontHeight = fontMetrics.bottom - fontMetrics.descent;
        if( id == Camera.CameraInfo.CAMERA_FACING_FRONT)
            isMirror = true;            //前置Camera需要mirror
        Matrix matrix = new Matrix();
        matrix.setScale(isMirror?-1:1, 1);
        matrix.postRotate(90);
        matrix.postScale(getWidth()/2000f, getHeight()/2000f);
        matrix.postTranslate(getWidth()/2f, getHeight()/2f);
        matrix.postRotate(0);
        canvas.save();
        for(Camera.Face face : faces){
            Log.e("Camera", "画出人脸！！！！！！");
            rect.set(face.rect);
            matrix.mapRect(rect);
            canvas.drawRect(rect, paint);
            //paint.setTextAlign(Paint.Align.LEFT);
            String text = String.format("0.%d", face.score);
            canvas.drawText(text, rect.left,
                    rect.top - 6, paint);
        }
        canvas.restore();
        super.onDraw(canvas);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setTextSize(32);
        paint.setStrokeWidth(3);
    }
}
