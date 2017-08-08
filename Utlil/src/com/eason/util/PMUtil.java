package com.eason.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.List;

/**
 * To Set Default Launcher, need system permission
 *
 * @author <a href="mailto:pujl1@lenovo.com">Eason Pu</a>
 * @date 12/30/14
 */
public class PMUtil {
    public final static String KIDSMODE = "com.lenovo.nova.childrencontrol";
    public final static String LAUNCHER = "com.lenovo.dll.nebula.launcher";

    public static void setDefaultLauncher(Context mContext, String which) {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_MAIN);
            filter.addCategory(Intent.CATEGORY_HOME);
            filter.addCategory(Intent.CATEGORY_DEFAULT);

            Intent home_intent = new Intent(Intent.ACTION_MAIN);
            home_intent.addCategory(Intent.CATEGORY_HOME);

            int bestMatch = 0;
            PackageManager pm = mContext.getPackageManager();
            List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(home_intent, 0);
            ComponentName[] set = new ComponentName[resolveInfoList.size()];
            ComponentName target = null;
            for (int i = 0; i < resolveInfoList.size(); i++) {
                ResolveInfo r = resolveInfoList.get(i);
                set[i] = new ComponentName(r.activityInfo.packageName, r.activityInfo.name);
                if (r.match > bestMatch) bestMatch = r.match;
                pm.clearPackagePreferredActivities(set[i].getPackageName());
                if (which.equals(r.activityInfo.packageName) || which.equals(r.activityInfo.packageName)) {
                    target = set[i];
                    Log.i("Eason", "Set Default Launcher: " + target.toString());
                }
            }
            if (target != null) {
                pm.addPreferredActivity(filter, bestMatch, set, target);
            }
        } catch (SecurityException e) {
            Intent intent = new Intent("com.lenovo.nova.appservice.CMD_RECEIVER");
            intent.putExtra("cmdName", "setDefaultPreferredActivity");
            intent.putExtra("packageName", which);
            mContext.sendBroadcast(intent);
        }
    }
}
