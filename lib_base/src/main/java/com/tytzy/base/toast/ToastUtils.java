package com.tytzy.base.toast;

import android.app.AppOpsManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.tytzy.base.toast.interceptor.IToastInterceptor;
import com.tytzy.base.toast.interceptor.ToastInterceptor;
import com.tytzy.base.toast.strategy.IToastStrategy;
import com.tytzy.base.toast.strategy.ToastStrategy;
import com.tytzy.base.toast.style.IToastStyle;
import com.tytzy.base.toast.style.ToastBlackStyle;
import com.tytzy.base.toast.style.ToastQQStyle;
import com.tytzy.base.toast.style.ToastWhiteStyle;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ToastUtils {
    /**
     * 作者: 白勃
     * 时间: 2020/6/16 10:15 AM
     * 版权: Copyright © 2020 BB Inc. All Rights Reserved
     * 描述: Toast 工具类
     */

    private static IToastInterceptor sInterceptor;

    private static IToastStrategy sStrategy;

    private static Toast sToast;

    /**
     * 不允许外部实例化
     */
    private ToastUtils() {}

    /**
     * 初始化 ToastUtils，在 Application 中初始化
     *
     * @param application       应用的上下文
     */
    public static void init(Application application) {
        init(application, new ToastBlackStyle(application));
    }

    /**
     * 初始化 ToastUtils 及样式
     */
    public static void init(Application application, IToastStyle style) {
        checkNullPointer(application);
        // 初始化 Toast 拦截器
        if (sInterceptor == null) {
            setToastInterceptor(new ToastInterceptor());
        }

        // 初始化 Toast 显示处理器
        if (sStrategy == null) {
            setToastStrategy(new ToastStrategy());
        }

        // 初始化吐司
        if (areNotificationsEnabled(application)) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                // 解决 Android 7.1 上主线程被阻塞后吐司会报错的问题
                setToast(new SafeToast(application));
            } else {
                setToast(new BaseToast(application));
            }
        } else {
            // 解决关闭通知栏权限后 Toast 不显示的问题
            setToast(new SupportToast(application));
        }

        // 设置 Toast 视图
        setView(createTextView(application, style));

        // 设置 Toast 重心
        setGravity(style.getGravity(), style.getXOffset(), style.getYOffset());
    }

    /**
     * 显示一个对象的吐司
     *
     * @param object      对象
     */
    public static void show(Object object) {
        show(object != null ? object.toString() : "null");
    }

    /**
     * 显示一个吐司
     *
     * @param id      如果传入的是正确的 string id 就显示对应字符串
     *                如果不是则显示一个整数的string
     */
    public static void show(int id) {
        checkToastState();

        try {
            // 如果这是一个资源 id
            show(getContext().getResources().getText(id));
        } catch (Resources.NotFoundException ignored) {
            // 如果这是一个 int 整数
            show(String.valueOf(id));
        }
    }

    /**
     * 显示一个吐司
     *
     * @param id            资源 id
     * @param args          参数集
     */
    public static void show(int id, Object... args) {
        show(getContext().getResources().getString(id), args);
    }

    /**
     * 显示一个吐司
     *
     * @param format        原字符串
     * @param args          参数集
     */
    public static void show(String format, Object... args) {
        show(String.format(format, args));
    }

    /**
     * 显示一个吐司
     *
     * @param text      需要显示的文本
     */
    public static synchronized void show(CharSequence text) {
        checkToastState();

        if (sInterceptor.intercept(sToast, text)) {
            return;
        }

        sStrategy.show(text);
    }

    /**
     * 取消吐司的显示
     */
    public static synchronized void cancel() {
        checkToastState();

        sStrategy.cancel();
    }

    /**
     * 设置吐司的位置
     *
     * @param gravity           重心
     * @param xOffset           x轴偏移
     * @param yOffset           y轴偏移
     */
    public static void setGravity(int gravity, int xOffset, int yOffset) {
        checkToastState();

        // 适配 Android 4.2 新特性，布局反方向（开发者选项 - 强制使用从右到左的布局方向）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            gravity = Gravity.getAbsoluteGravity(gravity, sToast.getView().getResources().getConfiguration().getLayoutDirection());
        }

        sToast.setGravity(gravity, xOffset, yOffset);
    }

    /**
     * 给当前Toast设置新的布局，具体实现可看{BaseToast#setView(View)}
     */
    public static void setView(int id) {
        checkToastState();

        setView(View.inflate(getContext(), id, null));
    }
    public static void setView(View view) {
        checkToastState();

         // 这个 View 不能为空
        checkNullPointer(view);

        // 当前必须用 Application 的上下文创建的 View，否则可能会导致内存泄露
        Context context = view.getContext();
        if (!(context instanceof Application)) {
            throw new IllegalArgumentException("The view must be initialized using the context of the application");
        }

        // 如果吐司已经创建，就重新初始化吐司
        if (sToast != null) {
            // 取消原有吐司的显示
            sToast.cancel();
            sToast.setView(view);
        }
    }

    /**
     * 获取当前 Toast 的视图
     */
    @SuppressWarnings("unchecked")
    public static <V extends View> V getView() {
        checkToastState();

        return (V) sToast.getView();
    }

    /**
     * 初始化全局的Toast样式
     *
     * @param style         样式实现类，框架已经实现三种不同的样式
     *                      黑色样式：{@link ToastBlackStyle}
     *                      白色样式：{@link ToastWhiteStyle}
     *                      仿QQ样式：{@link ToastQQStyle}
     */
    public static void initStyle(IToastStyle style) {
        checkNullPointer(style);
        // 如果吐司已经创建，就重新初始化吐司
        if (sToast != null) {
            // 取消原有吐司的显示
            sToast.cancel();
            sToast.setView(createTextView(getContext(), style));
            sToast.setGravity(style.getGravity(), style.getXOffset(), style.getYOffset());
        }
    }

    /**
     * 设置当前Toast对象
     */
    public static void setToast(Toast toast) {
        checkNullPointer(toast);
        if (sToast != null && toast.getView() == null) {
            // 移花接木
            toast.setView(sToast.getView());
            toast.setGravity(sToast.getGravity(), sToast.getXOffset(), sToast.getYOffset());
            toast.setMargin(sToast.getHorizontalMargin(), sToast.getVerticalMargin());
        }
        sToast = toast;
        if (sStrategy != null) {
            sStrategy.bind(sToast);
        }
    }

    /**
     * 设置 Toast 显示策略
     */
    public static void setToastStrategy(IToastStrategy handler) {
        checkNullPointer(handler);
        sStrategy = handler;
        if (sToast != null) {
            sStrategy.bind(sToast);
        }
    }

    /**
     * 设置 Toast 拦截器（可以根据显示的内容决定是否拦截这个Toast）
     * 场景：打印 Toast 内容日志、根据 Toast 内容是否包含敏感字来动态切换其他方式显示（这里可以使用我的另外一套框架 XToast）
     */
    public static void setToastInterceptor(IToastInterceptor interceptor) {
        checkNullPointer(interceptor);
        sInterceptor = interceptor;
    }

    /**
     * 获取当前Toast对象
     */
    public static Toast getToast() {
        return sToast;
    }

    /**
     * 检查吐司状态，如果未初始化请先调用{@link ToastUtils#init(Application)}
     */
    private static void checkToastState() {
        // 吐司工具类还没有被初始化，必须要先调用init方法进行初始化
        if (sToast == null) {
            throw new IllegalStateException("ToastUtils has not been initialized");
        }
    }

    /**
     * 检查对象是否为空
     */
    private static void checkNullPointer(Object object) {
        if (object == null) {
            throw new NullPointerException("are you ok?");
        }
    }

    /**
     * 生成默认的 TextView 对象
     */
    private static TextView createTextView(Context context, IToastStyle style) {

        GradientDrawable drawable = new GradientDrawable();
        // 设置背景色
        drawable.setColor(style.getBackgroundColor());
        // 设置圆角大小
        drawable.setCornerRadius(style.getCornerRadius());

        TextView textView = new TextView(context);
        textView.setId(android.R.id.message);
        textView.setTextColor(style.getTextColor());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getTextSize());

        // 适配布局反方向
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setPaddingRelative(style.getPaddingStart(), style.getPaddingTop(), style.getPaddingEnd(), style.getPaddingBottom());
        } else {
            textView.setPadding(style.getPaddingStart(), style.getPaddingTop(), style.getPaddingEnd(), style.getPaddingBottom());
        }

        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // setBackground API 版本兼容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setBackground(drawable);
        } else {
            textView.setBackgroundDrawable(drawable);
        }

        // 设置 Z 轴阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.setZ(style.getZ());
        }

        // 设置最大显示行数
        if (style.getMaxLines() > 0) {
            textView.setMaxLines(style.getMaxLines());
        }

        return textView;
    }

    /**
     * 获取上下文对象
     */
    private static Context getContext() {
        checkToastState();
        return sToast.getView().getContext();
    }

    /**
     * 检查通知栏权限有没有开启
     * 参考 SupportCompat 包中的方法： NotificationManagerCompat.from(context).areNotificationsEnabled();
     */
    private static boolean areNotificationsEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            return manager != null && manager.areNotificationsEnabled();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String packageName = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            try {
                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return ((int) checkOpNoThrowMethod.invoke(appOps, value, uid, packageName) == AppOpsManager.MODE_ALLOWED);
            } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException
                    | InvocationTargetException | IllegalAccessException | RuntimeException ignored) {
                return true;
            }
        } else {
            return true;
        }
    }
}