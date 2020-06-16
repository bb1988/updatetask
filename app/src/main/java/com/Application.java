package com;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.tytzy.base.BaseComponent;
import com.tytzy.base.toast.ToastUtils;
import com.tytzy.base.toast.interceptor.ToastInterceptor;
import com.tytzy.base.toast.style.ToastBlackStyle;
import com.tytzy.network.NetWork;

public class Application extends android.app.Application {

    private static Application mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        //ARouter初始化
        ARouter.init(this);

        //base组件初始化
        BaseComponent.init(this);
        //Net组件初始化
        NetWork.init(this)
                .baseUrl("http://192.168.10.248:8116")
                .connectTimeout(10)
                .withLog(true)
                .go();

    }

    public static Application getInstance() {
        return mApplication;
    }
}
