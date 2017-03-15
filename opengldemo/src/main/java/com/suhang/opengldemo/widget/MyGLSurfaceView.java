package com.suhang.opengldemo.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.View;

import com.suhang.opengldemo.R;
import com.suhang.opengldemo.function.Camera;
import com.suhang.opengldemo.interfaces.CanTranform;
import com.suhang.opengldemo.render.OpenGlRenderFive;

/**
 * Created by 苏杭 on 2017/3/6 14:18.
 */

public class MyGLSurfaceView extends GLSurfaceView implements ScaleGestureDetector.OnScaleGestureListener, ConstantButton.OnConstantClickListener{
    private CanTranform mTranform;
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
        if (renderer instanceof CanTranform) {
            mTranform = (CanTranform) renderer;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        super.surfaceChanged(holder, format, w, h);
    }

    float lastX;
    float lastY;
    private boolean isMorePoint;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTranform == null) {
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
                    mTranform.getCamera().touchMove(dx,dy);
                    lastX = event.getX();
                    lastY = event.getY();
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


    float initScale = 0.1f;
    int dir;
    int lastDir;

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (mTranform == null) {
            return false;
        }
        float factor = detector.getScaleFactor();
        if (factor > 1.0f) {
            dir = 1;
//            mTranform.scale(1);
            mTranform.getCamera().moveCamera(Camera.FORWARD, mTranform.getDeltaTime()*factor);

        } else {
            dir = 0;
            mTranform.getCamera().moveCamera(Camera.BACKWARD, mTranform.getDeltaTime()*(2-factor));
        }
        if (dir != lastDir) {
            initScale = 0.1f;
        }
        lastDir = dir;
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        initScale = 0.1f;
    }

    @Override
    public void onConstantClick(boolean isPress,View v) {
        if (mTranform == null) {
            return;
        }
        if (v.getId() == R.id.left) {
            mTranform.moveScreen(isPress, Camera.LEFT);
        } else if(v.getId()==R.id.right){
            mTranform.moveScreen(isPress,Camera.RIGHT);
        } else if (v.getId() == R.id.up) {
            mTranform.moveScreen(isPress,Camera.SCALE_BIG);
        } else {
            mTranform.moveScreen(isPress,Camera.SCALE_SMALL);
        }
    }
}
