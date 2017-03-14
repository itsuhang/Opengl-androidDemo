package com.suhang.opengldemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;

/**
 * Created by 苏杭 on 2017/3/1 16:50.
 */

public class TextureUtil {
    public static int loadTexture(Context context, int id) {
        int[] texture = new int[1];
        GLES30.glGenTextures(1, texture, 0);
        if (texture[0] == 0) {
            return 0;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id, options);
        if (bitmap == null) {
            GLES30.glDeleteTextures(1, texture, 0);
            return 0;
        }
//		GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture[0]);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT);    // Set texture wrapping to GL_REPEAT
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        bitmap.recycle();
        return texture[0];
    }


    //	public static int loadTextureCube(Context context, int[] resourceId) {
//        // создание объекта текстуры
//        final int[] textureIds = new int[1];
//		GLES30.glGenTextures(1, textureIds, 0);
//        if (textureIds[0] == 0) {
//            return 0;
//        }
//
//        // получение Bitmap
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inScaled = false;
//
//        Bitmap[] bitmaps = new Bitmap[6];
//        for (int i = 0; i < 6; i++) {
//            bitmaps[i] = BitmapFactory.decodeResource(
//                    context.getResources(), resourceId[i], options);
//
//            if (bitmaps[i] == null) {
//                GLES30.glDeleteTextures(1, textureIds, 0);
//                return 0;
//            }
//        }
//
//        // настройка объекта текстуры
//		GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, textureIds[0]);
//
//        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
//		GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
//
//        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, bitmaps[0], 0);
//        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, bitmaps[1], 0);
//
//        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, bitmaps[2], 0);
//        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, bitmaps[3], 0);
//
//        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, bitmaps[4], 0);
//        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, bitmaps[5], 0);
//
//        for (Bitmap bitmap : bitmaps) {
//            bitmap.recycle();
//        }
//
//        // сброс target
//		GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, 0);
//
//        return textureIds[0];
//    }
    public static int loadTextureCube(Context context, int[] resourceId) {
        if (resourceId.length < 6) {
            return 0;
        }
        int[] textures = new int[1];
        GLES30.glGenTextures(1, textures, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, textures[0]);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP,GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP,GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP,GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP,GLES30.GL_TEXTURE_WRAP_R,GLES30.GL_CLAMP_TO_EDGE);
        for (int i = 0; i < 6; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId[i]);
            if (bitmap == null) {
                GLES30.glDeleteTextures(1, textures, 0);
                return 0;
            }
            int target = GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i;
            GLUtils.texImage2D(target, 0, bitmap, 0);
            bitmap.recycle();
        }
        return textures[0];
    }
}
