package com.cpigeon.app;

import android.app.Application;
import android.content.Context;

import com.cpigeon.app.broadcastreceiver.NetStateReceiver;
import com.cpigeon.app.utils.CpigeonData;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import org.xutils.x;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/5.
 */

public class MyApp extends Application {
    private static MyApp instance;
    private static String TAG = "AndySong";
    public static MyApp getInstance(){
        return instance;
    }
    public static CpigeonData mCpigeonData;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        instance = this;
        //开启网络广播监听
        NetStateReceiver.registerNetworkStateReceiver(this);
        if (!BuildConfig.DEBUG)
        {
            CrashReport.initCrashReport(getApplicationContext(), "f7c8c8f49a", BuildConfig.DEBUG);
        }else {
            Logger.init(TAG);
        }
        mCpigeonData = CpigeonData.getInstance();
    }

    /**
     * 低内存自动杀死
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        NetStateReceiver.unRegisterNetworkStateReceiver(this);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}