package com.suhang.opengldemo.utils;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {

	public static String readTextFromRaw(Context context, int rawId) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader bufferedReader = null;
			try {
				InputStream inputStream =
						context.getResources().openRawResource(rawId);
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuilder.append(line);
					stringBuilder.append("\r\n");
				}
			} finally {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			}
		} catch (IOException | Resources.NotFoundException ioex) {
			ioex.printStackTrace();
		}
		return stringBuilder.toString();
	}

	public static String readTextFromAssets(Context context, String fileName) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader bufferedReader = null;
			try {
				InputStream inputStream =
						context.getAssets().open(fileName);
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuilder.append(line);
					stringBuilder.append("\r\n");
				}
			} finally {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			}
		} catch (IOException | Resources.NotFoundException ioex) {
			ioex.printStackTrace();
		}
		return stringBuilder.toString();
	}
}