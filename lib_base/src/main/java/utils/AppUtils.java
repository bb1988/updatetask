package utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.io.File;

import androidx.annotation.NonNull;

/**
 * 作者: 白勃
 * 时间: 2020/5/20 6:00 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: App工具类，全局维护一个ApplicationContext
 * @param
 */
public final class AppUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@NonNull final Context context) {
        AppUtils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("should be initialized in application");
    }

    /**
     * 作者: 白勃
     * 时间: 2020/5/20 5:57 PM
     * 版权: Copyright © 2020 BB Inc. All Rights Reserved
     * 描述: 应用安装
     *
     * @param apkFile
     * @param mContext
     */
    public static void appInstall(File apkFile, Context mContext) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // todo 回头实现 先注销
//            Uri photoURI = FileProvider.getUriForFile(mContext, Constant.FileOS.FILE_AUTHORITY, apkFile);
//            intent.setDataAndType(photoURI, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(intent);
    }
}