package com.arcsoft.facedetection;

import android.graphics.Rect;

public class AFD_FSDKFace {
    int mDegree;
    Rect mRect;

    public AFD_FSDKFace() {
        this.mRect = new Rect();
        this.mDegree = 0;
    }

    public AFD_FSDKFace(AFD_FSDKFace aFD_FSDKFace) {
        this.mRect = new Rect(aFD_FSDKFace.getRect());
        this.mDegree = aFD_FSDKFace.getDegree();
    }

    public AFD_FSDKFace clone() {
        return new AFD_FSDKFace(this);
    }

    public int getDegree() {
        return this.mDegree;
    }

    public Rect getRect() {
        return this.mRect;
    }

    public String toString() {
        return this.mRect.toString() + "," + this.mDegree;
    }
}
