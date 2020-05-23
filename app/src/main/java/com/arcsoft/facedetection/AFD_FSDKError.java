package com.arcsoft.facedetection;

public class AFD_FSDKError {
    public static final int MERR_BAD_STATE = 5;
    public static final int MERR_BASIC_BASE = 1;
    public static final int MERR_BUFFER_OVERFLOW = 9;
    public static final int MERR_BUFFER_UNDERFLOW = 10;
    public static final int MERR_FSDK_BASE = 28672;
    public static final int MERR_FSDK_INVALID_APP_ID = 28673;
    public static final int MERR_FSDK_INVALID_ID_PAIR = 28675;
    public static final int MERR_FSDK_INVALID_SDK_ID = 28674;
    public static final int MERR_FSDK_LICENCE_EXPIRED = 28678;
    public static final int MERR_FSDK_MISMATCH_ID_AND_SDK = 28676;
    public static final int MERR_FSDK_SYSTEM_VERSION_UNSUPPORTED = 28677;
    public static final int MERR_INVALID_PARAM = 2;
    public static final int MERR_NO_MEMORY = 4;
    public static final int MERR_UNKNOWN = 1;
    public static final int MERR_UNSUPPORTED = 3;
    public static final int MOK = 0;
    int mCode = 0;

    public int getCode() {
        return this.mCode;
    }
}
