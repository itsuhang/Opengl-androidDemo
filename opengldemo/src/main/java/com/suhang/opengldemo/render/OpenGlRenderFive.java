package com.suhang.opengldemo.render;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.suhang.opengldemo.R;
import com.suhang.opengldemo.utils.ShaderUtil;
import com.suhang.opengldemo.utils.TextureUtil;
import com.suhang.opengldemo.utils.VectorUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.GL_DEPTH_BUFFER_BIT;

/**
 * Created by 苏杭 on 2017/2/5 13:54.
 */

public class OpenGlRenderFive implements GLSurfaceView.Renderer {
    public static final int VERTEX_COUNT = 3;
    public static final int VERTEX_COLOR_COUNT = 0;
    public static final int VERTEX_TEXTURE_COUNT = 2;
    public static final int FLOATBYTE = 4;
    public static final int STRIDE = (VERTEX_COUNT + VERTEX_COLOR_COUNT + VERTEX_TEXTURE_COUNT) * FLOATBYTE;

    Context mContext;
    private float[] mVertex;
    private FloatBuffer mVertexData;
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
    private float[][] mCube;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;

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
        mPosition = a;
        createViewMatrix();
    }

    private float scaleSpeed = 0.05f;

    public void scale(int dir) {
        if (dir > 0) {
            mSee = VectorUtil.add(mSee, VectorUtil.multiply(mPosition, scaleSpeed));
        } else {
            mSee = VectorUtil.reduce(mSee, VectorUtil.multiply(mPosition, scaleSpeed));
        }
        createViewMatrix();
    }

    private boolean isPress;
    private boolean isLeft;

    public void moveScreen(boolean isPress, boolean isLeft) {
        this.isPress = isPress;
        this.isLeft = isLeft;
    }


    private void getLocations() {
        mOutTexture = GLES30.glGetUniformLocation(mProgram, "outTexture");
        mModel = GLES30.glGetUniformLocation(mProgram, "model");
        mView = GLES30.glGetUniformLocation(mProgram, "view");
        mProjection = GLES30.glGetUniformLocation(mProgram, "projection");
    }

    private void createData() {
        mVertex = new float[]{
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,

                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,

                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,

                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
        };

        mCube = new float[][]{
                {0.0f, 0.0f, 0.0f},
                {2.0f, 3.0f, -6.0f},
                {-1.5f, -2.2f, -1.5f},
                {-1.8f, -2.0f, -4.3f},
                {2.4f, -0.4f, -2.5f},
                {-1.7f, 2.0f, -3.5f},
                {1.3f, -1.0f, -2.5f},
                {1.5f, 2.0f, -2.5f},
                {1.5f, 0.2f, -1.5f},
                {-1.3f, 1.0f, -1.5f}
        };

        mVertexData = ByteBuffer.allocateDirect(mVertex.length * FLOATBYTE).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexData.put(mVertex);
        mTexture = TextureUtil.loadTexture(mContext, R.mipmap.box5);
    }


    private float[] mSee = {0f, 0f, 5f};
    private float[] mPosition = {0f, 0f, -1f};
    private float[] mUp = {0f, 1f, 0f};


    private void init() {
        mProgram = ShaderUtil.createProgram(ShaderUtil.createShader(mContext, R.raw.vertex_shader_five, GLES30.GL_VERTEX_SHADER), ShaderUtil.createShader(mContext, R.raw.fragment_shader_five, GLES30.GL_FRAGMENT_SHADER));
        GLES30.glUseProgram(mProgram);
    }


    private void createProjectionMatrix(int width, int height) {
        Matrix.perspectiveM(mProjectionMatrix, 0, 45.0f, 1.0f * width / height, 0.1f, 100.0f);
    }

    private void createViewMatrix() {
        Matrix.setLookAtM(mViewMatrix, 0, mSee[0], mSee[1], mSee[2], mSee[0] + mPosition[0], mSee[1] + mPosition[1], mSee[2] + mPosition[2], mUp[0], mUp[1], mUp[2]);
    }

    private void createModelMatrix() {
        Matrix.setIdentityM(mModelMatrix, 0);
    }

    private void bindData() {
        mVertexData.position(0);
        GLES30.glVertexAttribPointer(0, VERTEX_COUNT, GLES30.GL_FLOAT, false, STRIDE, mVertexData);
        GLES30.glEnableVertexAttribArray(0);
        mVertexData.position(3);
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
        if (isPress) {
            if (isLeft) {
                mSee = VectorUtil.add(mSee, VectorUtil.normalize(VectorUtil.multiply(VectorUtil.multiply(mPosition, mUp), scaleSpeed), scaleSpeed));
            } else {
                mSee = VectorUtil.reduce(mSee, VectorUtil.normalize(VectorUtil.multiply(VectorUtil.multiply(mPosition, mUp), scaleSpeed), scaleSpeed));
            }
            createViewMatrix();
        }
        GLES30.glUniformMatrix4fv(mProjection, 1, false, mProjectionMatrix, 0);
        GLES30.glUniformMatrix4fv(mView, 1, false, mViewMatrix, 0);
        Matrix.setIdentityM(mModelMatrix, 0);
        for (int i = 0; i < 10; i++) {
            float[] cubes = mCube[i];
            Matrix.translateM(mModelMatrix, 0, cubes[0], cubes[1], cubes[2]);
            float angle = 20.0f * i;
            Matrix.rotateM(mModelMatrix, 0, angle, 1.0f, 0.3f, 0.5f);
            GLES30.glUniformMatrix4fv(mModel, 1, false, mModelMatrix, 0);
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
        }
    }

    private float getCurrentTime() {
        return 1.0f * SystemClock.uptimeMillis() / 1000;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float currentTime = getCurrentTime();
        deltaTime = currentTime - lastTime;
        lastTime = currentTime;
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);  // OpenGL docs.0
//        createViewMatrix();
        bindMatrix();
    }
}
