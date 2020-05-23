package com.arcsoft.facedetection;

import java.util.List;

public class AFD_FSDKEngine {
    public static final int AFD_FOC_0 = 1;
    public static final int AFD_FOC_120 = 7;
    public static final int AFD_FOC_150 = 8;
    public static final int AFD_FOC_180 = 4;
    public static final int AFD_FOC_210 = 9;
    public static final int AFD_FOC_240 = 10;
    public static final int AFD_FOC_270 = 3;
    public static final int AFD_FOC_30 = 5;
    public static final int AFD_FOC_300 = 11;
    public static final int AFD_FOC_330 = 12;
    public static final int AFD_FOC_60 = 6;
    public static final int AFD_FOC_90 = 2;
    public static final int AFD_OPF_0_HIGHER_EXT = 5;
    public static final int AFD_OPF_0_ONLY = 1;
    public static final int AFD_OPF_180_ONLY = 4;
    public static final int AFD_OPF_270_ONLY = 3;
    public static final int AFD_OPF_90_ONLY = 2;
    public static final int CP_PAF_NV21 = 2050;
    private final String TAG = getClass().toString();
    private AFD_FSDKError error = new AFD_FSDKError();
    private Integer handle = Integer.valueOf(0);
    private int mFaceCount = 0;
    private AFD_FSDKFace[] mFaces = new AFD_FSDKFace[16];

    static {
        System.loadLibrary("mpbase");
        System.loadLibrary("ArcSoft_FDEngine");
    }

    private native int FD_Config(int i, int i2);

    private native int FD_GetErrorCode(int i);

    private native int FD_GetResult(int i, AFD_FSDKFace[] aFD_FSDKFaceArr);

    private native int FD_Init(String str, String str2, int i, int i2, int i3, AFD_FSDKError aFD_FSDKError);

    private native int FD_Process(int i, byte[] bArr, int i2, int i3, int i4);

    private native int FD_UnInit(int i);

    private native String FD_Version(int i);

    private AFD_FSDKFace[] obtainFaceArray(int i) {
        if (this.mFaceCount < i) {
            if (this.mFaces.length < i) {
                this.mFaces = new AFD_FSDKFace[(((i / 16) + 1) * 16)];
            }
            for (int i2 = this.mFaceCount; i2 < i; i2++) {
                this.mFaces[i2] = new AFD_FSDKFace();
            }
            this.mFaceCount = i;
        }
        return this.mFaces;
    }

    public AFD_FSDKError AFD_FSDK_GetVersion(AFD_FSDKVersion aFD_FSDKVersion) {
        if (aFD_FSDKVersion == null) {
            this.error.mCode = 2;
        } else if (this.handle.intValue() != 0) {
            this.error.mCode = 0;
            aFD_FSDKVersion.mVersion = FD_Version(this.handle.intValue());
        } else {
            this.error.mCode = 5;
        }
        return this.error;
    }

    public AFD_FSDKError AFD_FSDK_InitialFaceEngine(String str, String str2, int i, int i2, int i3) {
        this.handle = Integer.valueOf(FD_Init(str, str2, i, i2, i3, this.error));
        return this.error;
    }

    public AFD_FSDKError AFD_FSDK_StillImageFaceDetection(byte[] bArr, int i, int i2, int i3, List<AFD_FSDKFace> list) {
        if (list == null || bArr == null) {
            this.error.mCode = 2;
        } else if (this.handle.intValue() != 0) {
            int FD_Process = FD_Process(this.handle.intValue(), bArr, i, i2, i3);
            this.error.mCode = FD_GetErrorCode(this.handle.intValue());
            if (FD_Process > 0 && this.error.mCode == 0) {
                AFD_FSDKFace[] obtainFaceArray = obtainFaceArray(FD_Process);
                this.error.mCode = FD_GetResult(this.handle.intValue(), obtainFaceArray);
                for (int i4 = 0; i4 < FD_Process; i4++) {
                    list.add(obtainFaceArray[i4]);
                }
            }
        } else {
            this.error.mCode = 5;
        }
        return this.error;
    }

    public AFD_FSDKError AFD_FSDK_UninitialFaceEngine() {
        if (this.handle.intValue() != 0) {
            this.error.mCode = FD_UnInit(this.handle.intValue());
            this.handle = Integer.valueOf(0);
        } else {
            this.error.mCode = 5;
        }
        return this.error;
    }
}
