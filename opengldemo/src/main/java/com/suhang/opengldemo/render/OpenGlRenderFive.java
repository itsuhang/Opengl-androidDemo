package com.suhang.opengldemo.render;

import android.content.Context;
import android.graphics.Camera;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.suhang.opengldemo.R;
import com.suhang.opengldemo.utils.LogUtil;
import com.suhang.opengldemo.utils.ShaderUtil;
import com.suhang.opengldemo.utils.TextureUtil;
import com.suhang.opengldemo.utils.VectorUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.GL_DEPTH_BUFFER_BIT;

/**
 * Created by 苏杭 on 2017/2/5 13:54.
 */

public class OpenGlRenderFive implements GLSurfaceView.Renderer {
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
    float deltaTime = 0.0f;
    float lastTime = 0.0f;

    public OpenGlRenderFive(Context context) {
        mContext = context;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background color to black ( rgba ).
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1f);  // OpenGL docs.
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
    }

    public void move(float[] a) {
//			mSpeed = 5 * deltaTime;
//		switch (direction) {
//			case LEFT:
//				mSee = VectorUtil.add(mSee, VectorUtil.normalize(VectorUtil.multiply(VectorUtil.multiply(mPosition, mUp), mSpeed), mSpeed));
//				break;
//			case RIGHT:
//				mSee = VectorUtil.reduce(mSee, VectorUtil.normalize(VectorUtil.multiply(VectorUtil.multiply(mPosition, mUp), mSpeed), mSpeed));
//				break;
//			case UP:
//				mSee =VectorUtil.add(mSee,VectorUtil.multiply(mPosition,mSpeed));
//				break;
//			case DOWN:
//				mSee =VectorUtil.reduce(mSee,VectorUtil.multiply(mPosition,mSpeed));
//				break;
//		}
        mPosition = a;
//        LogUtil.i("啊啊啊see" + mSee[0] + "  " + mSee[1] + "   " + mSee[2]);
//        LogUtil.i("啊啊啊po" + mPosition[0] + "  " + mPosition[1] + "   " + mPosition[2]);
//        LogUtil.i("啊啊啊up" + mUp[0] + "  " + mUp[1] + "   " + mUp[2]);
        createViewMatrix();
        bindMatrix();
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
        mTexture = TextureUtil.loadTexture(mContext, R.mipmap.box0);
    }


    private float[] mSee = {0f, 0f,  5f};
    private float[] mPosition = {0f, 0f, -1f};
    private float[] mUp = {0f, 1f, 0f};


    private void init() {
        mProgram = ShaderUtil.createProgram(ShaderUtil.createShader(mContext, R.raw.vertex_shader_five, GLES30.GL_VERTEX_SHADER), ShaderUtil.createShader(mContext, R.raw.fragment_shader_five, GLES30.GL_FRAGMENT_SHADER));
        GLES30.glUseProgram(mProgram);
    }


    private void createProjectionMatrix(int width, int height) {
        Matrix.setIdentityM(mProjectionMatrix, 0);
        Matrix.perspectiveM(mProjectionMatrix, 0, 45.0f, 1.0f * width / height, 0.1f, 100.0f);
    }

    private void createViewMatrix() {
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setLookAtM(mViewMatrix, 0, mSee[0], mSee[1], mSee[2], mSee[0]+mPosition[0],mSee[1]+ mPosition[1], mSee[2]+mPosition[2], mUp[0], mUp[1], mUp[2]);
    }

    private void createModelMatrix() {
        Matrix.setIdentityM(mModelMatrix, 0);
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
        GLES30.glViewport(0, 0, width, height);// OpenGL docs.
        init();
        createData();
        getLocations();
        bindData();
        createViewMatrix();
        createModelMatrix();
        createProjectionMatrix(width, height);
        bindMatrix();
    }


    private void bindMatrix() {
        GLES30.glUniformMatrix4fv(mModel, 1, false, mModelMatrix, 0);
        GLES30.glUniformMatrix4fv(mView, 1, false, mViewMatrix, 0);
        GLES30.glUniformMatrix4fv(mProjection, 1, false, mProjectionMatrix, 0);
    }

    private float getCurrentTime() {
        return 1.0f * SystemClock.uptimeMillis() / 1000;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float currentTime = getCurrentTime();
        deltaTime = currentTime - lastTime;
        lastTime = currentTime;
        GLES30.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);  // OpenGL docs.0
        Matrix.setIdentityM(mModelMatrix, 0);
        createViewMatrix();
        bindMatrix();
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, mIndices.length, GLES30.GL_UNSIGNED_INT, mIndicesData);
    }
}
