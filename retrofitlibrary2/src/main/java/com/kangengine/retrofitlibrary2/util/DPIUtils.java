package com.kangengine.retrofitlibrary2.util;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;


import java.util.ArrayList;
import java.util.List;

public class DPIUtils {

	private static Display defaultDisplay;
	private static float mDensity;
	private static Point displaySize;

	static class SdkVersionGreateThan13 {
		static void getSize(Point outSize) {
			defaultDisplay.getSize(outSize);
		}
	}

	static class SdkVersionLessThan13 {

		static void getSize(Point outSize) {
			outSize.x = defaultDisplay.getWidth();
			outSize.y = defaultDisplay.getHeight();
		}
	}

	public static int dip2px(float paramFloat) {
		return (int) (mDensity * paramFloat + 0.5F);
	}

	public static void setSize(Point outSize) {
		if (Build.VERSION.SDK_INT >= 13) {
			SdkVersionGreateThan13.getSize(outSize);
		} else {
			SdkVersionLessThan13.getSize(outSize);
		}
		displaySize = outSize;

	}

	public static int getHeight() {
		if (displaySize != null) {
			return displaySize.y;
		} else {
			return 0;
		}
	}

	public static int getWidth() {
		if (displaySize != null) {
			return displaySize.x;
		} else {
			return 0;
		}

	}

	public static int percentHeight(float paramFloat) {
		return (int) (displaySize.y * paramFloat);
	}

	public static int percentWidth(float paramFloat) {
		return (int) (displaySize.x * paramFloat);
	}

	public static int px2dip(Context paramContext, float paramFloat) {
		float f = mDensity;
		return (int) (paramFloat / f + 0.5F);
	}

	public static void setDefaultDisplay(Display paramDisplay) {
		defaultDisplay = paramDisplay;
		displaySize = new Point();
		setSize(displaySize);
	}

	public static void setDensity(float paramFloat) {
		mDensity = paramFloat;
	}

	public static float getDensity() {
		return mDensity;
	}

	public static Display getDisplay() {
		return defaultDisplay;
	}
	
	public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
	
    /**
     * 将sp值转换为px值，保证文字大小不变
     * 
     * @param context
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f);
    }

	/**
	 * 调整FrameLayout大小
	 *
	 * @param tp
	 */
	public static void resizePikcer(FrameLayout tp) {
		float[] size = null;
		//npList size==3 代表 datepicker 年月日宽度对应为 0.25f 0.2f 0.2f
		//npList size==2 代表 timepicker 时分宽度对应为 0.175f 0.175f
		List<NumberPicker> npList = findNumberPicker(tp);
		if (npList.size() == 3) {
			size = new float[]{0.25f, 0.2f, 0.2f};
		} else if (npList.size() == 2) {
			size = new float[]{0.175f, 0.175f};

		}
		for (int i = 0; i < npList.size(); i++) {
			NumberPicker np = npList.get(i);
			resizeNumberPicker(np, size[i]);
		}
	}

	/**
	 * 得到viewGroup里面的numberpicker组件
	 *
	 * @param viewGroup
	 * @return
	 */
	private static List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {

		List<NumberPicker> npList = new ArrayList<NumberPicker>();
		View child = null;
		if (null != viewGroup) {
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				child = viewGroup.getChildAt(i);
				if (child instanceof NumberPicker) {
					npList.add((NumberPicker) child);
				} else if (child instanceof LinearLayout) {
					List<NumberPicker> result = findNumberPicker((ViewGroup) child);
					if (result.size() > 0) {
						return result;
					}
				}
			}
		}
		return npList;
	}

	/**
	 * 调整numberpicker大小
	 * @param np
	 * @param size 每个numberPicker对应分得屏幕宽度
	 */
	private static void resizeNumberPicker(NumberPicker np, float size) {
		int dp5 = dip2px(np.getContext(), 5);
		//timepicker 时 分 左右各自有8dp空白
		int dp32 = dip2px(np.getContext(), 32);
		//屏幕宽度 - timepicker左右空白 -自设周边5dp空白
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) ((getWidth() - dp32 - dp5 * 10) *size), ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(dp5, 0, dp5, 0);
		np.setLayoutParams(params);
	}

	/**
	 * 获得屏幕宽度
	 *
	 * @return
	 */
	public static int getScreenWidth(Context context)
	{
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * 获得屏幕高度
	 *
	 * @return
	 */
	public static int getScreenHeight(Context context)
	{
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}
}
