package com.eason.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.Field;
import java.util.Locale;

public class NetworkUtils {

    public static final int NETWORK_TYPE_UNKNOWN = -1;
    public static final int NETWORK_TYPE_UNAVAILABLE = 0;
    public static final int NETWORK_TYPE_WIFI = 1;
    public static final int NETWORK_MOBILE = 2;
    public static final int NETWORK_TYPE_2G = 3;
    public static final int NETWORK_TYPE_3G = 4;
    public static final int NETWORK_TYPE_4G = 5;

    public static final String STRING_G3 = "3G";
    public static final String STRING_G2 = "2G";
    public static final String STRING_G4 = "4G";
    public static final String STRING_UNKNOWN = "Unknown";
    public static final String String_UNAVAILABLE = "unavailable";

    public static final String STRING_WIFI = "WiFi";

    private static int networkType = NETWORK_TYPE_UNKNOWN;

    public static final void initNetworkType(Context context) {
        ConnectivityManager mConnectivity = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if ((info != null) && info.isAvailable()) {
            if (info.getTypeName().toLowerCase(Locale.ENGLISH).equals("wifi")) {
                networkType = NETWORK_TYPE_WIFI;
            } else if (info.getTypeName().toLowerCase(Locale.ENGLISH).equals("mobile")) {
                networkType = getMobileType(context);
            }
        } else {
            networkType = NETWORK_TYPE_UNAVAILABLE;
        }
    }

    public static final int getNetworkType(Context context) {
        if (networkType == NETWORK_TYPE_UNKNOWN) {
            initNetworkType(context);
        }
        return networkType;
    }

    private static final int getMobileType(Context context) {
        int type = NETWORK_MOBILE;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                type = NETWORK_TYPE_2G; // ~ 100 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                type = NETWORK_TYPE_2G; // ~ 14-64 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                type = NETWORK_TYPE_2G; // ~ 50-100 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                type = NETWORK_TYPE_3G; // ~ 50-100 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                type = NETWORK_TYPE_3G; // ~ 400-1000 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                type = NETWORK_TYPE_3G; // ~ 600-1400 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                type = NETWORK_TYPE_3G; // ~ 2-14 Mbps
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                type = NETWORK_TYPE_3G; // ~ 700-1700 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                type = NETWORK_TYPE_3G; // ~ 1-23 Mbps
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                type = NETWORK_TYPE_3G; // ~ 400-7000 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                type = NETWORK_MOBILE;
                break;
            default:
                type = NETWORK_MOBILE;
                break;
        }
        int sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        try {
            Class<?> tempClass = Class.forName("android.telephony.TelephonyManager");
            if (sdkVersion >= 11) {

                Field fieldLTE = tempClass.getField("NETWORK_TYPE_LTE");
                if (fieldLTE != null && fieldLTE.get(null) != null
                    && fieldLTE.get(null).toString().equals(String.valueOf(telephonyManager.getNetworkType()))) {
                    type = NETWORK_TYPE_4G;
                }
                Field fieldEHRPD = tempClass.getField("NETWORK_TYPE_EHRPD");
                if (fieldEHRPD != null && fieldEHRPD.get(null) != null
                    && fieldEHRPD.get(null).toString().equals(String.valueOf(telephonyManager.getNetworkType()))) {
                    type = NETWORK_TYPE_3G;
                }

            }
            if (sdkVersion >= 9) {
                Field fieldEVDOB = tempClass.getField("NETWORK_TYPE_EVDO_B");
                if (fieldEVDOB != null && fieldEVDOB.get(null) != null
                    && fieldEVDOB.get(null).toString().equals(String.valueOf(telephonyManager.getNetworkType()))) {
                    type = NETWORK_TYPE_3G;
                }
            }
            if (sdkVersion >= 13) {
                Field fieldHSPAP = tempClass.getField("NETWORK_TYPE_HSPAP");
                if (fieldHSPAP != null && fieldHSPAP.get(null) != null
                    && fieldHSPAP.get(null).toString().equals(String.valueOf(telephonyManager.getNetworkType()))) {
                    type = NETWORK_TYPE_3G;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return type;
    }

    public static String getNetworkStringByType(int networkType) {
        String networkString = STRING_UNKNOWN;
        switch (networkType) {
            case NETWORK_TYPE_UNAVAILABLE:
                networkString = String_UNAVAILABLE;
                break;
            case NETWORK_TYPE_WIFI:
                networkString = STRING_WIFI;
                break;
            case NETWORK_TYPE_2G:
                networkString = STRING_G2;
                break;
            case NETWORK_TYPE_3G:
                networkString = STRING_G3;
                break;
            case NETWORK_TYPE_4G:
                networkString = STRING_G4;
                break;
            default:
                networkString = STRING_UNKNOWN;
                break;
        }
        return networkString;
    }

    public static final boolean is2G(Context context) {
        boolean ret = false;
        if (isMobileConnected(context)) {
            ret = getMobileType(context) == NETWORK_TYPE_2G;
        }
        return ret;
    }

    public static final boolean is3G(Context context) {
        boolean ret = false;
        if (isMobileConnected(context)) {
            ret = getMobileType(context) == NETWORK_TYPE_3G;
        }
        return ret;
    }

    public static final boolean is4G(Context context) {
        boolean ret = false;
        if (isMobileConnected(context)) {
            ret = getMobileType(context) == NETWORK_TYPE_4G;
        }
        return ret;
    }

    public static final boolean isMobile(int networkType) {
        return (networkType == NETWORK_TYPE_2G || networkType == NETWORK_TYPE_3G || networkType == NETWORK_TYPE_4G || networkType == NETWORK_TYPE_UNKNOWN);
    }

    public static final boolean isWifi(int networkType) {
        return networkType == NETWORK_TYPE_WIFI;
    }

    public static final boolean isMobile(Context context) {
        return isMobile(getNetworkType(context));
    }

    public static final boolean isWifi(Context context) {
        return isWifi(getNetworkType(context));
    }

    public static final boolean isWifiConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean ret = (networkInfo != null) && networkInfo.isConnected();
        return ret;
    }

    public static final boolean isMobileConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean ret = (networkInfo != null) && networkInfo.isConnected();
        return ret;
    }

    public static final boolean isUnavailable(int networkType) {
        return networkType == NETWORK_TYPE_UNAVAILABLE;
    }


    public static final boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = null;
        if (wifi != null) {
            info = wifi.getConnectionInfo();
        }
        String macString = "";
        if (info != null) {
            macString = info.getMacAddress();
        }
        return macString;
    }

    /**
     * get Current IP Address
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        try {
            if (!isWifi(context)) {
                return "";
            }
            WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            return "";
        }
        // return null;
    }

    /**
     * Transfer IP int to IP String
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

}
