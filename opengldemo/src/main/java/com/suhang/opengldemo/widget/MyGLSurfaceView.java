package com.suhang.opengldemo.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.suhang.opengldemo.render.OpenGlRenderFive;
import com.suhang.opengldemo.utils.LogUtil;
import com.suhang.opengldemo.utils.VectorUtil;

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

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		super.surfaceChanged(holder, format, w, h);
		lastX = w / 2.0f;
		lastY = h / 2.0f;

	}

	float lastX;
	float lastY;
	float sensitivity = 0.05f;
	float yaw = -90;
	float pitch;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mRenderFive == null) {
			return false;
		}
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				lastX = event.getX();
				lastY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				float dx = event.getX() - lastX;
				float dy = lastY - event.getY();
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
				LogUtil.i("啊啊啊"+yaw +"  "+ pitch);
				LogUtil.i("啊啊啊"+VectorUtil.angleTransform(yaw)+"    "+VectorUtil.angleTransform(pitch));
				float[] front = {(float) (Math.cos(VectorUtil.angleTransform(yaw)) * Math.cos(VectorUtil.angleTransform(pitch))), (float) Math.cos(VectorUtil.angleTransform(pitch)), (float) (Math.cos(VectorUtil.angleTransform(yaw)) * Math.sin(VectorUtil.angleTransform(pitch)))};
				mRenderFive.move(VectorUtil.normalize(front, 1));
				break;
			case MotionEvent.ACTION_UP:
				break;
		}
		return true;
	}
}
