package com.suhang.opengldemo.render;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.suhang.opengldemo.R;
import com.suhang.opengldemo.function.Camera;
import com.suhang.opengldemo.utils.ShaderUtil;
import com.suhang.opengldemo.utils.TextureUtil;

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

public class OpenGlRenderSeven implements GLSurfaceView.Renderer {
    public static final int VERTEX_COUNT = 3;
    public static final int FLOATBYTE = 4;

    Context mContext;
    private int mProgram;
    private int mTexture;
    private float[] mModelMatrix = new float[16];
    private int mModel;
    private int mView;
    private int mProjection;
    private int mOutTexture;
    float deltaTime = 0.0f;
    float lastTime = 0.0f;
    private float[][] mCube;
    private float[] mSkyboxVertices;
    private Camera mCamera = new Camera(new float[]{0,0,5});

    public OpenGlRenderSeven(Context context) {
        mContext = context;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1f);  // OpenGL docs.
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
    }


    public Camera getCamera() {
        return mCamera;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    private boolean isPress;
    private int direction;

    public void moveScreen(boolean isPress, int direction) {
        this.isPress = isPress;
        this.direction = direction;
    }


    private void getLocations() {
        mOutTexture = GLES30.glGetUniformLocation(mProgram, "outTexture");
        mModel = GLES30.glGetUniformLocation(mProgram, "model");
        mView = GLES30.glGetUniformLocation(mProgram, "view");
        mProjection = GLES30.glGetUniformLocation(mProgram, "projection");
    }

    private FloatBuffer mSkyBuffer;

    private void createData() {
        mSkyboxVertices = new float[]{
                // Positions
                -1.0f, 1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,

                -1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, 1.0f,
                -1.0f, -1.0f, 1.0f,

                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,

                -1.0f, -1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, 1.0f,

                -1.0f, 1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, -1.0f,

                -1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, 1.0f
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
        mSkyBuffer = ByteBuffer.allocateDirect(mSkyboxVertices.length * FLOATBYTE).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mSkyBuffer.put(mSkyboxVertices);
        mTexture = TextureUtil.loadTextureCube(mContext, new int[]{R.mipmap.box0, R.mipmap.box1, R.mipmap.box2, R.mipmap.box3, R.mipmap.box4, R.mipmap.box5});
    }

    private void init() {
        mProgram = ShaderUtil.createProgram(ShaderUtil.createShader(mContext, R.raw.vertex_shader_five, GLES30.GL_VERTEX_SHADER), ShaderUtil.createShader(mContext, R.raw.fragment_shader_five, GLES30.GL_FRAGMENT_SHADER));
        GLES30.glUseProgram(mProgram);
    }

    private void bindData() {
        mSkyBuffer.position(0);
        GLES30.glVertexAttribPointer(0, VERTEX_COUNT, GLES30.GL_FLOAT, false, VERTEX_COUNT*FLOATBYTE, mSkyBuffer);
        GLES30.glEnableVertexAttribArray(0);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, mTexture);
        GLES30.glUniform1i(mOutTexture, 0);
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);// OpenGL docs.
        init();
        createData();
        getLocations();
        bindData();
        mCamera.onScreenChange(width, height);
        bindMatrix();
    }


    private void bindMatrix() {
        if (isPress) {
            mCamera.moveCamera(direction, deltaTime);
        }
        mCamera.bindMatrix(mView, mProjection);
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
        bindMatrix();
    }
}
