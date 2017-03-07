package com.suhang.opengldemo.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by 苏杭 on 2017/3/6 14:18.
 */

public class MyGLSurfaceView  extends GLSurfaceView{
	public MyGLSurfaceView(Context context) {
		this(context,null);
	}

	public MyGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setEGLContextClientVersion(3);
	}
}
