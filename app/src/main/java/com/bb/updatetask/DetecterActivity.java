package com.bb.updatetask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKMatching;
import com.arcsoft.facerecognition.AFR_FSDKVersion;
import com.arcsoft.facetracking.AFT_FSDKEngine;
import com.arcsoft.facetracking.AFT_FSDKFace;
import com.arcsoft.facetracking.AFT_FSDKVersion;
import com.guo.android_extend.java.AbsLoop;
import com.guo.android_extend.java.ExtByteArrayOutputStream;
import com.guo.android_extend.tools.CameraHelper;
import com.guo.android_extend.widget.CameraFrameData;
import com.guo.android_extend.widget.CameraGLSurfaceView;
import com.guo.android_extend.widget.CameraSurfaceView;
import com.guo.android_extend.widget.CameraSurfaceView.OnCameraListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetecterActivity extends Activity implements AutoFocusCallback, OnClickListener, OnTouchListener, OnCameraListener {
    static InitApplication initApplication = new InitApplication();
    static String k = null;
    static String l = "相机开启失败";
    static String m = "未检测到人脸";
    public static int waitTime;
    private TextView textView1;
    private ImageView imageView;
    private SharedPreferences sharedPreferences;
    private Editor editor;
    private ImageButton imageButton;
    AFT_FSDKVersion aft_fsdkVersion = new AFT_FSDKVersion();
    AFT_FSDKEngine aft_fsdkEngine = new AFT_FSDKEngine();
    List<AFT_FSDKFace> aftFsdkFaces = new ArrayList();
    int e;
    int f;
    boolean g;
    byte[] bytes = null;
    MyAbsLoop i = null;
    ErrorFile errorFile = null;
    AFT_FSDKFace aft_fsdkFace = null;
    Handler handler;
    boolean p = false;
    private final String simpleName = getClass().getSimpleName();
    private int t;
    private int u;
    private int v;
    private CameraSurfaceView cameraSurfaceView;
    private CameraGLSurfaceView cameraGLSurfaceView;
    private Camera camera;
    private TextView textView;

    class MyAbsLoop extends AbsLoop {
        AFR_FSDKVersion version = new AFR_FSDKVersion();
        AFR_FSDKEngine engine = new AFR_FSDKEngine();
        AFR_FSDKFace face = new AFR_FSDKFace();
        List<FaceDB.FaceDBA> list;
        ErrorFile file;
        String f;
        final DetecterActivity detecterActivity;

        MyAbsLoop(DetecterActivity detecterActivity) {
            this.detecterActivity = detecterActivity;
            DetecterActivity.initApplication.getmFaceDB();
            this.list = FaceDB.getmRegister();
            this.file = null;
            this.f = null;
        }

        public void loop() {
//            Log.e("有结果","识别结果============："+detecterActivity.bytes);
            if (this.detecterActivity.bytes != null) {
                String str;
                int i = this.detecterActivity.f;
                long currentTimeMillis = System.currentTimeMillis();
                AFR_FSDKError AFR_FSDK_ExtractFRFeature = this.engine.AFR_FSDK_ExtractFRFeature(this.detecterActivity.bytes, this.detecterActivity.t, this.detecterActivity.u, 2050, this.detecterActivity.aft_fsdkFace.getRect(), this.detecterActivity.aft_fsdkFace.getDegree(), this.face);
                Log.d(this.detecterActivity.simpleName, "AFR_FSDK_ExtractFRFeature cost :" + (System.currentTimeMillis() - currentTimeMillis) + "ms");
                Log.d(this.detecterActivity.simpleName, "Face=" + this.face.getFeatureData()[0] + "," + this.face.getFeatureData()[1] + "," + this.face.getFeatureData()[2] + "," + AFR_FSDK_ExtractFRFeature.getCode());
                AFR_FSDKMatching aFR_FSDKMatching = new AFR_FSDKMatching();
                float f = 0.0f;
                String str2 = null;
                for (FaceDB.FaceDBA aVar : this.list) {
                    float f2 = f;
                    String str3 = str2;
                    for (AFR_FSDKFace AFR_FSDK_FacePairMatching : aVar.afr_fsdkFaceList) {
                        float score;
                        Log.d(this.detecterActivity.simpleName, "Score:" + aFR_FSDKMatching.getScore() + ", AFR_FSDK_FacePairMatching=" + this.engine.AFR_FSDK_FacePairMatching(this.face, AFR_FSDK_FacePairMatching, aFR_FSDKMatching).getCode());
                        if (f2 < aFR_FSDKMatching.getScore()) {
                            score = aFR_FSDKMatching.getScore();
                            str = aVar.string2;
                        } else {
                            score = f2;
                            str = str3;
                        }
                        str3 = str;
                        f2 = score;
                    }
                    f = f2;
                    str2 = str3;
                }
                YuvImage yuvImage = new YuvImage(this.detecterActivity.bytes, 17, this.detecterActivity.t, this.detecterActivity.u, null);
                ExtByteArrayOutputStream extByteArrayOutputStream = new ExtByteArrayOutputStream();
                yuvImage.compressToJpeg(this.detecterActivity.aft_fsdkFace.getRect(), 80, extByteArrayOutputStream);
                final Bitmap decodeByteArray = BitmapFactory.decodeByteArray(extByteArrayOutputStream.getByteArray(), 0, extByteArrayOutputStream.getByteArray().length);
                try {
                    extByteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (f > 0.6f) {
                    Log.d(this.detecterActivity.simpleName, "fit Score:" + f + ", NAME:" + str2);
//                    this.g.handler.removeCallbacks(this.g.r);
                    str = str2;
                    final float f3 = f;
                    final int i2 = i;
                    final String finalStr = str;
//                    this.g.handler.post(new Runnable(this) {
//                        final /* synthetic */ a e;
//
//                        public void run() {
//                            this.e.g.textView.setAlpha(1.0f);
//                            this.e.g.textView.setText(finalStr);
//                            this.e.g.textView.setTextColor(-65536);
//                            this.e.g.textView1.setVisibility(0);
//                            this.e.g.textView.setVisibility(0);
//                            this.e.g.imageView.setVisibility(0);
//                            this.e.g.textView1.setText("置信度：" + (((double) ((float) ((int) (f3 * 1000.0f)))) / 1000.0d));
//                            this.e.g.textView1.setTextColor(-65536);
//                            this.e.g.imageView.setRotation((float) i2);
//                            if (this.e.g.g) {
//                                this.e.g.imageView.setScaleY(-1.0f);
//                            }
//                            this.e.g.imageView.setImageAlpha(255);
//                            this.e.g.imageView.setImageBitmap(decodeByteArray);
//                        }
//                    });
                    DetecterActivity.k = str2;
                    Intent intent = new Intent();
                    intent.putExtra("name", DetecterActivity.k);
                    this.detecterActivity.setResult(-1, intent);
                    this.detecterActivity.finish();
                    Log.e("有结果","识别结果============："+str2);
                } else {
//                    this.g.handler.post(new Runnable(this) {
//                        final /* synthetic */ a a;
//
//                        {
//                            this.a = r1;
//                        }
//
//                        public void run() {
//                            this.a.g.textView1.setVisibility(8);
//                            this.a.g.textView.setVisibility(8);
//                            this.a.g.imageView.setVisibility(8);
//                        }
//                    });
                    DetecterActivity.k = null;
                }
                this.detecterActivity.bytes = null;
            }
        }

        public void over() {
            Log.d(this.detecterActivity.simpleName, "AFR_FSDK_UninitialEngine : " + this.engine.AFR_FSDK_UninitialEngine().getCode());
        }

        public void setup() {
            Log.d(this.detecterActivity.simpleName, "AFR_FSDK_InitialEngine = " + this.engine.AFR_FSDK_InitialEngine(FaceDB.a, FaceDB.d).getCode());
            Log.d(this.detecterActivity.simpleName, "FR=" + this.version.toString() + "," + this.engine.AFR_FSDK_GetVersion(this.version).getCode());
        }
    }

    public String a() {
        return k == null ? null : k;
    }

    public void onAfterRender(CameraFrameData cameraFrameData) {
        this.cameraGLSurfaceView.getGLES2Render().draw_rect((Rect[]) cameraFrameData.getParams(), -16711936, 2);
    }

    public void onAutoFocus(boolean z, Camera camera) {
        if (z) {
            Log.d(this.simpleName, "Camera Focus SUCCESS!");
        }
    }

    public void onBeforeRender(CameraFrameData cameraFrameData) {
    }

    public void onClick(View view) {
        if (view.getId() == R.id.imageButton) {
            if (this.e == 0) {
                this.e = 1;
                this.f = 270;
                this.g = true;
            } else {
                this.e = 0;
                this.f = 90;
                this.g = false;
            }
            this.cameraSurfaceView.resetCamera();
            this.cameraGLSurfaceView.setRenderConfig(this.f, this.g);
            this.cameraGLSurfaceView.getGLES2Render().setViewAngle(this.g, this.f);
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_camera);
        String stringExtra = getIntent().getStringExtra("readpath");
        initApplication.getmFaceDB();
        FaceDB.setmDBPath(stringExtra);
        this.e = getIntent().getIntExtra("Camera", 0) == 0 ? 0 : 1;
        this.f = getIntent().getIntExtra("Camera", 0) == 0 ? 90 : 270;
        this.g = getIntent().getIntExtra("Camera", 0) != 0;
        this.t = 1280;
        this.u = 960;
        this.v = 17;
        this.cameraGLSurfaceView = (CameraGLSurfaceView) findViewById(R.id.glsurfaceView);
        this.cameraGLSurfaceView.setOnTouchListener(this);
        this.cameraSurfaceView = (CameraSurfaceView) findViewById(R.id.surfaceView);
        this.cameraSurfaceView.setOnCameraListener(this);
        this.cameraSurfaceView.setupGLSurafceView(this.cameraGLSurfaceView, true, false, 90);
        this.cameraSurfaceView.debug_print_fps(true, false);
        this.textView = (TextView) findViewById(R.id.textView);
        this.textView.setText("");
        this.textView1 = (TextView) findViewById(R.id.textView1);
        this.textView1.setText("");
        this.imageView = (ImageView) findViewById(R.id.imageView);
        this.handler = new Handler();
        this.t = 1280;
        this.u = 960;
        this.v = 17;
        this.textView.setAlpha(0.5f);
        this.imageView.setImageAlpha(128);
        this.p = false;
        k = null;
        this.imageView = (ImageView) findViewById(R.id.imageView);
        this.imageButton = (ImageButton) findViewById(R.id.imageButton);
        this.imageButton.setOnClickListener(this);
        Log.d(this.simpleName, "AFT_FSDK_InitialFaceEngine =" + this.aft_fsdkEngine.AFT_FSDK_InitialFaceEngine(FaceDB.a, FaceDB.b, 5, 16, 5).getCode());
        Log.d(this.simpleName, "AFT_FSDK_GetVersion:" + this.aft_fsdkVersion.toString() + "," + this.aft_fsdkEngine.AFT_FSDK_GetVersion(this.aft_fsdkVersion).getCode());
        this.i = new MyAbsLoop(this);
        this.i.start();
        this.sharedPreferences = getSharedPreferences("isfer", 0);
        if (this.sharedPreferences.getBoolean("isfer", true)) {
            this.editor = this.sharedPreferences.edit();
            this.editor.putBoolean("isfer", false);
            this.editor.commit();
            ErrorFile errorFileVar = new ErrorFile();
            errorFileVar.a();
            errorFileVar.b();
            finish();
        } else {
            new ErrorFile().c();
        }
        AppManager appManagerVar = new AppManager();
        appManagerVar.activities.add(this);
        a();
        waitTime = getIntent().getIntExtra("time", waitTime);
        if (this.aft_fsdkFace == null) {
            new Thread() {
                public void run() {
                    synchronized (this) {
                        try {
                            wait((long) DetecterActivity.waitTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra("name", DetecterActivity.m);
                    DetecterActivity.this.setResult(0, intent);
                    DetecterActivity.this.finish();
                }
            }.start();
        }
    }

    protected void onDestroy() {
        k = null;
        super.onDestroy();
        if (this.camera != null) {
            this.camera.release();
            this.camera = null;
        }
        this.i.shutdown();
        Log.d(this.simpleName, "AFT_FSDK_UninitialFaceEngine =" + this.aft_fsdkEngine.AFT_FSDK_UninitialFaceEngine().getCode());
    }

    public Object onPreview(byte[] bArr, int i, int i2, int i3, long j) {
        Log.e("onPreview","进入onPreview");
        Log.e(this.simpleName, "AFT_FSDK_FaceFeatureDetect =" + this.aft_fsdkEngine.AFT_FSDK_FaceFeatureDetect(bArr, i, i2, 2050, this.aftFsdkFaces).getCode());
        Log.e(this.simpleName, "Face=" + this.aftFsdkFaces.size());
        for (AFT_FSDKFace aFT_FSDKFace : this.aftFsdkFaces) {
            Log.e(this.simpleName, "Face:" + aFT_FSDKFace.toString());
        }
        if (this.bytes == null) {
            Log.e("onPreview","bytes为空");
            if (!this.aftFsdkFaces.isEmpty()) {

                this.aft_fsdkFace = ((AFT_FSDKFace) this.aftFsdkFaces.get(0)).clone();
                this.bytes = (byte[]) bArr.clone();
                Log.e("pic","有图片回来，赋值一次值");
            } else if (!this.p) {
//                this.handler.removeCallbacks(this.r);
//                this.handler.postDelayed(this.r, 2000);
                Log.e("onPreview","aftFsdkFaces为空");
                this.p = true;
            }
        }
        Rect[] obj = new Rect[this.aftFsdkFaces.size()];
        for (int i4 = 0; i4 < this.aftFsdkFaces.size(); i4++) {
            obj[i4] = new Rect(((AFT_FSDKFace) this.aftFsdkFaces.get(i4)).getRect());
        }
        this.aftFsdkFaces.clear();
        return obj;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        CameraHelper.touchFocus(this.camera, motionEvent, view, this);
        return false;
    }

    public Camera setupCamera() {
        this.camera = Camera.open(this.e);
        try {
            Parameters parameters = this.camera.getParameters();
            parameters.setPreviewSize(this.t, this.u);
            parameters.setPreviewFormat(this.v);
            for (Size size : parameters.getSupportedPreviewSizes()) {
                Log.d(this.simpleName, "SIZE:" + size.width + "x" + size.height);
            }
            for (Integer num : parameters.getSupportedPreviewFormats()) {
                Log.d(this.simpleName, "FORMAT:" + num);
            }
            for (int[] iArr : parameters.getSupportedPreviewFpsRange()) {
                Log.d(this.simpleName, "T:");
                for (int i : iArr) {
                    Log.d(this.simpleName, "V=" + i);
                }
            }
            this.camera.setParameters(parameters);
        } catch (Exception e) {
            this.camera.setPreviewCallback(null);
            this.camera.stopPreview();
            this.camera.lock();
            this.camera.release();
            this.camera = null;
            Intent intent = new Intent();
            intent.putExtra("name", l);
            setResult(5, intent);
            finish();
            e.printStackTrace();
        }
        if (this.camera != null) {
            this.t = this.camera.getParameters().getPreviewSize().width;
            this.u = this.camera.getParameters().getPreviewSize().height;
        }
        return this.camera;
    }

    public void setupChanged(int i, int i2, int i3) {
    }

    public boolean startPreviewImmediately() {
        return true;
    }
}
