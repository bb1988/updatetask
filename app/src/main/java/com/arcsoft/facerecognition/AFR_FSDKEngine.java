package com.arcsoft.facerecognition;

import android.graphics.Rect;

public class AFR_FSDKEngine {
    public static final int AFR_FOC_0 = 1;
    public static final int AFR_FOC_120 = 7;
    public static final int AFR_FOC_150 = 8;
    public static final int AFR_FOC_180 = 4;
    public static final int AFR_FOC_210 = 9;
    public static final int AFR_FOC_240 = 10;
    public static final int AFR_FOC_270 = 3;
    public static final int AFR_FOC_30 = 5;
    public static final int AFR_FOC_300 = 11;
    public static final int AFR_FOC_330 = 12;
    public static final int AFR_FOC_60 = 6;
    public static final int AFR_FOC_90 = 2;
    public static final int CP_PAF_NV21 = 2050;
    private final String TAG = getClass().toString();
    private AFR_FSDKError error = new AFR_FSDKError();
    private Integer handle = Integer.valueOf(0);

    static {
        System.loadLibrary("mpbase");
        System.loadLibrary("ArcSoft_FREngine");
    }

    private AFR_FSDKError AFR_FSDK_UpdateFeature(AFR_FSDKFace aFR_FSDKFace, AFR_FSDKFace aFR_FSDKFace2) {
        if (aFR_FSDKFace == null || aFR_FSDKFace2 == null) {
            this.error.mCode = 2;
        } else if (this.handle.intValue() != 0) {
            this.error.mCode = FR_UpdateFeature(this.handle.intValue(), aFR_FSDKFace.mFeatureData, aFR_FSDKFace2.mFeatureData);
        } else {
            this.error.mCode = 5;
        }
        return this.error;
    }

    private native int FR_GetErrorCode(int i);

    private native int FR_GetFeatures(int i, byte[] bArr);

    private native int FR_Init(String str, String str2, AFR_FSDKError aFR_FSDKError);

    private native float FR_Recognize(int i, byte[] bArr, byte[] bArr2);

    private native int FR_Registe(int i, byte[] bArr, int i2, int i3, int i4, Rect rect, int i5);

    private native int FR_UnInit(int i);

    private native int FR_UpdateFeature(int i, byte[] bArr, byte[] bArr2);

    private native int FR_Version(int i, AFR_FSDKVersion aFR_FSDKVersion);

    public AFR_FSDKError AFR_FSDK_ExtractFRFeature(byte[] bArr, int i, int i2, int i3, Rect rect, int i4, AFR_FSDKFace aFR_FSDKFace) {
        if (aFR_FSDKFace == null || bArr == null) {
            this.error.mCode = 2;
        } else if (aFR_FSDKFace.mFeatureData == null) {
            this.error.mCode = 2;
        } else if (aFR_FSDKFace.mFeatureData.length < AFR_FSDKFace.FEATURE_SIZE) {
            this.error.mCode = 2;
        } else if (this.handle.intValue() != 0) {
            int FR_Registe = FR_Registe(this.handle.intValue(), bArr, i, i2, i3, rect, i4);
            this.error.mCode = FR_GetErrorCode(this.handle.intValue());
            if (FR_Registe > 0 && this.error.mCode == 0) {
                this.error.mCode = FR_GetFeatures(this.handle.intValue(), aFR_FSDKFace.mFeatureData);
            }
        } else {
            this.error.mCode = 5;
        }
        return this.error;
    }

    public AFR_FSDKError AFR_FSDK_FacePairMatching(AFR_FSDKFace aFR_FSDKFace, AFR_FSDKFace aFR_FSDKFace2, AFR_FSDKMatching aFR_FSDKMatching) {
        if (aFR_FSDKFace == null || aFR_FSDKFace2 == null || aFR_FSDKMatching == null) {
            if (this.error != null) {
                this.error.mCode = 2;
            }
        } else if (this.handle.intValue() != 0) {
            aFR_FSDKMatching.mScore = FR_Recognize(this.handle.intValue(), aFR_FSDKFace.mFeatureData, aFR_FSDKFace2.mFeatureData);
            this.error.mCode = FR_GetErrorCode(this.handle.intValue());
        } else {
            this.error.mCode = 5;
        }
        return this.error;
    }

    public AFR_FSDKError AFR_FSDK_GetVersion(AFR_FSDKVersion aFR_FSDKVersion) {
        if (this.handle.intValue() != 0) {
            this.error.mCode = FR_Version(this.handle.intValue(), aFR_FSDKVersion);
        } else {
            this.error.mCode = 5;
        }
        return this.error;
    }

    public AFR_FSDKError AFR_FSDK_InitialEngine(String str, String str2) {
        this.handle = Integer.valueOf(FR_Init(str, str2, this.error));
        return this.error;
    }

    public AFR_FSDKError AFR_FSDK_UninitialEngine() {
        if (this.handle.intValue() != 0) {
            this.error.mCode = FR_UnInit(this.handle.intValue());
            this.handle = Integer.valueOf(0);
        } else {
            this.error.mCode = 5;
        }
        return this.error;
    }
}
