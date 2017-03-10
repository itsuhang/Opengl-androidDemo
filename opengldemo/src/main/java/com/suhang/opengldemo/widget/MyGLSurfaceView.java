package com.suhang.opengldemo.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.View;

import com.suhang.opengldemo.R;
import com.suhang.opengldemo.render.OpenGlRenderFive;
import com.suhang.opengldemo.utils.LogUtil;
import com.suhang.opengldemo.utils.VectorUtil;

/**
 * Created by 苏杭 on 2017/3/6 14:18.
 */

public class MyGLSurfaceView extends GLSurfaceView implements ScaleGestureDetector.OnScaleGestureListener, ConstantButton.OnConstantClickListener{
    private OpenGlRenderFive mRenderFive;
    private Context mContext;
    private ScaleGestureDetector mScaleGestureDetector;

    public MyGLSurfaceView(Context context) {
        this(context, null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        setEGLContextClientVersion(3);
        mScaleGestureDetector = new ScaleGestureDetector(mContext, this);
    }

    @Override
    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
        if (renderer instanceof OpenGlRenderFive) {
            mRenderFive = (OpenGlRenderFive) renderer;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        super.surfaceChanged(holder, format, w, h);
        lastX = w / 2.0f;
        lastY = h / 2.0f;
        yaw = -90;
        pitch = 0;
    }

    float lastX;
    float lastY;
    float sensitivity = 0.05f;
    float yaw = -90;
    float pitch;
    private boolean isMorePoint;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mRenderFive == null) {
            return false;
        }
        mScaleGestureDetector.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                isMorePoint = true;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!isMorePoint) {
                    float dx = lastX - event.getX();
                    float dy = event.getY() - lastY;
                    lastX = event.getX();
                    lastY = event.getY();
                    dx *= sensitivity;
                    dy *= sensitivity;
                    yaw += dx;
                    pitch += dy;
                    if (pitch >= 89.0f) {
                        pitch = 89.0f;
                    }
                    if (pitch <= -89.0f) {
                        pitch = -89.0f;
                    }
                    float[] front = {(float) (Math.cos(VectorUtil.angleTransform(yaw)) * Math.cos(VectorUtil.angleTransform(pitch))), (float) Math.sin(VectorUtil.angleTransform(pitch)), (float) (Math.cos(VectorUtil.angleTransform(pitch)) * Math.sin(VectorUtil.angleTransform(yaw)))};
                    mRenderFive.move(VectorUtil.normalize(front, 1));
                    return true;
                } else {
                    return false;
                }
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_UP:
                isMorePoint = false;
                break;
        }
        return true;
    }



    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (mRenderFive == null) {
            return false;
        }
        float factor = detector.getScaleFactor();
        if (factor > 1.0f) {
            mRenderFive.scale(1);
        } else {
            mRenderFive.scale(0);
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        LogUtil.i("啊啊啊begin");
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        LogUtil.i("啊啊啊end");
    }

    @Override
    public void onConstantClick(boolean isPress,View v) {
        if (mRenderFive == null) {
            return;
        }
        if (v.getId() == R.id.left) {
            mRenderFive.moveScreen(isPress,true);
        } else {
            mRenderFive.moveScreen(isPress,false);
        }
    }
}
