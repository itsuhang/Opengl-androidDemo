package com.suhang.opengldemo.render;

import android.content.Context;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import com.suhang.opengldemo.utils.LogUtil;
import com.suhang.opengldemo.utils.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 苏杭 on 2017/2/5 13:54.
 */

public class OpenGlRenderOne implements GLSurfaceView.Renderer {
	public static final int VERTEX_COUNT = 3;
	Context mContext;
	private float[] mVertex;
	private short[] mIndices;
	private FloatBuffer mVertexData;
	private ShortBuffer mIndicesData;
	private int mProgram;

	public OpenGlRenderOne(Context context) {
		mContext = context;
	}


	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background color to black ( rgba ).
		GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1f);  // OpenGL docs.
		init();
		createData();
		getLocations();
		bindData();

	}

	private void getLocations() {
		float time = 1.0f*SystemClock.uptimeMillis()/1000;
		float v = (float) (Math.sin(time) / 2 + 0.5);
		int fragcolor = GLES31.glGetUniformLocation(mProgram, "fragcolor");
		GLES31.glUniform4f(fragcolor, 0.0f, v, 0.0f, 1.0f);
	}

	private void createData() {
		mVertex = new float[]{
				0.5f, 0.5f, 0.0f,   // 右上角
				0.5f, -0.5f, 0.0f,  // 右下角
				-0.5f, -0.5f, 0.0f, // 左下角
				-0.5f, 0.5f, 0.0f   // 左上角
		};

		mIndices = new short[]{
				0, 1, 3, // 第一个三角形
				1, 2, 3  // 第二个三角形
		};
		mVertexData = ByteBuffer.allocateDirect(mVertex.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mVertexData.put(mVertex);
		mIndicesData = ByteBuffer.allocateDirect(mIndices.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
		mIndicesData.put(mIndices);
		mIndicesData.position(0);
	}

	private void init() {
		mProgram = ShaderUtil.createProgram(ShaderUtil.createShader(mContext, "vertex_shader", GLES31.GL_VERTEX_SHADER), ShaderUtil.createShader(mContext, "fragment_shader", GLES31.GL_FRAGMENT_SHADER));
		GLES31.glUseProgram(mProgram);
	}

	private void bindData() {
		mVertexData.position(0);
		GLES31.glVertexAttribPointer(0, VERTEX_COUNT, GLES31.GL_FLOAT, false, mVertex.length, mVertexData);
		GLES31.glEnableVertexAttribArray(0);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Sets the current view port to the new size.
		GLES31.glViewport(0, 0, width, height);// OpenGL docs.
	}

	int i = 0;

	@Override
	public void onDrawFrame(GL10 gl) {
//		if (i > 10) {
			getLocations();
//			i = 0;
//		}
//		i++;
		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);  // OpenGL docs.
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -4);
		GLES31.glLineWidth(10);
		GLES31.glDrawElements(GLES31.GL_TRIANGLES, 6, GLES31.GL_UNSIGNED_SHORT, mIndicesData);
	}
}
