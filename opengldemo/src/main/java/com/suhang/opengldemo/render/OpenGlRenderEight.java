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
import com.suhang.opengldemo.utils.TextureUtil;
import com.suhang.opengldemo.utils.VectorUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.GL_DEPTH_BUFFER_BIT;

/**
 * Created by 苏杭 on 2017/2/5 13:54.
 */

public class OpenGlRenderEight implements GLSurfaceView.Renderer, CanTranform {
    public static final int VERTEX_COUNT = 3;
    public static final int NORMAL_COUNT = 3;
    public static final int TEXTURE_COUNT = 2;
    public static final int FLOATBYTE = 4;
    public static final int GROUP_COUNT = VERTEX_COUNT + NORMAL_COUNT+TEXTURE_COUNT;
    public static final int STRIED = GROUP_COUNT * FLOATBYTE;

    Context mContext;
    private int mObjectProgram;
    private int mLightProgram;
    private float[] mModelMatrix = new float[16];
    private int mObjectModel;
    private int mObjectView;
    private int mObjectProjection;
    private int mViewPos;
    private int mObjectDiffuse;
    private int mObjectSpecular;
    private int mObjectShininess;
    private int mLightAmbient;
    private int mLightDiffuse;
    private int mLightSpecular;
    private int mLightPosition;


    private int mLightModel;
    private int mLightView;
    private int mLightProjection;
    private float deltaTime = 0.0f;
    private float lastTime = 0.0f;
    private float[] mVertices;
    private Camera mCamera = new Camera(new float[]{1, 0, 7});
    private float[] mLightPos = {1.2f, 1.0f, 2f};
    private int mTexture;
    private int mTextureSpecular;

    public OpenGlRenderEight(Context context) {
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
        //物品的模型,观察,投影矩阵
        mObjectModel = GLES30.glGetUniformLocation(mObjectProgram, "model");
        mObjectView = GLES30.glGetUniformLocation(mObjectProgram, "view");
        mObjectProjection = GLES30.glGetUniformLocation(mObjectProgram, "projection");
        mViewPos = GLES30.glGetUniformLocation(mObjectProgram, "viewPos");
        mObjectDiffuse = GLES30.glGetUniformLocation(mObjectProgram, "material.diffuse");
        mObjectSpecular = GLES30.glGetUniformLocation(mObjectProgram, "material.specular");
        mObjectShininess = GLES30.glGetUniformLocation(mObjectProgram, "material.shininess");
        mLightAmbient = GLES30.glGetUniformLocation(mObjectProgram, "light.ambient");
        mLightDiffuse = GLES30.glGetUniformLocation(mObjectProgram, "light.diffuse");
        mLightSpecular = GLES30.glGetUniformLocation(mObjectProgram, "light.specular");
        mLightPosition = GLES30.glGetUniformLocation(mObjectProgram, "light.position");


        //光源的模型,观察,投影矩阵
        mLightModel = GLES30.glGetUniformLocation(mLightProgram, "model");
        mLightView = GLES30.glGetUniformLocation(mLightProgram, "view");
        mLightProjection = GLES30.glGetUniformLocation(mLightProgram, "projection");
    }

    private FloatBuffer mVertexBuffer;

    private void createData() {
        mVertices = new float[]{
                //后面
                -0.5f, -0.5f, -0.5f, 0, 0, -1f, 0, 0,
                0.5f, -0.5f, -0.5f, 0, 0, -1f, 1, 0,
                0.5f, 0.5f, -0.5f, 0, 0, -1f, 1, 1,
                0.5f, 0.5f, -0.5f, 0, 0, -1f, 1, 1,
                -0.5f, 0.5f, -0.5f, 0, 0, -1f, 0, 1,
                -0.5f, -0.5f, -0.5f, 0, 0, -1f, 0, 0,

                //前面
                -0.5f, -0.5f, 0.5f, 0, 0, 1f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 0, 0, 1f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 0, 0, 1f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 0, 0, 1f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 0, 0, 1f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0, 0, 1f, 0.0f, 0.0f,

                //左面
                -0.5f, 0.5f, 0.5f, -1.0f, 0, 0, 1.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, -1.0f, 0, 0, 1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f, 0, 0, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f, 0, 0, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, -1.0f, 0, 0, 0.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, -1.0f, 0, 0, 1.0f, 0.0f,

                //右面
                0.5f, 0.5f, 0.5f, 1.0f, 0, 0, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 0, 0, 1.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0, 0, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0, 0, 0.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0, 0, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0, 0, 1.0f, 0.0f,

                //下面
                -0.5f, -0.5f, -0.5f, 0, -1.0f, 0, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0, -1.0f, 0, 1.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0, -1.0f, 0, 1.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0, -1.0f, 0, 1.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0, -1.0f, 0, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 0, -1.0f, 0, 0.0f, 0.0f,

                //上面
                -0.5f, 0.5f, -0.5f, 0, 1.0f, 0, 0.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 0, 1.0f, 0, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 0, 1.0f, 0, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 0, 1.0f, 0, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 0, 1.0f, 0, 0.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 0, 1.0f, 0, 0.0f, 0.0f
        };

        mVertexBuffer = ByteBuffer.allocateDirect(mVertices.length * FLOATBYTE).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.put(mVertices);
        mTexture = TextureUtil.loadTexture(mContext, R.mipmap.container2);
        mTextureSpecular = TextureUtil.loadTexture(mContext, R.mipmap.container2_specular);
    }

    private void init() {
        mObjectProgram = ShaderUtil.createProgram(ShaderUtil.createShader(mContext, R.raw.vertex_shader_eight, GLES30.GL_VERTEX_SHADER), ShaderUtil.createShader(mContext, R.raw.fragment_shader_eight, GLES30.GL_FRAGMENT_SHADER));
        mLightProgram = ShaderUtil.createProgram(ShaderUtil.createShader(mContext, R.raw.vertex_shader_seven_light, GLES30.GL_VERTEX_SHADER), ShaderUtil.createShader(mContext, R.raw.fragment_shader_seven_light, GLES30.GL_FRAGMENT_SHADER));
    }

    private void bindData() {
        mVertexBuffer.position(0);
        GLES30.glVertexAttribPointer(0, VERTEX_COUNT, GLES30.GL_FLOAT, false, STRIED, mVertexBuffer);
        GLES30.glEnableVertexAttribArray(0);
        mVertexBuffer.position(VERTEX_COUNT);
        GLES30.glVertexAttribPointer(1, NORMAL_COUNT, GLES30.GL_FLOAT, false, STRIED, mVertexBuffer);
        GLES30.glEnableVertexAttribArray(1);
        mVertexBuffer.position(VERTEX_COUNT+NORMAL_COUNT);
        GLES30.glVertexAttribPointer(2, TEXTURE_COUNT, GLES30.GL_FLOAT, false, STRIED, mVertexBuffer);
        GLES30.glEnableVertexAttribArray(2);

        mVertexBuffer.position(0);
        GLES30.glVertexAttribPointer(0, VERTEX_COUNT, GLES30.GL_FLOAT, false, STRIED, mVertexBuffer);
        GLES30.glEnableVertexAttribArray(0);

        GLES30.glUseProgram(mObjectProgram);
        GLES30.glUniform1i(mObjectDiffuse, 0);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GL_TEXTURE_2D,mTexture);
        GLES30.glUniform1i(mObjectSpecular,1);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
        GLES30.glBindTexture(GL_TEXTURE_2D,mTextureSpecular);
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
        GLES30.glUseProgram(mObjectProgram);
        GLES30.glUniform3f(mLightSpecular,0.5f,0.5f,0.5f);
        GLES30.glUniform1f(mObjectShininess, 32f);
        GLES30.glUniform3f(mLightPosition, mLightPos[0], mLightPos[1], mLightPos[2]);
        GLES30.glUniform3f(mViewPos, mCamera.position[0], mCamera.position[1], mCamera.position[2]);
        GLES30.glUniform3f(mLightAmbient, 0.2f, 0.2f, 0.2f);
        GLES30.glUniform3f(mLightDiffuse, 0.5f, 0.5f, 0.5f);
        GLES30.glUniform3f(mLightSpecular, 1.0f, 1.0f, 1.0f);
        mCamera.bindMatrix(mObjectView, mObjectProjection);
        Matrix.setIdentityM(mModelMatrix, 0);
        GLES30.glUniformMatrix4fv(mObjectModel, 1, false, mModelMatrix, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, mVertices.length / GROUP_COUNT);

        GLES30.glUseProgram(mLightProgram);
        mCamera.bindMatrix(mLightView, mLightProjection);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, mLightPos[0], mLightPos[1], mLightPos[2]);
        Matrix.scaleM(mModelMatrix, 0, 0.2f, 0.2f, 0.2f);
        GLES30.glUniformMatrix4fv(mLightModel, 1, false, mModelMatrix, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, mVertices.length / GROUP_COUNT);

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
