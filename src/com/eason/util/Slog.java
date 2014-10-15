package com.eason.util;

import android.util.Log;

/**
 * Log Util Class
 */
public final class Slog {

    private static final String TAG = "@LenovoTv";
    private static final boolean isLog = true;

    /**
     * 1, debug level 2, error level
     */
    private static int level = 1;

    private Slog() {
    }

    public static void d(String TAG, String msg) {
        if (level == 1) {
            if (isLog) {
                StackTraceElement[] el = new Exception().getStackTrace();
                String className = el[1].getClassName();

                String str = "Class:" + className.substring(className.lastIndexOf(".") + 1) + "."
                    + el[1].getMethodName() + " [Line:"
                    + new Integer(el[1].getLineNumber()).toString()
                    + "] :--> " + msg;
                Log.d(TAG, str);
            }
        }
    }

    public static void d(String msg) {
        if (level == 1) {
            if (isLog) {
                StackTraceElement[] el = new Exception().getStackTrace();
                String className = el[1].getClassName();

                String str = "Class:" + className.substring(className.lastIndexOf(".") + 1) + "."
                    + el[1].getMethodName() + " [Line:"
                    + new Integer(el[1].getLineNumber()).toString()
                    + "] :--> " + msg;
                Log.d(TAG, str);
            }
        }
    }

    public static void v(String TAG, String msg) {
        if (level == 1) {
            if (isLog) {
                StackTraceElement[] el = new Exception().getStackTrace();
                String className = el[1].getClassName();

                String str = "Class:" + className.substring(className.lastIndexOf(".") + 1) + "."
                    + el[1].getMethodName() + " [Line:"
                    + new Integer(el[1].getLineNumber()).toString()
                    + "] :--> " + msg;
                Log.v(TAG, str);
            }
        }
    }

    public static void v(String msg) {
        if (level == 1) {
            if (isLog) {
                StackTraceElement[] el = new Exception().getStackTrace();
                String className = el[1].getClassName();

                String str = "Class:" + className.substring(className.lastIndexOf(".") + 1) + "."
                    + el[1].getMethodName() + " [Line:"
                    + new Integer(el[1].getLineNumber()).toString()
                    + "] :--> " + msg;
                Log.v(TAG, str);
            }
        }
    }

    public static void i(String TAG, String msg) {
        if (isLog) {
            StackTraceElement[] el = new Exception().getStackTrace();
            String className = el[1].getClassName();

            String str = "Class:" + className.substring(className.lastIndexOf(".") + 1) + "."
                + el[1].getMethodName() + " [Line:"
                + new Integer(el[1].getLineNumber()).toString()
                + "] :--> " + msg;
            Log.i(TAG, str);
        }
    }

    public static void i(String msg) {
        if (isLog) {
            StackTraceElement[] el = new Exception().getStackTrace();
            String className = el[1].getClassName();

            String str = "Class:" + className.substring(className.lastIndexOf(".") + 1) + "."
                + el[1].getMethodName() + " [Line:"
                + new Integer(el[1].getLineNumber()).toString()
                + "] :--> " + msg;
            Log.i(TAG, str);
        }
    }

    public static void w(String TAG, String msg) {
        if (isLog) {
            StackTraceElement[] el = new Exception().getStackTrace();
            String className = el[1].getClassName();

            String str = "Class:" + className.substring(className.lastIndexOf(".") + 1) + "."
                + el[1].getMethodName() + " [Line:"
                + new Integer(el[1].getLineNumber()).toString()
                + "] :--> " + msg;
            Log.w(TAG, str);
        }
    }

    public static void w(String msg) {
        if (isLog) {
            StackTraceElement[] el = new Exception().getStackTrace();
            String className = el[1].getClassName();

            String str = "Class:" + className.substring(className.lastIndexOf(".") + 1) + "."
                + el[1].getMethodName() + " [Line:"
                + new Integer(el[1].getLineNumber()).toString()
                + "] :--> " + msg;
            Log.w(TAG, str);
        }
    }

    public static void e(String TAG, String msg) {
        if (isLog) {
            StackTraceElement[] el = new Exception().getStackTrace();
            String className = el[1].getClassName();

            String str = "Class:" + className.substring(className.lastIndexOf(".") + 1) + "."
                + el[1].getMethodName() + " [Line:"
                + new Integer(el[1].getLineNumber()).toString()
                + "] :--> " + msg;
            Log.e(TAG, str);
        }
    }

    public static void e(String msg) {
        if (isLog) {
            StackTraceElement[] el = new Exception().getStackTrace();
            String className = el[1].getClassName();

            String str = "Class:" + className.substring(className.lastIndexOf(".") + 1) + "."
                + el[1].getMethodName() + " [Line:"
                + new Integer(el[1].getLineNumber()).toString()
                + "] :--> " + msg;
            Log.e(TAG, str);
        }
    }

    public static void e(String TAG, String msg, Throwable tr) {
        if (isLog) {
            StackTraceElement[] el = new Exception().getStackTrace();
            String className = el[1].getClassName();

            String str = "Class:" + className.substring(className.lastIndexOf(".") + 1) + "."
                + el[1].getMethodName() + " [Line:"
                + new Integer(el[1].getLineNumber()).toString()
                + "] :--> " + msg + "[Throwable]: " + tr;
            Log.e(TAG, str);
        }
    }

    public static void log(String msg) {
        if (isLog) {
            StackTraceElement[] el = new Exception().getStackTrace();
            String className = el[1].getClassName();

            String str = "Class:" + className.substring(className.lastIndexOf(".") + 1) + "."
                + el[1].getMethodName() + " [Line:"
                + new Integer(el[1].getLineNumber()).toString()
                + "] :--> " + msg;
            Log.i(TAG, str);
        }
    }
}
