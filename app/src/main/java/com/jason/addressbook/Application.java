package com.jason.addressbook;

import com.jason.addressbook.tools.NetUtil;

/**
 * Created by Jason on 2016/3/26.
 */
public class Application extends android.app.Application {
    private static Application mApplication;
    public static int mNetWorkState ;

    public static synchronized Application getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        initData();
    }



    public void initData() {
        mNetWorkState = NetUtil.getNetworkState(this);
    }
}
