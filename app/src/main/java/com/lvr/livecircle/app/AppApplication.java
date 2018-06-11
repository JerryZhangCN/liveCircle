package com.lvr.livecircle.app;

import android.content.Context;

import com.lvr.livecircle.base.BaseApplication;

/**
 * APPLICATION
 */
public class AppApplication extends BaseApplication {

    public static Context mMainContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mMainContext = this;
    }


}
