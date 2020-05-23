package com.bb.updatetask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

public class InitApplication {
    private static FaceDB mFaceDB;
    private final String TAG = getClass().toString();
    private Uri mImage;

    public static Bitmap decodeImage(String str) {
        try {
            int attributeInt = new ExifInterface(str).getAttributeInt("Orientation", 1);
            Options options = new Options();
            options.inSampleSize = 1;
            options.inJustDecodeBounds = false;
            Bitmap decodeFile = BitmapFactory.decodeFile(str, options);
            Matrix matrix = new Matrix();
            if (attributeInt == 6) {
                matrix.postRotate(90.0f);
            } else if (attributeInt == 3) {
                matrix.postRotate(180.0f);
            } else if (attributeInt == 8) {
                matrix.postRotate(270.0f);
            }
            Bitmap createBitmap = Bitmap.createBitmap(decodeFile, 0, 0, decodeFile.getWidth(), decodeFile.getHeight(), matrix, true);
            Log.d("com.arcsoft", "check target Image:" + createBitmap.getWidth() + "X" + createBitmap.getHeight());
            if (!createBitmap.equals(decodeFile)) {
                decodeFile.recycle();
            }
            return createBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setmFaceDB(FaceDB faceDB) {
        mFaceDB = faceDB;
    }

    public Uri getCaptureImage() {
        return this.mImage;
    }

    public FaceDB getmFaceDB() {
        return mFaceDB;
    }

    public void setCaptureImage(Uri uri) {
        this.mImage = uri;
    }
}
