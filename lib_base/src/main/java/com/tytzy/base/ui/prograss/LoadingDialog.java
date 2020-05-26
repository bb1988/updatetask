package com.tytzy.base.ui.prograss;

import android.app.Dialog;
import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;
/**
 * @Author: Baibo
 * @Date: 2020/5/15 4:18 PM
 * @param
 * @Description:LoadingDialog对外接口
 */
public interface LoadingDialog extends IProvider {

    /**
     * @Author: Baibo
     * @Date: 2020/5/15 4:19 PM
     * @param context
     * @param msg
     * @param baseLoadingCancelListener
     * @Description:
     */
     Dialog createLoadingDialog(Context context, String msg, final LoadingCancelListener loadingCancelListener);

    /**
     * @Author: Baibo
     * @Date: 2020/5/15 4:20 PM
     * @param mDialogUtils
     * @Description:关闭
     */
     void closeDialog(Dialog mDialogUtils);
}
