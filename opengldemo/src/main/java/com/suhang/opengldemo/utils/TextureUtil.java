package com.suhang.opengldemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLES31;
import android.opengl.GLUtils;

/**
 * Created by 苏杭 on 2017/3/1 16:50.
 */

public class TextureUtil {
	public static int loadTexture(Context context,int id) {
		int[] texture = new int[1];
		GLES31.glGenTextures(1,texture,0);
		if (texture[0]==0) {
			return 0;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id,options);
		if (bitmap == null) {
            GLES31.glDeleteTextures(1, texture, 0);
            return 0;
        }
//		GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
		GLES31.glBindTexture(GLES31.GL_TEXTURE_2D,texture[0]);
		GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D,GLES31.GL_TEXTURE_MIN_FILTER,GLES31.GL_LINEAR);
		GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D,GLES31.GL_TEXTURE_MAG_FILTER,GLES31.GL_LINEAR);
		GLUtils.texImage2D(GLES31.GL_TEXTURE_2D,0,bitmap,0);
		GLES31.glGenerateMipmap(GLES31.GL_TEXTURE_2D);
		GLES31.glBindTexture(GLES31.GL_TEXTURE_2D,0);
		bitmap.recycle();
		return texture[0];
	}
}
