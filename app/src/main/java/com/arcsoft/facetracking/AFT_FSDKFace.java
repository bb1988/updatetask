package com.arcsoft.facetracking;

import android.graphics.Rect;

public class AFT_FSDKFace {
    int mDegree;
    Rect mRect;

    public AFT_FSDKFace() {
        this.mRect = new Rect();
        this.mDegree = 0;
    }

    public AFT_FSDKFace(AFT_FSDKFace aFT_FSDKFace) {
        this.mRect = new Rect(aFT_FSDKFace.getRect());
        this.mDegree = aFT_FSDKFace.getDegree();
    }

    public AFT_FSDKFace clone() {
        return new AFT_FSDKFace(this);
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
