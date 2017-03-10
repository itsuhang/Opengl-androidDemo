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

	/**
	 * 向量标准化
	 *
	 * @param a
	 * @return
	 */
	public static float[] normalize(float[] a, float speed) {
		float[] b = new float[a.length];
		float x = a[0];
		float y = a[1];
		float z = a[2];
		float sqrt = (float) Math.sqrt(x * x + y * y + z * z);
//		LogUtil.i("啊啊啊sqrt"+sqrt);
		if (sqrt == 0) {
			b[0] = 0;
			b[1] = 0;
			b[2] = 0;
		} else {
			b[0] = x / sqrt;
			b[1] = y / sqrt;
			b[2] = z / sqrt;
		}
		return multiply(b, speed);
	}

	public static float angleTransform(float angle) {
		return (float) (angle * (Math.PI / 180.0f));
	}
}
