package com.bb.updatetask;

import androidx.appcompat.app.AppCompatActivity;
import autodispose2.AutoDisposeConverter;
import okhttp3.ResponseBody;

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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.tytzy.network.DownLoadManager;
import com.tytzy.network.NetWork;
import com.tytzy.network.observer.DownLoadObserver;
import com.tytzy.network.response.BaseResponse;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView textView;

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
                .baseUrl("")
                .withCache(true)
                .connectTimeout(10)
                .headers("key1","value1")
                .headers("key2","value2")
                .withLog(true)
                .go();


//        DownLoadManager.init(this).downLoad("", new DownLoadObserver<ResponseBody>() {
//            @Override
//            public void onProgress(int percent, boolean done) {
//
//            }
//        },new File("1111"));

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
        Intent it = new Intent(this, DetecterActivity.class);
        it.putExtra("readpath", Environment.getExternalStorageDirectory() + File.separator + "FaceDB" + File.separator);
        it.putExtra("time", 30 * 1000);//超时时间30s,单位ms
        startActivity(it);


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


}
