package com.suhang.opengldemo.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.suhang.opengldemo.render.OpenGlRenderFive;

/**
 * Created by 苏杭 on 2017/3/6 14:18.
 */

public class MyGLSurfaceView extends GLSurfaceView {
    private OpenGlRenderFive mRenderFive;

    public MyGLSurfaceView(Context context) {
        this(context, null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(3);
    }

    @Override
    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
        if (renderer instanceof OpenGlRenderFive) {
            mRenderFive = (OpenGlRenderFive) renderer;
        }
    }

    int y = 0;
    int x = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mRenderFive == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = (int) event.getY();
                x = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) (event.getY() - y);
                int dx = (int) (event.getX() - x);
                if (dy > 50) {
                    mRenderFive.move(OpenGlRenderFive.UP);
                } else if (dy <= -50) {
                    mRenderFive.move(OpenGlRenderFive.DOWN);
                }
                if (dx > 50) {
                    mRenderFive.move(OpenGlRenderFive.RIGHT);
                } else if (dx <= -50) {
                    mRenderFive.move(OpenGlRenderFive.LEFT);
                }
                break;
            case MotionEvent.ACTION_UP:
                y = 0;
                break;
        }
        return true;
    }
}
