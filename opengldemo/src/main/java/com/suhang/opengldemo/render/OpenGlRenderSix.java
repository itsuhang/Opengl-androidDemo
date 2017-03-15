package com.suhang.opengldemo.render;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.suhang.opengldemo.R;
import com.suhang.opengldemo.function.Camera;
import com.suhang.opengldemo.interfaces.CanTranform;
import com.suhang.opengldemo.utils.ShaderUtil;

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

public class OpenGlRenderSix implements GLSurfaceView.Renderer, CanTranform {
    public static final int VERTEX_COUNT = 3;
    public static final int FLOATBYTE = 4;

    Context mContext;
    private int mObjectProgram;
    private int mLightProgram;
    //    private int mTexture;
    private float[] mModelMatrix = new float[16];
    private int mObjectModel;
    private int mObjectView;
    private int mObjectProjection;
    private int mLightPos;
    private int mLightModel;
    private int mLightView;
    private int mLightProjection;
    //    private int mOutTexture;
    float deltaTime = 0.0f;
    float lastTime = 0.0f;
    private float[] mVertices;
    private Camera mCamera = new Camera(new float[]{0, 0, 5});
    private int mObjectColor;
    private int mLightColor;
    private float[] lightPos = {1.2f, 1.0f, 2.0f};

    public OpenGlRenderSix(Context context) {
        mContext = context;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1f);  // OpenGL docs.
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
    }

    @Override
    public Camera getCamera() {
        return mCamera;
    }

    @Override
    public float getDeltaTime() {
        return deltaTime;
    }

    private boolean isPress;
    private int direction;

    @Override
    public void moveScreen(boolean isPress, int direction) {
        this.isPress = isPress;
        this.direction = direction;
    }


    private void getLocations() {
//        mOutTexture = GLES30.glGetUniformLocation(mObjectProgram, "outTexture");
        mObjectModel = GLES30.glGetUniformLocation(mObjectProgram, "model");
        mObjectView = GLES30.glGetUniformLocation(mObjectProgram, "view");
        mObjectProjection = GLES30.glGetUniformLocation(mObjectProgram, "projection");
        mLightPos = GLES30.glGetUniformLocation(mObjectProgram, "lightPos");


        mLightModel = GLES30.glGetUniformLocation(mLightProgram, "model");
        mLightView = GLES30.glGetUniformLocation(mLightProgram, "view");
        mLightProjection = GLES30.glGetUniformLocation(mLightProgram, "projection");


        mObjectColor = GLES30.glGetUniformLocation(mObjectProgram, "objectColor");
        mLightColor = GLES30.glGetUniformLocation(mObjectProgram, "lightColor");
    }

    private FloatBuffer mVertexBuffer;

    private void createData() {
        mVertices = new float[]{
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,

                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,

                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,

                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
                0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
                0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,

                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f
        };

        mVertexBuffer = ByteBuffer.allocateDirect(mVertices.length * FLOATBYTE).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.put(mVertices);
    }

    private void init() {
        mObjectProgram = ShaderUtil.createProgram(ShaderUtil.createShader(mContext, R.raw.vertex_shader_six, GLES30.GL_VERTEX_SHADER), ShaderUtil.createShader(mContext, R.raw.fragment_shader_six, GLES30.GL_FRAGMENT_SHADER));
        mLightProgram = ShaderUtil.createProgram(ShaderUtil.createShader(mContext, R.raw.vertex_shader_six_light, GLES30.GL_VERTEX_SHADER), ShaderUtil.createShader(mContext, R.raw.fragment_shader_six_light, GLES30.GL_FRAGMENT_SHADER));
    }

    private void bindData() {
        mVertexBuffer.position(0);
        GLES30.glVertexAttribPointer(0, VERTEX_COUNT, GLES30.GL_FLOAT, false, 6 * FLOATBYTE, mVertexBuffer);
        GLES30.glEnableVertexAttribArray(0);
        mVertexBuffer.position(3);
        GLES30.glVertexAttribPointer(1, VERTEX_COUNT, GLES30.GL_FLOAT, false, 6 * FLOATBYTE, mVertexBuffer);
        GLES30.glEnableVertexAttribArray(1);
        mVertexBuffer.position(0);
        GLES30.glVertexAttribPointer(0, VERTEX_COUNT, GLES30.GL_FLOAT, false, 6 * FLOATBYTE, mVertexBuffer);
        GLES30.glEnableVertexAttribArray(0);
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
        GLES30.glUseProgram(mObjectProgram);
        GLES30.glUniform3f(mObjectColor, 1.0f, 0.5f, 0.31f);
        GLES30.glUniform3f(mLightColor, 1.0f, 1.0f, 1.0f);
        GLES30.glUniform3f(mLightPos,lightPos[0],lightPos[1],lightPos[2]);
        mCamera.bindMatrix(mObjectView, mObjectProjection);
        Matrix.setIdentityM(mModelMatrix, 0);
        GLES30.glUniformMatrix4fv(mObjectModel, 1, false, mModelMatrix, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);



        GLES30.glUseProgram(mLightProgram);
        if (isPress) {
            mCamera.moveCamera(direction, deltaTime);
        }
        mCamera.bindMatrix(mLightView, mLightProjection);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, lightPos[0], lightPos[1], lightPos[2]);
        Matrix.scaleM(mModelMatrix, 0, 0.4f, 0.4f, 0.4f);
        GLES30.glUniformMatrix4fv(mLightModel, 1, false, mModelMatrix, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
    }

    private float getCurrentTime() {
        return 1.0f * SystemClock.uptimeMillis() / 1000;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float currentTime = getCurrentTime();
        deltaTime = currentTime - lastTime;
        lastTime = currentTime;
        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);  // OpenGL docs.0
        bindMatrix();
    }
}
