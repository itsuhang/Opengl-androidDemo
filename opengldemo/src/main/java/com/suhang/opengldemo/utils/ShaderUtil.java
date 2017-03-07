package com.suhang.opengldemo.utils;

import android.content.Context;
import android.opengl.GLES31;

/**
 * Created by 苏杭 on 2017/3/3 9:55.
 */

public class ShaderUtil {
	public static int createShader(Context context,String fileName,int type) {
		String string = FileUtils.readTextFromAssets(context, fileName);
		int shader = GLES31.glCreateShader(type);
		GLES31.glShaderSource(shader,string);
		GLES31.glCompileShader(shader);
		int[] success = new int[1];
		GLES31.glGetShaderiv(shader,GLES31.GL_COMPILE_STATUS,success,0);
		if (success[0] <= 0) {
			String s = GLES31.glGetShaderInfoLog(shader);
			GLES31.glDeleteShader(shader);
			LogUtil.i("啊啊啊"+s);
		}
		return shader;
	}

	public static int createProgram(int vertexShader, int fragShader) {
		int program = GLES31.glCreateProgram();
		GLES31.glAttachShader(program,vertexShader);
		GLES31.glAttachShader(program,fragShader);
		GLES31.glLinkProgram(program);
		int[] success = new int[1];
		GLES31.glGetProgramiv(program, GLES31.GL_LINK_STATUS, success, 0);
		if (success[0] <= 0) {
			String s = GLES31.glGetProgramInfoLog(program);
			GLES31.glDeleteProgram(program);
			LogUtil.i("啊啊啊"+s);
		}
		return program;
	}

	public static int createShader(Context context,int id,int type) {
		String string = FileUtils.readTextFromRaw(context, id);
		int shader = GLES31.glCreateShader(type);
		GLES31.glShaderSource(shader,string);
		GLES31.glCompileShader(shader);
		int[] success = new int[1];
		GLES31.glGetShaderiv(shader,GLES31.GL_COMPILE_STATUS,success,0);
		if (success[0] <= 0) {
			String s = GLES31.glGetShaderInfoLog(shader);
			GLES31.glDeleteShader(shader);
			LogUtil.i("啊啊啊"+s);
		}
		return shader;
	}
}
