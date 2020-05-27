package com.bb.updatetask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.tytzy.base.ui.prograss.LoadingCancelListener;
import com.tytzy.base.ui.prograss.LoadingDialog;
import com.tytzy.network.DownLoadManager;
import com.tytzy.network.NetWork;
import com.tytzy.network.observer.DownLoadObserver;
import com.tytzy.network.observer.LoadingObserver;
import com.tytzy.network.response.BaseResponse;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoadingCancelListener {
    private Button button;
    private TextView textView;
    private File file;

    @Autowired(name = "/ui/loading_dialog")
    protected LoadingDialog loadingDialog;

    private final String[] PERMISSIONS = new String[]{
//            //读写存储卡
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            //麦克风,照相机
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            //位置
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            //手机状态
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS
    };

    private final String[] PERMISSIONS2 = new String[]{
            //读写存储卡
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            //麦克风,照相机
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            //位置
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.bottom);
        textView = findViewById(R.id.textview);
        requestPermission();
        NetWork.init(this)
                .baseUrl("http://192.168.10.248:8116")
                .connectTimeout(10)
                .withLog(true)
                .go();

        File dir = new File(getExternalFilesDir(null).getAbsolutePath());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        file = new File(dir, "test.apk");


        ARouter.getInstance().inject(this);



//        NetWork.create(Appservice).dosomeThing();

    }

    //请求权限
    private void requestPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q?PERMISSIONS2:PERMISSIONS)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
                        try {
                            initFaceDB(Environment.getExternalStorageDirectory() + File.separator + "FaceDB" + File.separator);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
//                        for (String person : data) {
//                            Toast.makeText(MainActivity.this, person, Toast.LENGTH_SHORT).show();
//                        }
//                        requestPermission();
//                        Toast.makeText(MainActivity.this, "现场安全管控已被禁止权限:相机和存储。可在系统设置——权限管理开启授权", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

    public void clickAlert(View view){
//        goToUpdateApk(MainActivity.this,"cn.com.codeteenager.accessibilityapp");
//        Intent it = new Intent(this, DetecterActivity.class);
//        it.putExtra("readpath", Environment.getExternalStorageDirectory() + File.separator + "FaceDB" + File.separator);
//        it.putExtra("time", 30 * 1000);//超时时间30s,单位ms
//        startActivity(it);


        //下载方法测试
//        DownLoadManager.init(this).downLoad("/appPath/04ede9fe-6ff3-4713-972e-65b06b0fce71.apk", new DownLoadObserver<ResponseBody>() {
//            @Override
//            public void onProgress(int percent, boolean done) {
//                Log.e("onProgress","进度======================="+percent+"%,是否完成："+done);
//            }
//        },file);

        JSONObject obj = new JSONObject();
        try {
            obj.put("username", "张甜");
            obj.put("password", "123456");
            obj.put("imei", "357092061466849");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //普通连接测试
        RequestBody requestBody = RequestBody.create(obj.toString(),
                MediaType.parse("Content-Type, application/json"));

        login(new LoadingObserver<BaseResponse<UserInfoEntity>, UserInfoEntity>(this,true) {
            @Override
            public void onResult(UserInfoEntity t) {
                Log.e("UserInfoEntity",t.getAvatar());
                Log.e("UserInfoEntity",t.getInfoMap().getCity());
            }
        }, requestBody);


//        loadingDialog.createLoadingDialog(this,"",this::onCancelProgress).show();

    }

    public void login(LoadingObserver<BaseResponse<UserInfoEntity>,UserInfoEntity> loadingObserver,RequestBody requestBody) {
        NetWork.create(LoginService.class,"http://192.168.10.248:8116").login(requestBody)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from( this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(loadingObserver);
    }



    public boolean initFaceDB(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        InitApplication inAp = new InitApplication();

        writeToface wtf = new writeToface();
        wtf.addFaceTxt(path);
        FaceDB faceDB = new FaceDB(path);
        inAp.setmFaceDB(faceDB);
        return inAp.getmFaceDB().loadFaces();
    }

    /**
     * 检查是否安装了某应用
     *
     * @param packageName 包名
     * @return
     */
    public static boolean isAvilible(String packageName, Context mContext) {

        final PackageManager packageManager = mContext.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 检测
     *
     */
    public  void goToUpdateApk(Context ctx, String packageName) {

        if ( !isAvilible( packageName , ctx ) ){
            //没有安装Apk
            Toast.makeText(MainActivity.this, "没有安装应用程序", Toast.LENGTH_SHORT).show();
        }else {
            int nextVersion = getVersionCode(this)+1;
            if(nextVersion<5) {

                //已经安装了Apk
                Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
                Bundle bundle = new Bundle();
                bundle.putString("args", "我就是跳转过来的");
                bundle.putString("args1", nextVersion + "");
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                exitAPP();
            }else{
                Toast.makeText(MainActivity.this, "已是最新版本应用", Toast.LENGTH_SHORT).show();
            }
        }


    }

    /**
     * 获取版本号
     *
     * @param context 上下文
     *
     * @return 版本号
     */
    public static int getVersionCode(Context context) {

        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void exitAPP() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
        for (ActivityManager.AppTask appTask : appTaskList) {
            appTask.finishAndRemoveTask();
        }
    }

    @Override
    public void onCancelProgress() {

    }

    private interface LoginService {

        @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
        @POST("/sso/appLogin")
        Observable<BaseResponse<UserInfoEntity>> login(@Body RequestBody body);
    }


}
