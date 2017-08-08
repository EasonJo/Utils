package com.eason.util.pic;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * DeviceUtils
 * <ul>
 * <li>{@link #getBuildModel()} 获取设备Model，可以用来判断设备型号</li>
 * <li>{@link #getProcessorNumber()} 获取CPU的核数</li>
 * <li> {@link #getVersionRelease()}获取系统型号,以android_OS_开头，eg：android_OS_4.1</li>
 * <li> {@link #getSdkInt()}获取SDK版本号,14代表Android4.0</li>
 * <li>{@link #getImei(Context)}获取设备imei号</li>
 * <li>{@link #getMacAddress(Context)}获取mac地址</li>
 * <li> {@link #getManufacturer()}获取厂商信息</li>
 * </ul>
 * <ul>
 * <strong>Attentions:</strong>
 * <li>You should add <strong>android.permission.ACCESS_WIFI_STATE</strong> in
 * manifest</li>
 * <li>You should add <strong>android.permission.READ_PHONE_STATE</strong> in
 * manifest</li>
 * </ul>
 *  
 */
public class DeviceUtils {

    private static String mImei, mMacAddress;

    /**
     * 获取设备Model，可以用来判断设备型号
     * 
     * @return
     */
    public static String getBuildModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取CPU的核数
     * 
     * @return
     */
    public static int getProcessorNumber() {
        Runtime rt = Runtime.getRuntime();
        return rt.availableProcessors();
    }

    /**
     * 获取系统型号，eg：android_OS_4.1
     * 
     * @return
     */
    public static String getVersionRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取SDK版本号
     * 
     * @return
     */
    public static int getSdkInt() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取imei号，如果可以获取到，直接返回，如果无法获取，或是获取到的不是imei，则返回null
     * <ul>
     * <strong>Attentions:</strong>
     * <li>You should add <strong>android.permission.READ_PHONE_STATE</strong>
     * in manifest</li>
     * </ul>
     * 
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        if (!TextUtils.isEmpty(mImei)) {
            return mImei;
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mImei = tm.getDeviceId();

        // 如果不是imei号，则返回空
        if (!isImei(mImei)) {
            mImei = null;
            return null;
        }
        return mImei;
    }

    /**
     * 判断是否是imei号
     * 
     * @param imei
     * @return
     */
    private static boolean isImei(String imei) {
        if (TextUtils.isEmpty(imei)) {
            return false;
        }

        if (imei.length() < 15) {
            return false;
        }

        // 检查是否都是数字
        for (int i = 0; i < imei.length(); i++) {
            if (!Character.isDigit(imei.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取mac地址
     * <ul>
     * <strong>Attentions:</strong>
     * <li>You should add <strong>android.permission.ACCESS_WIFI_STATE</strong>
     * in manifest</li>
     * </ul>
     * 
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        if (!TextUtils.isEmpty(mMacAddress)) {
            return mMacAddress;
        }
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        mMacAddress = info.getMacAddress();
        return mMacAddress;
    }

    /**
     * 获取手机制造商信息
     * 
     * @return
     */
    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

}
