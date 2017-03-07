package com.suhang.opengldemo.render;

import android.content.Context;
import android.opengl.GLES31;
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
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;

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
    private int mTexture1;
    private float[] mMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private int mTransform;
    private int mOutTexture;
    private int mOutTexture1;

    public OpenGlRenderFour(Context context) {
        mContext = context;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background color to black ( rgba ).
        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1f);  // OpenGL docs.
        GLES31.glEnable(GLES31.GL_DEPTH_TEST);
    }

    private void getLocations() {
        mOutTexture = GLES31.glGetUniformLocation(mProgram, "outTexture");
        mOutTexture1 = GLES31.glGetUniformLocation(mProgram, "outTexture1");
        mTransform = GLES31.glGetUniformLocation(mProgram, "transform");
    }

    private void createData() {
        mVertex = new float[]{
                0.5f, 0.5f * factor, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,   // 右上
                0.5f, -0.5f * factor, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,   // 右下
                -0.5f, -0.5f * factor, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,   // 左下
                -0.5f, 0.5f * factor, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f    // 左上
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
        mProgram = ShaderUtil.createProgram(ShaderUtil.createShader(mContext, R.raw.vertex_shader_four, GLES31.GL_VERTEX_SHADER), ShaderUtil.createShader(mContext, R.raw.fragment_shader_four, GLES31.GL_FRAGMENT_SHADER));
        GLES31.glUseProgram(mProgram);
    }


    private void createProjectionMatrix(int width, int height) {
        float ratio = 1;
        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;
        float near = 2;
        float far = 12;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    private void createViewMatrix() {
        // точка положения камеры
        float eyeX = 0;
        float eyeY = 2;
        float eyeZ = 4;

        // точка направления камеры
        float centerX = 0;
        float centerY = 0;
        float centerZ = 0;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
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
        GLES31.glUniform1i(mOutTexture, 0);
        GLES31.glActiveTexture(GLES31.GL_TEXTURE1);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, mTexture1);
        GLES31.glUniform1i(mOutTexture1, 1);
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
        createViewMatrix();
        Matrix.setIdentityM(mModelMatrix, 0);
        createProjectionMatrix(width, height);
    }
    long TIME = 10000L;

    private void bindMatrix() {
        float angle = (float)(SystemClock.uptimeMillis() % TIME) / TIME * 360;
        Matrix.rotateM(mModelMatrix,0,angle,0f,0.0f,1f);
//        Matrix.translateM(mModelMatrix, 0, 0.5f, 0.0f, 0.0f);
        GLES31.glUniformMatrix4fv(mTransform, 1, false, mModelMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);  // OpenGL docs.0
        Matrix.setIdentityM(mModelMatrix, 0);
        bindMatrix();
//		gl.glLoadIdentity();
//		gl.glTranslatef(0, 0, -4);
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, 6, GLES31.GL_UNSIGNED_INT, mIndicesData);
//		GLES31.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}
