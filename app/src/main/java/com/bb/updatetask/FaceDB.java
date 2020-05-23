package com.bb.updatetask;

import android.util.Log;
import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKVersion;
import com.guo.android_extend.java.ExtInputStream;
import com.guo.android_extend.java.ExtOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FaceDB {
    public static String a = "72rJVPFqsbB4jfosGyDAkXMt7sWMkTCNEm95XKefpn2T";
    public static String b = "HdoS8Z5QY9L8VZtpQSWJ4Sjo6giwJ51jiUfTuNSgekha";
    public static String c = "HdoS8Z5QY9L8VZtpQSWJ4SjvG5z5ZG8j7ePAbZZFuiFS";
    public static String d = "HdoS8Z5QY9L8VZtpQSWJ4SkQuh2oCUuWmLoLDLn4Su8j";
    public static String string1;
    public static List<FaceDBA> faceDBAList;
    AFR_FSDKEngine afr_fsdkEngine;
    AFR_FSDKVersion afr_fsdkVersion;
    boolean aBoolean;
    private final String faceDBClazz = getClass().toString();

    class FaceDBA {
        String string2;
        List<AFR_FSDKFace> afr_fsdkFaceList = new ArrayList();
        final  FaceDB faceDB;

        public FaceDBA(FaceDB faceDB, String str) {
            this.faceDB = faceDB;
            this.string2 = str;
        }
    }

    public FaceDB(String str) {
        string1 = str;
        faceDBAList = new ArrayList();
        this.afr_fsdkVersion = new AFR_FSDKVersion();
        this.aBoolean = false;
        this.afr_fsdkEngine = new AFR_FSDKEngine();
        AFR_FSDKError AFR_FSDK_InitialEngine = this.afr_fsdkEngine.AFR_FSDK_InitialEngine(a, d);
        if (AFR_FSDK_InitialEngine.getCode() != 0) {
            Log.e(this.faceDBClazz, "AFR_FSDK_InitialEngine fail! error code :" + AFR_FSDK_InitialEngine.getCode());
            return;
        }
        this.afr_fsdkEngine.AFR_FSDK_GetVersion(this.afr_fsdkVersion);
        Log.e(this.faceDBClazz, "AFR_FSDK_GetVersion=" + this.afr_fsdkVersion.toString());
    }

    private boolean a() {
        if (!faceDBAList.isEmpty()) {
            return false;
        }
        try {
            InputStream fileInputStream = new FileInputStream(string1 + "/face.txt");
            ExtInputStream extInputStream = new ExtInputStream(fileInputStream);
            String readString = extInputStream.readString();
            Log.e(this.faceDBClazz, "loadInfo: " + readString);
            if (readString.equals(this.afr_fsdkVersion.toString() + "," + this.afr_fsdkVersion.getFeatureLevel())) {
                this.aBoolean = true;
            }
            if (readString != null) {
                for (readString = extInputStream.readString(); readString != null; readString = extInputStream.readString()) {
                    if (new File(string1 + "/" + readString + ".data").exists()) {
                        faceDBAList.add(new FaceDBA(this, new String(readString)));
                    }
                }
            }
            extInputStream.close();
            fileInputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static String getmDBPath() {
        return string1;
    }

    public static List<FaceDBA> getmRegister() {
        return faceDBAList;
    }

    public static void setmDBPath(String str) {
        string1 = str;
    }

    public static void setmRegister(List<FaceDBA> list) {
        faceDBAList = list;
    }

    public void a(String str, AFR_FSDKFace aFR_FSDKFace, String str2) {
        string1 = str2;
        File file = new File(string1);
        if (!(file.exists() || file.mkdirs())) {
            Log.e("创建目录", "创建目录失败，请检查是否有sdcard读写权限。");
        }
        try {
            FaceDBA faceDBAVar;
            Object obj = null;
            for (FaceDBA faceDBAVar2 : faceDBAList) {
                if (faceDBAVar2.string2.equals(str)) {
                    faceDBAVar2.afr_fsdkFaceList.add(aFR_FSDKFace);
                    obj = null;
                    break;
                }
            }
            //好像不会执行
            if (obj != null) {
                faceDBAVar = new FaceDBA(this, str);
                faceDBAVar.afr_fsdkFaceList.add(aFR_FSDKFace);
                faceDBAList.add(faceDBAVar);
            }

            if (a(string1)) {
                OutputStream fileOutputStream = new FileOutputStream(string1 + "/face.txt", true);
                ExtOutputStream extOutputStream = new ExtOutputStream(fileOutputStream);
                for (FaceDBA faceDBAVar22 : faceDBAList) {
                    extOutputStream.writeString(faceDBAVar22.string2);
                }
                extOutputStream.close();
                fileOutputStream.close();
                OutputStream fileOutputStream2 = new FileOutputStream(string1 + "/" + str + ".data", false);
                ExtOutputStream extOutputStream2 = new ExtOutputStream(fileOutputStream2);
                extOutputStream2.writeBytes(aFR_FSDKFace.getFeatureData());
                extOutputStream2.close();
                fileOutputStream2.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public boolean a(String str) {
        try {
            Log.e("faceDB", "aaaaaaaa======。");
            OutputStream fileOutputStream = new FileOutputStream(str + "/face.txt");
            ExtOutputStream extOutputStream = new ExtOutputStream(fileOutputStream);
            extOutputStream.writeString(this.afr_fsdkVersion.toString() + "," + this.afr_fsdkVersion.getFeatureLevel());
            extOutputStream.close();
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e("faceDB", "FileNotFoundException======。");
            e.printStackTrace();
            return false;
        } catch (IOException e2) {
            Log.e("faceDB", "IOException======。");
            e2.printStackTrace();
            return false;
        }
    }

    public boolean loadFaces() {
        if (a()) {
            try {
                for (FaceDBA faceDBAVar : faceDBAList) {
                    Log.d(this.faceDBClazz, "load name:" + faceDBAVar.string2 + "'s face feature data.");
                    InputStream fileInputStream = new FileInputStream(string1 + "/" + faceDBAVar.string2 + ".data");
                    ExtInputStream extInputStream = new ExtInputStream(fileInputStream);
                    Object obj = null;
                    do {
                        if (obj != null) {
                            if (this.aBoolean) {
                                faceDBAVar.afr_fsdkFaceList.add((AFR_FSDKFace) obj);
                            } else {
                                faceDBAVar.afr_fsdkFaceList.add((AFR_FSDKFace) obj);
                            }
                        }
                        obj = new AFR_FSDKFace();
                    } while (extInputStream.readBytes(((AFR_FSDKFace) obj).getFeatureData()));
                    extInputStream.close();
                    fileInputStream.close();
                    Log.d(this.faceDBClazz, "load name: size = " + faceDBAVar.afr_fsdkFaceList.size());
                }
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }
}
