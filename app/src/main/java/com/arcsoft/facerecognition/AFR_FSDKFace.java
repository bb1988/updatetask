package com.arcsoft.facerecognition;

public class AFR_FSDKFace {
    public static final int FEATURE_SIZE = 22020;
    byte[] mFeatureData;

    public AFR_FSDKFace() {
        this.mFeatureData = new byte[FEATURE_SIZE];
    }

    public AFR_FSDKFace(AFR_FSDKFace aFR_FSDKFace) {
        this.mFeatureData = (byte[]) aFR_FSDKFace.getFeatureData().clone();
    }

    public AFR_FSDKFace(byte[] bArr) {
        this.mFeatureData = bArr;
    }

    public AFR_FSDKFace clone() {
        return new AFR_FSDKFace(this);
    }

    public byte[] getFeatureData() {
        return this.mFeatureData;
    }

    public void setFeatureData(byte[] bArr) {
        this.mFeatureData = bArr;
    }
}
