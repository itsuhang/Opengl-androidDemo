package com.suhang.opengldemo.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;

import com.suhang.opengldemo.R;
import com.suhang.opengldemo.utils.ShaderUtil;
import com.suhang.opengldemo.utils.TextureUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 苏杭 on 2017/2/5 13:54.
 */

public class OpenGlRenderThree implements GLSurfaceView.Renderer {
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
	private int mTexture1;

	public OpenGlRenderThree(Context context) {
		mContext = context;
	}


	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background color to black ( rgba ).
		GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1f);  // OpenGL docs.
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
				0.5f, 0.5f*factor, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,   // 右上
				0.5f, -0.5f*factor, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,   // 右下
				-0.5f, -0.5f*factor, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,   // 左下
				-0.5f, 0.5f*factor, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f    // 左上
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
		mTexture = TextureUtil.loadTexture(mContext, R.mipmap.ic_launcher_round);
		mTexture1 = TextureUtil.loadTexture(mContext, R.mipmap.huanpeng);
	}

	private void init() {
		mProgram = ShaderUtil.createProgram(ShaderUtil.createShader(mContext, "vertex_shader_three", GLES31.GL_VERTEX_SHADER), ShaderUtil.createShader(mContext, "fragment_shader_three", GLES31.GL_FRAGMENT_SHADER));
		GLES31.glUseProgram(mProgram);
	}

	private void bindData() {
		mVertexData.position(0);
		GLES31.glVertexAttribPointer(0, VERTEX_COUNT, GLES31.GL_FLOAT, false, STRIDE, mVertexData);
		GLES31.glEnableVertexAttribArray(0);
		mVertexData.position(3);
		GLES31.glVertexAttribPointer(1, VERTEX_COLOR_COUNT, GLES31.GL_FLOAT, false, STRIDE, mVertexData);
		GLES31.glEnableVertexAttribArray(1);
		mVertexData.position(6);
		GLES31.glVertexAttribPointer(2, VERTEX_TEXTURE_COUNT, GLES31.GL_FLOAT, false, STRIDE, mVertexData);
		GLES31.glEnableVertexAttribArray(2);


		GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
		GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, mTexture);
		GLES31.glUniform1i(GLES31.glGetUniformLocation(mProgram,"outTexture"),0);
		GLES31.glActiveTexture(GLES31.GL_TEXTURE1);
		GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, mTexture1);
		GLES31.glUniform1i(GLES31.glGetUniformLocation(mProgram,"outTexture1"),1);

	}

	private float factor;

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Sets the current view port to the new size.
		GLES31.glViewport(0, 0, width, height);// OpenGL docs.
		factor = 1.0f * width / height;
		init();
		createData();
		getLocations();
		bindData();
	}


	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);  // OpenGL docs.
//		gl.glLoadIdentity();
//		gl.glTranslatef(0, 0, -4);
		GLES31.glDrawElements(GLES31.GL_TRIANGLES,6,GLES31.GL_UNSIGNED_INT,mIndicesData);
//		GLES31.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
	}
}
