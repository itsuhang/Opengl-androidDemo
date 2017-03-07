package com.suhang.opengldemo.render;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;

import com.suhang.opengldemo.utils.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 苏杭 on 2017/2/5 13:54.
 */

public class OpenGlRenderTwo implements GLSurfaceView.Renderer {
	public static final int VERTEX_COUNT = 3;
	public static final int VERTEX_COLOR_COUNT = 3;
	Context mContext;
	private float[] mVertex;
	//	private short[] mIndices;
	private FloatBuffer mVertexData;
	//	private ShortBuffer mIndicesData;
	private int mProgram;

	public OpenGlRenderTwo(Context context) {
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
//		float time = 1.0f*SystemClock.uptimeMillis()/1000;
//		float v = (float) (Math.sin(time) / 2 + 0.5);
//		LogUtil.i("啊啊啊"+Math.sin(time));
//		int fragcolor = GLES31.glGetUniformLocation(mProgram, "fragcolor");
//		GLES31.glUniform4f(fragcolor, 0.0f, v, 0.0f, 1.0f);
	}

	private void createData() {
		mVertex = new float[]{
				0.5f, 0.5f, 0.0f,  1.0f, 0.0f, 0.0f,   // 右下
				-0.5f, 0.5f, 0.0f,  0.0f, 1.0f, 0.0f,   // 左下
				0.0f,  -0.5f, 0.0f,  0.0f, 0.0f, 1.0f    // 顶部
		};

//		mIndices = new short[]{
//				0, 1, 3, // 第一个三角形
//				1, 2, 3  // 第二个三角形
//		};
		mVertexData = ByteBuffer.allocateDirect(mVertex.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mVertexData.put(mVertex);
//		mIndicesData = ByteBuffer.allocateDirect(mIndices.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
//		mIndicesData.put(mIndices);
//		mIndicesData.position(0);
	}

	private void init() {
		mProgram = ShaderUtil.createProgram(ShaderUtil.createShader(mContext, "vertex_shader_two", GLES31.GL_VERTEX_SHADER), ShaderUtil.createShader(mContext, "fragment_shader_two", GLES31.GL_FRAGMENT_SHADER));
		GLES31.glUseProgram(mProgram);
	}

	private void bindData() {
		mVertexData.position(0);
		GLES31.glVertexAttribPointer(0, VERTEX_COUNT, GLES31.GL_FLOAT, false, 6*4, mVertexData);
		GLES31.glEnableVertexAttribArray(0);
		mVertexData.position(3);
		GLES31.glVertexAttribPointer(1, VERTEX_COLOR_COUNT, GLES31.GL_FLOAT, false, 6*4, mVertexData);
		GLES31.glEnableVertexAttribArray(1);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Sets the current view port to the new size.
		GLES31.glViewport(0, 0, width, height);// OpenGL docs.
	}

	int i = 0;

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);  // OpenGL docs.
//		gl.glLoadIdentity();
//		gl.glTranslatef(0, 0, -4);
		GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, 3);
//		GLES31.glDrawElements(GLES31.GL_TRIANGLES, 6, GLES31.GL_UNSIGNED_SHORT, mIndicesData);
	}
}
