package com.eason.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:pujl1@lenovo.com">Eason Pu</a>
 * @date 1/26/15
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler crashHandler = new CrashHandler();
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private HashMap<String, String> infos = new HashMap<String, String>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");


    public static CrashHandler getInstance() {
        return crashHandler;
    }

    public void init(Context mContext) {
        this.mContext = mContext;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //set Default UnCaughtExceptionHandler.
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

    }

    private boolean handleExcetpion(Throwable ex) {
        if (ex == null) {
            return false;
        }

        collectDeviceInfo(mContext);

        return true;
    }

    /**
     * collect all device infomation
     *
     * @param ctx
     */
    private void collectDeviceInfo(Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * save crash log to file
     *
     * @param ex
     * @return fileName, it maybe is null.
     */
    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();

        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        printWriter.close();
        String result = writer.toString();

        sb.append(result);

        //save log to file
        try {
            long timeStamp = System.currentTimeMillis();
            String time = dateFormat.format(new Date());
            String fileName = "Crash-" + time + "-" + timeStamp + ".log";
            if (Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)) {
                String path = "/sdcard/crash";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                FileOutputStream fo = new FileOutputStream(path + fileName);
                fo.write(sb.toString().getBytes());
                fo.close();
                return fileName;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
