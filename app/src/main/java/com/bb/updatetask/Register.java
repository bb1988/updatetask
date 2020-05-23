package com.bb.updatetask;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import com.arcsoft.facedetection.AFD_FSDKEngine;
import com.arcsoft.facedetection.AFD_FSDKError;
import com.arcsoft.facedetection.AFD_FSDKFace;
import com.arcsoft.facedetection.AFD_FSDKVersion;
import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKVersion;
import com.guo.android_extend.image.ImageConverter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Register {
    private static final int MSG_CODE = 4096;
    private static final int MSG_EVENT_FD_ERROR = 4100;
    private static final int MSG_EVENT_FR_ERROR = 4101;
    private static final int MSG_EVENT_NO_FACE = 4098;
    private static final int MSG_EVENT_NO_FEATURE = 4099;
    private static final int MSG_EVENT_REG = 4097;
    private static final int REQUEST_CODE_OP = 3;
    private static List<String> imagePaths = null;
    private final String TAG = getClass().toString();
    private AFR_FSDKFace mAFR_FSDKFace;
    private Bitmap mBitmap;
    private TextView view;

    public static boolean checkIsImageFile(String str) {
        String toLowerCase = str.substring(str.lastIndexOf(".") + 1, str.length()).toLowerCase();
        return toLowerCase.equals("jpg") || toLowerCase.equals("png") || toLowerCase.equals("gif") || toLowerCase.equals("jpeg") || toLowerCase.equals("bmp");
    }

    protected int ReInit(String str, String str2, InitApplication initApplication) {
        return regist(str, str2, initApplication);
    }

    public boolean fileIsExists(String str) {
        try {
            return new File(str).exists();
        } catch (Exception e) {
            return false;
        }
    }

    public int regist(String str, String str2, InitApplication initApplication) {
        if (!fileIsExists(str)) {
            return 4;
        }
        if (!checkIsImageFile(str)) {
            return 0;
        }
        this.mBitmap = InitApplication.decodeImage(str);
        byte[] bArr = new byte[(((this.mBitmap.getWidth() * this.mBitmap.getHeight()) * 3) / 2)];
        try {
            ImageConverter imageConverter = new ImageConverter();
            imageConverter.initial(this.mBitmap.getWidth(), this.mBitmap.getHeight(), 2050);
            if (imageConverter.convert(this.mBitmap, bArr)) {
                Log.d(this.TAG, "convert ok!");
            }
            imageConverter.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AFD_FSDKEngine aFD_FSDKEngine = new AFD_FSDKEngine();
        AFD_FSDKVersion aFD_FSDKVersion = new AFD_FSDKVersion();
        List arrayList = new ArrayList();
        AFD_FSDKError AFD_FSDK_InitialFaceEngine = aFD_FSDKEngine.AFD_FSDK_InitialFaceEngine(FaceDB.a, FaceDB.c, 5, 16, 300);
        Log.d(this.TAG, "AFD_FSDK_InitialFaceEngine = " + AFD_FSDK_InitialFaceEngine.getCode());
        if (AFD_FSDK_InitialFaceEngine.getCode() != 0) {
            Message obtain = Message.obtain();
            obtain.what = MSG_CODE;
            obtain.arg1 = MSG_EVENT_FD_ERROR;
            obtain.arg2 = AFD_FSDK_InitialFaceEngine.getCode();
        }
        Log.d(this.TAG, "AFD_FSDK_GetVersion =" + aFD_FSDKVersion.toString() + ", " + aFD_FSDKEngine.AFD_FSDK_GetVersion(aFD_FSDKVersion).getCode());
        Log.d(this.TAG, "AFD_FSDK_StillImageFaceDetection =" + aFD_FSDKEngine.AFD_FSDK_StillImageFaceDetection(bArr, this.mBitmap.getWidth(), this.mBitmap.getHeight(), 2050, arrayList).getCode() + "<" + arrayList.size());
        if (arrayList.isEmpty()) {
            Message obtain2 = Message.obtain();
            obtain2.what = MSG_CODE;
            obtain2.arg1 = MSG_EVENT_NO_FACE;
            ArrayList arrayList2 = new ArrayList();
            return 3;
        }
        System.out.println("有人脸");
        AFR_FSDKVersion aFR_FSDKVersion = new AFR_FSDKVersion();
        AFR_FSDKEngine aFR_FSDKEngine = new AFR_FSDKEngine();
        AFR_FSDKFace aFR_FSDKFace = new AFR_FSDKFace();
        AFR_FSDKError AFR_FSDK_InitialEngine = aFR_FSDKEngine.AFR_FSDK_InitialEngine(FaceDB.a, FaceDB.d);
        Log.d("com.arcsoft", "AFR_FSDK_InitialEngine = " + AFR_FSDK_InitialEngine.getCode());
        if (AFR_FSDK_InitialEngine.getCode() != 0) {
            Message obtain3 = Message.obtain();
            obtain3.what = MSG_CODE;
            obtain3.arg1 = MSG_EVENT_FR_ERROR;
            obtain3.arg2 = AFR_FSDK_InitialEngine.getCode();
        }
        Log.d("com.arcsoft", "FR=" + aFD_FSDKVersion.toString() + "," + aFR_FSDKEngine.AFR_FSDK_GetVersion(aFR_FSDKVersion).getCode());
        AFR_FSDKError AFR_FSDK_ExtractFRFeature = aFR_FSDKEngine.AFR_FSDK_ExtractFRFeature(bArr, this.mBitmap.getWidth(), this.mBitmap.getHeight(), 2050, new Rect(((AFD_FSDKFace) arrayList.get(0)).getRect()), ((AFD_FSDKFace) arrayList.get(0)).getDegree(), aFR_FSDKFace);
        Log.d("com.arcsoft", "Face=" + aFR_FSDKFace.getFeatureData()[0] + "," + aFR_FSDKFace.getFeatureData()[1] + "," + aFR_FSDKFace.getFeatureData()[2] + "," + AFR_FSDK_ExtractFRFeature.getCode());
        if (AFR_FSDK_ExtractFRFeature.getCode() == 0) {
            this.mAFR_FSDKFace = aFR_FSDKFace.clone();
            int width = ((AFD_FSDKFace) arrayList.get(0)).getRect().width();
            int height = ((AFD_FSDKFace) arrayList.get(0)).getRect().height();
            Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
            new Canvas(createBitmap).drawBitmap(this.mBitmap, ((AFD_FSDKFace) arrayList.get(0)).getRect(), new Rect(0, 0, width, height), null);
            Message obtain2 = Message.obtain();
            obtain2.what = MSG_CODE;
            obtain2.arg1 = MSG_EVENT_REG;
            obtain2.obj = createBitmap;
            initApplication.getmFaceDB().a(str.substring(str.lastIndexOf("/") + 1, str.lastIndexOf(".")), this.mAFR_FSDKFace, str2);
            return 1;
        }
        Message obtain2 = Message.obtain();
        obtain2.what = MSG_CODE;
        obtain2.arg1 = MSG_EVENT_NO_FEATURE;
        return 2;
    }
}
