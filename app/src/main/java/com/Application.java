package com;

import com.alibaba.android.arouter.launcher.ARouter;

public class Application extends android.app.Application {

    private static Application mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        //ARouter初始化
        ARouter.init(this);
    }

    public static Application getInstance() {
        return mApplication;
    }
}
