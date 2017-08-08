package com.eason.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Eason on 2015/4/20.
 */
public class SystemInfo {
    private Context mContext;

    public static String VERSION_NAME;
    public static int VERSION_CODE;
    public static String SN;
    public static String DeviceModel;
    public static String SystemVersion;
    public static String IP;

    public SystemInfo(Context mContext) {
        this.mContext = mContext;
    }

    public void initVersionInfo() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = mContext.getPackageManager().
                getPackageInfo(mContext.getPackageName(), 0);
            VERSION_NAME = packageInfo.versionName;
            VERSION_CODE = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readSystemInfo() {
        SN = Build.SERIAL;
        DeviceModel = Build.MODEL != null ? Build.MODEL : "";
        SystemVersion = Build.VERSION.RELEASE != null ? Build.VERSION.RELEASE : "";
    }

    public static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:SS");
        return simpleDateFormat.format(new Date());
    }

    public void init() {
        initVersionInfo();
        readSystemInfo();
        IP = NetworkUtils.getLocalIpAddress(mContext);
    }
}
