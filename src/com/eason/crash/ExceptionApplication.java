package com.eason.crash;

/**
 * @author <a href="mailto:pujl1@lenovo.com">Eason Pu</a>
 * @date 1/26/15
 */
public class ExceptionApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CrashHandler crashHandler = new CrashHandler();
        crashHandler.init(this.getApplicationContext());
    }
}
