package com.suhang.opengldemo.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * Created by sh on 2016/4/26 11:25.
 */
public class ScreenUtils {
	/**
	 * 获取状态栏高度
	 * @param context
	 * @return
     */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
				"android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * 获取导航栏高度
	 * @param context
	 * @return
     */
	public static int getNavigationBarHeight(Context context) {
		int totalHeight = getHasVirtualKeyHeight(context);

		int contentHeight = getScreenHeight(context);

		return totalHeight  - contentHeight;
	}

	/**
	 * 通过反射，获取包含虚拟键的整体屏幕高度（真实高度）
	 *
	 * @return
	 */
	public static int getHasVirtualKeyHeight(Context context) {
		int dpi = 0;
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
			method.invoke(display, dm);
			dpi = dm.heightPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dpi;
	}

	/**
	 * 获取屏幕高度（不包括系统ui）
	 * @param context
	 * @return
     */
	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 获取屏幕宽度（不包括系统ui）
	 * @param context
	 * @return
     */
	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * 通过反射，获取包含虚拟键的整体屏幕宽度（真实宽度）
	 *
	 * @return
	 */
	public static int getHasVirtualKeyWidth(Context context) {
		int dpi = 0;
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
			method.invoke(display, dm);
			dpi = dm.widthPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dpi;
	}


	/**
	 * 判断导航栏是否显示（用户是否滑出导航栏）
	 * @param context
	 * @return
     */
	public static boolean isNavigationBarShow(Context context){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = manager.getDefaultDisplay();
			Point size = new Point();
			Point realSize = new Point();
			display.getSize(size);
			display.getRealSize(realSize);
			return realSize.y!=size.y;
		}else {
			boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
			boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
			return !(menu || back);
		}
	}
}
