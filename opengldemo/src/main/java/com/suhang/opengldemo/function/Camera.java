package com.suhang.opengldemo.function;

import android.opengl.GLES30;
import android.opengl.Matrix;

import com.suhang.opengldemo.utils.LogUtil;
import com.suhang.opengldemo.utils.VectorUtil;

/**
 * Created by 苏杭 on 2017/3/14 15:55.
 * 摄像机旋转类
 */

public class Camera {
    public static final int LEFT = 100;
    public static final int RIGHT = 101;
    public static final int FORWARD = 102;
    public static final int BACKWARD = 103;
    public static final int SCALE_BIG = 104;
    public static final int SCALE_SMALL = 105;

    private static final float YAW = -90f;
    private static final float PITCH = 0.0f;
    private static final float SPEED = 5.0f;
    private static final float SCALESPEED = 50.0f;
    private static final float SENSITIVTY = 0.1f;
    private static final float ZOOM = 45.0f;

    public float[] position = new float[]{0, 0, 3};
    private float[] front = new float[]{0, 0, -1};
    private float[] worldUp = new float[]{0, 1, 0};
    private float[] up = new float[3];
    private float[] right = new float[3];
    private float yaw = YAW;
    private float pitch = PITCH;
    private float movementSpeed = SPEED;
    private float scaleSpeed = SCALESPEED;
    private float touchSensitivity = SENSITIVTY;
    private float zoom = ZOOM;
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private int width;
    private int height;

    public Camera() {
        updateCameraVectors();
    }

    public Camera(float[] position) {
        this.position = position;
        updateCameraVectors();
    }
    public Camera(float[] position,float[] front) {
        this.position = position;
        this.front = front;
        updateCameraVectors();
    }

    public Camera(float[] position, float[] up, float yaw, float pitch) {
        this.position = position;
        this.worldUp = up;
        this.yaw = yaw;
        this.pitch = pitch;
        updateCameraVectors();
    }


    public void moveCamera(int direction, float deltaTime) {
        float v = movementSpeed * deltaTime;
        if (direction == LEFT) {
            position = VectorUtil.add(position, VectorUtil.normalize(VectorUtil.multiply(VectorUtil.multiply(front, worldUp), v), v));
        } else if (direction == RIGHT) {
            position = VectorUtil.reduce(position, VectorUtil.normalize(VectorUtil.multiply(VectorUtil.multiply(front, worldUp), v), v));
        } else if (direction == FORWARD) {
            position = VectorUtil.add(position, VectorUtil.multiply(front, v));
        } else if (direction == BACKWARD) {
            position = VectorUtil.reduce(position, VectorUtil.multiply(front, v));
        }
    }

    public void onScreenChange(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void bindMatrix(int view, int projection) {
        Matrix.setLookAtM(viewMatrix, 0, position[0], position[1], position[2], position[0] + front[0], position[1] + front[1], position[2] + front[2], worldUp[0], worldUp[1], worldUp[2]);
        Matrix.perspectiveM(projectionMatrix, 0, zoom, 1.0f * width / height, 0.1f, 100.0f);
        GLES30.glUniformMatrix4fv(projection, 1, false, projectionMatrix, 0);
        GLES30.glUniformMatrix4fv(view, 1, false, viewMatrix, 0);
    }

    public void touchMove(float dx, float dy) {
        dx *= touchSensitivity;
        dy *= touchSensitivity;
        yaw += dx;
        pitch += dy;
        if (pitch >= 89.0f) {
            pitch = 89.0f;
        } else if (pitch <= -89.0f) {
            pitch = -89.0f;
        }
        updateCameraVectors();
    }

    public void scale(int direction, float deltaTime) {
        float v = 0;
        if (direction == SCALE_BIG) {
            v = scaleSpeed * deltaTime;
        } else if(direction==SCALE_SMALL){
            v = -scaleSpeed * deltaTime;
        }
        zoom -= v;
        if (zoom > 1 && zoom < 45) {
            zoom -= v;
        } else if (zoom <= 1) {
            zoom = 1;
        } else if (zoom >= 45) {
            zoom = 45;
        }
    }

    private void updateCameraVectors() {
        float[] aFront = new float[3];
        aFront[0] = (float) (Math.cos(VectorUtil.angleTransform(yaw)) * Math.cos(VectorUtil.angleTransform(pitch)));
        aFront[1] = (float) Math.sin(VectorUtil.angleTransform(pitch));
        aFront[2] = (float) (Math.cos(VectorUtil.angleTransform(pitch)) * Math.sin(VectorUtil.angleTransform(yaw)));
        front = VectorUtil.normalize(aFront, 1);
        right = VectorUtil.normalize(VectorUtil.multiply(front, worldUp), 1);
        up = VectorUtil.normalize(VectorUtil.multiply(right, front), 1);
    }
}
