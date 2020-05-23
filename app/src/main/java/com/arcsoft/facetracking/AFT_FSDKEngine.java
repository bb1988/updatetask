package com.arcsoft.facetracking;

import java.util.List;

public class AFT_FSDKEngine {
    public static final int AFT_FOC_0 = 1;
    public static final int AFT_FOC_180 = 4;
    public static final int AFT_FOC_270 = 3;
    public static final int AFT_FOC_90 = 2;
    public static final int AFT_OPF_0_HIGHER_EXT = 5;
    public static final int AFT_OPF_0_ONLY = 1;
    public static final int AFT_OPF_180_ONLY = 4;
    public static final int AFT_OPF_270_ONLY = 3;
    public static final int AFT_OPF_90_ONLY = 2;
    public static final int CP_PAF_NV21 = 2050;
    private final String TAG = getClass().toString();
    private AFT_FSDKError error = new AFT_FSDKError();
    private Integer handle = Integer.valueOf(0);
    private int mFaceCount = 0;
    private AFT_FSDKFace[] mFaces = new AFT_FSDKFace[16];

    static {
        System.loadLibrary("mpbase");
        System.loadLibrary("ArcSoft_FTEngine");
    }

    private native int FT_Config(int i, int i2);

    private native int FT_GetErrorCode(int i);

    private native int FT_GetResult(int i, AFT_FSDKFace[] aFT_FSDKFaceArr);

    private native int FT_Init(String str, String str2, int i, int i2, int i3, AFT_FSDKError aFT_FSDKError);

    private native int FT_Process(int i, byte[] bArr, int i2, int i3, int i4);

    private native int FT_UnInit(int i);

    private native String FT_Version(int i);

    private AFT_FSDKFace[] obtainFaceArray(int i) {
        if (this.mFaceCount < i) {
            if (this.mFaces.length < i) {
                this.mFaces = new AFT_FSDKFace[(((i / 16) + 1) * 16)];
            }
            for (int i2 = this.mFaceCount; i2 < i; i2++) {
                this.mFaces[i2] = new AFT_FSDKFace();
            }
            this.mFaceCount = i;
        }
        return this.mFaces;
    }

    public AFT_FSDKError AFT_FSDK_FaceFeatureDetect(byte[] bArr, int i, int i2, int i3, List<AFT_FSDKFace> list) {
        if (list == null || bArr == null) {
            this.error.mCode = 2;
        } else if (this.handle.intValue() != 0) {
            int FT_Process = FT_Process(this.handle.intValue(), bArr, i, i2, i3);
            this.error.mCode = FT_GetErrorCode(this.handle.intValue());
            if (FT_Process > 0 && this.error.mCode == 0) {
                AFT_FSDKFace[] obtainFaceArray = obtainFaceArray(FT_Process);
                this.error.mCode = FT_GetResult(this.handle.intValue(), obtainFaceArray);
                for (int i4 = 0; i4 < FT_Process; i4++) {
                    list.add(obtainFaceArray[i4]);
                }
            }
        } else {
            this.error.mCode = 5;
        }
        return this.error;
    }

    public AFT_FSDKError AFT_FSDK_GetVersion(AFT_FSDKVersion aFT_FSDKVersion) {
        if (aFT_FSDKVersion == null) {
            this.error.mCode = 2;
        } else if (this.handle.intValue() != 0) {
            this.error.mCode = 0;
            aFT_FSDKVersion.mVersion = FT_Version(this.handle.intValue());
        } else {
            this.error.mCode = 5;
        }
        return this.error;
    }

    public AFT_FSDKError AFT_FSDK_InitialFaceEngine(String str, String str2, int i, int i2, int i3) {
        this.handle = Integer.valueOf(FT_Init(str, str2, i, i2, i3, this.error));
        return this.error;
    }

    public AFT_FSDKError AFT_FSDK_UninitialFaceEngine() {
        if (this.handle.intValue() != 0) {
            this.error.mCode = FT_UnInit(this.handle.intValue());
            this.handle = Integer.valueOf(0);
        } else {
            this.error.mCode = 5;
        }
        return this.error;
    }
}
