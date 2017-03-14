package com.suhang.opengldemo.utils;

import android.content.Context;
import android.opengl.GLES30;

/**
 * Created by 苏杭 on 2017/3/3 9:55.
 */

public class ShaderUtil {
	public static int createShader(Context context,String fileName,int type) {
		String string = FileUtils.readTextFromAssets(context, fileName);
		int shader = GLES30.glCreateShader(type);
		GLES30.glShaderSource(shader,string);
		GLES30.glCompileShader(shader);
		int[] success = new int[1];
		GLES30.glGetShaderiv(shader,GLES30.GL_COMPILE_STATUS,success,0);
		if (success[0] <= 0) {
			String s = GLES30.glGetShaderInfoLog(shader);
			GLES30.glDeleteShader(shader);
			LogUtil.i("啊啊啊"+s);
		}
		return shader;
	}

	public static int createProgram(int vertexShader, int fragShader) {
		int program = GLES30.glCreateProgram();
		GLES30.glAttachShader(program,vertexShader);
		GLES30.glAttachShader(program,fragShader);
		GLES30.glLinkProgram(program);
		int[] success = new int[1];
		GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, success, 0);
		if (success[0] <= 0) {
			String s = GLES30.glGetProgramInfoLog(program);
			GLES30.glDeleteProgram(program);
			LogUtil.i("啊啊啊"+s);
		}
		return program;
	}

	public static int createShader(Context context,int id,int type) {
		String string = FileUtils.readTextFromRaw(context, id);
		int shader = GLES30.glCreateShader(type);
		GLES30.glShaderSource(shader,string);
		GLES30.glCompileShader(shader);
		int[] success = new int[1];
		GLES30.glGetShaderiv(shader,GLES30.GL_COMPILE_STATUS,success,0);
		if (success[0] <= 0) {
			String s = GLES30.glGetShaderInfoLog(shader);
			GLES30.glDeleteShader(shader);
			LogUtil.i("啊啊啊"+s);
		}
		return shader;
	}
}
