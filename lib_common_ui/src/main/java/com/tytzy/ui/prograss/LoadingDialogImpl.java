package com.tytzy.ui.prograss;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.tytzy.base.ui.prograss.LoadingCancelListener;
import com.tytzy.base.ui.prograss.LoadingDialog;
import com.tytzy.ui.R;


/**
 * @Author: Baibo
 * @Date: 2020/5/15 3:54 PM
 * @param
 * @Description:基础进度条弹窗
 */
@Route(path = "/ui/LoadingDialog")
public class LoadingDialogImpl implements LoadingDialog {

    /**
     * @Author: Baibo
     * @Date: 2020/5/15 3:54 PM
     * @param context
     * @param msg
     * @param baseLoadingCancelListener
     * @Description:
     */
    @Override
    public Dialog createLoadingDialog(Context context, String msg, final LoadingCancelListener loadingCancelListener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.id_tv_msg);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                loadingCancelListener.onCancelProgress();
            }
        });
        loadingDialog.setCanceledOnTouchOutside(true); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();

        return loadingDialog;
    }

    /**
     * @Author: Baibo
     * @Date: 2020/5/15 3:56 PM
     * @param mDialogUtils
     * @Description:关闭
     */
    @Override
    public void closeDialog(Dialog mDialogUtils) {
        if (mDialogUtils != null && mDialogUtils.isShowing()) {
            mDialogUtils.dismiss();
        }
    }

    @Override
    public void init(Context context) {

    }




}