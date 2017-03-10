package com.suhang.opengldemo.render;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.suhang.opengldemo.R;
import com.suhang.opengldemo.utils.ShaderUtil;
import com.suhang.opengldemo.utils.TextureUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;

/**
 * Created by 苏杭 on 2017/2/5 13:54.
 */

public class OpenGlRenderFour implements GLSurfaceView.Renderer {
	public static final int VERTEX_COUNT = 3;
	public static final int VERTEX_COLOR_COUNT = 3;
	public static final int VERTEX_TEXTURE_COUNT = 2;
	public static final int FLOATBYTE = 4;
	public static final int STRIDE = (VERTEX_COUNT + VERTEX_COLOR_COUNT + VERTEX_TEXTURE_COUNT) * FLOATBYTE;

	Context mContext;
	private float[] mVertex;
	private int[] mIndices;
	private FloatBuffer mVertexData;
	private IntBuffer mIndicesData;
	private int mProgram;
	private int mTexture;
	private float[] mProjectionMatrix = new float[16];
	private float[] mViewMatrix = new float[16];
	private float[] mModelMatrix = new float[16];
	private int mModel;
	private int mView;
	private int mProjection;
	private int mOutTexture;

	public OpenGlRenderFour(Context context) {
		mContext = context;
	}


	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background color to black ( rgba ).
		GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1f);  // OpenGL docs.
//        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
	}

	private void getLocations() {
		mOutTexture = GLES30.glGetUniformLocation(mProgram, "outTexture");
		mModel = GLES30.glGetUniformLocation(mProgram, "model");
		mView = GLES30.glGetUniformLocation(mProgram, "view");
		mProjection = GLES30.glGetUniformLocation(mProgram, "projection");
	}

	private void createData() {
		mVertex = new float[]{
				0.5f, 0.5f , 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,   // 右上
				0.5f, -0.5f , 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,   // 右下
				-0.5f, -0.5f , 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,   // 左下
				-0.5f, 0.5f , 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f    // 左上
		};

		mIndices = new int[]{
				0, 1, 3, // 第一个三角形
				1, 2, 3  // 第二个三角形
		};
		mVertexData = ByteBuffer.allocateDirect(mVertex.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mVertexData.put(mVertex);
		mIndicesData = ByteBuffer.allocateDirect(mIndices.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
		mIndicesData.put(mIndices);
		mIndicesData.position(0);
		mTexture = TextureUtil.loadTexture(mContext, R.mipmap.huanpeng);
	}

	private void init() {
		mProgram = ShaderUtil.createProgram(ShaderUtil.createShader(mContext, R.raw.vertex_shader_four, GLES30.GL_VERTEX_SHADER), ShaderUtil.createShader(mContext, R.raw.fragment_shader_four, GLES30.GL_FRAGMENT_SHADER));
		GLES30.glUseProgram(mProgram);
	}


	private void createProjectionMatrix(int width, int height) {
//		Matrix.setIdentityM(mProjectionMatrix,0);
		Matrix.perspectiveM(mProjectionMatrix, 0, 45.0f, 1.0f * width / height, 0.1f, 100.0f);
	}

	private void createViewMatrix() {
		Matrix.setIdentityM(mViewMatrix,0);
		Matrix.translateM(mViewMatrix, 0, 0.0f, 0.0f, -3.0f);
//		Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 0f, 0, 0, 0, 0, 1, 1);
	}

	private void createModelMatrix() {
		Matrix.setIdentityM(mModelMatrix,0);
		Matrix.rotateM(mModelMatrix,0,-80.0f,1f,0.0f,0f);
	}

	private void bindData() {
		mVertexData.position(0);
		GLES30.glVertexAttribPointer(0, VERTEX_COUNT, GLES30.GL_FLOAT, false, STRIDE, mVertexData);
		GLES30.glEnableVertexAttribArray(0);
		mVertexData.position(3);
		GLES30.glVertexAttribPointer(1, VERTEX_COLOR_COUNT, GLES30.GL_FLOAT, false, STRIDE, mVertexData);
		GLES30.glEnableVertexAttribArray(1);
		mVertexData.position(6);
		GLES30.glVertexAttribPointer(2, VERTEX_TEXTURE_COUNT, GLES30.GL_FLOAT, false, STRIDE, mVertexData);
		GLES30.glEnableVertexAttribArray(2);

		GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTexture);
		GLES30.glUniform1i(mOutTexture, 0);
	}

//	private float factor;

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Sets the current view port to the new size.
		GLES30.glViewport(0, 0, width, height);// OpenGL docs.
//		factor = 1.0f * width / height;
		init();
		createData();
		getLocations();
		bindData();
		createViewMatrix();
		createModelMatrix();
		createProjectionMatrix(width, height);
//		Matrix.setIdentityM(mModelMatrix, 0);
		bindMatrix();
	}

	long TIME = 10000L;

	private void bindMatrix() {
        float angle = (float)(SystemClock.uptimeMillis() % TIME) / TIME * 360;
        Matrix.rotateM(mModelMatrix, 0,angle, 0.0f, 1.0f,0.5f);
		GLES30.glUniformMatrix4fv(mModel, 1, false, mModelMatrix, 0);
		GLES30.glUniformMatrix4fv(mView, 1, false, mViewMatrix, 0);
		GLES30.glUniformMatrix4fv(mProjection, 1, false, mProjectionMatrix, 0);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL_COLOR_BUFFER_BIT);  // OpenGL docs.0
		Matrix.setIdentityM(mModelMatrix, 0);
		bindMatrix();
		GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, mIndicesData);
	}
}
