package com.eason.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Rect;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * DisplayUtils
 * <ul>
 * <li>{@link #getDisplay(Context)}获取显示设备</li>
 * <li>{@link #getScreenWidth(Context)}获取屏幕宽度</li>
 * <li>{@link #getScreenHeight(Context)} 获取屏幕高度大小</li>
 * <li>{@link #dipToPx(Context, float)}dip转换为px</li>
 * <li>{@link #pxToDip(Context, int)}px转换为dip</li>
 * <li>{@link #getDensityDpi(Context)}获取屏幕密度</li>
 * <li>{@link #getStatusHeight(Activity)}获得状态栏高度</li>
 * </ul>
 */
public class DisplayUtils {
    private static final String TAG = DisplayUtils.class.getSimpleName();

    /**
     * 获取显示设备参数
     * 
     * @param context
     * @return
     */
    public static Display getDisplay(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay();
    }

    /**
     * 获取屏幕宽度大小，单位px
     * 
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        Display display = getDisplay(context);
        int screenWidth = display.getWidth();
        return screenWidth;
    }

    /**
     * 获取屏幕高度大小，单位px
     * 
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        Display display = getDisplay(context);
        int screenHeight = display.getHeight();
        return screenHeight;
    }

    /**
     * dip转换为px
     * 
     * @param context
     * @param dip
     * @return
     */
    public static int dipToPx(Context context, float dip) {
        return (int) (dip * getDisplayMetrics(context).density + 0.5f);
    }

    /**
     * px转换为dip
     * 
     * @param context
     * @param px
     * @return
     */
    public static int pxToDip(Context context, int px) {
        return (int) (px / getDisplayMetrics(context).density + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        return (int) (pxValue / getDisplayMetrics(context).scaledDensity + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        return (int) (spValue * getDisplayMetrics(context).scaledDensity + 0.5f);
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        Display display = getDisplay(context);
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        return dm;
    }

    /**
     * 获取屏幕密度
     * 
     * @param context
     * @return
     */
    public static int getDensityDpi(Context context) {
        DisplayMetrics dm = getDisplayMetrics(context);
        return dm.densityDpi;
    }

    /**
     * 获得状态栏高度
     * 
     * @param context
     * @return
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 获取textview文案的宽度
     * 
     * @param tv
     * @return 宽度 - px单位
     */
    public static int getTextStringWidth(TextView tv) {
        Rect bounds = new Rect();
        String text = tv.getText().toString();
        TextPaint paint;

        paint = tv.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    /**
     * 获取textview文案的宽度
     * 
     * @param tv
     * @return 高度 - px单位
     */
    public static int getTextStringHeight(TextView tv) {
        Rect bounds = new Rect();
        String text = tv.getText().toString();
        TextPaint paint;

        paint = tv.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    /*** 判断是否开启了自动亮度调节 */
    public static boolean isAutoBrightness(ContentResolver aContentResolver) {
        boolean automicBrightness = false;
        try {
            automicBrightness = Settings.System.getInt(aContentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        return automicBrightness;
    }

    /**
     * 获取屏幕亮度
     * 
     * @param contentResolver
     * @return
     */
    public static int getBrightness(Context context) {
        ContentResolver cr = context.getContentResolver();
        int brightness = 255;
        try {
            brightness = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException ex) {
            Log.d(TAG, ex.toString());
        }
        return brightness;
    }

    public static void updateBrightness(int brightness, Context context) {
        if (brightness < 0) {
            return;
        }

        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
        WindowManager.LayoutParams params = ((Activity) context).getWindow().getAttributes();

        float brightNess = brightness / 255f;
        Log.d(TAG, "brightNess : " + brightNess);

        params.screenBrightness = brightNess;
        ((Activity) context).getWindow().setAttributes(params);
    }
}
