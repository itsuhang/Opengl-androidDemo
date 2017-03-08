package com.suhang.opengldemo.utils;

/**
 * Created by 苏杭 on 2017/3/8 18:13.
 */

public class VectorUtil {
    public static float[] add(float[] a, float[] b) {
        if (a.length != b.length) {
            return null;
        }
        float[] c = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i] + b[i];
        }
        return c;
    }

    public static float[] reduce(float[] a, float[] b) {
        if (a.length != b.length) {
            return null;
        }
        float[] c = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i] - b[i];
        }
        return c;
    }

    public static float[] multiply(float[] a, float[] b) {
        if (a.length != b.length) {
            return null;
        }
        float[] c = new float[a.length];
        if (a.length == 3) {
            c[0] = a[1] * b[2] - a[2] * b[1];
            c[1] = a[2] * b[0] - a[0] * b[2];
            c[2] = a[0] * b[1] - a[1] * b[0];
        }
        return c;
    }

    public static float[] multiply(float[] a, float b) {
        float[] c = new float[a.length];
        for (int i = 0; i < c.length; i++) {
            c[i] = a[i] * b;
        }
        return c;
    }
}
