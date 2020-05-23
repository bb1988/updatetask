package com.bb.updatetask;

import android.os.Process;
import android.util.Log;
import java.io.File;

public class ErrorFile {
    public static Boolean a(long j) {
        File file = new File("/sdcard/SDK_Demo/log.dll");
        BaseA baseAVar = new BaseA();
        try {
            String str = new String(BaseA.methodA(file));
            if (str.equals("Error")) {
                return Boolean.valueOf(true);
            }
            long longValue = j - Long.valueOf(str).longValue();
            long j2 = longValue / 86400000;
            j2 = ((longValue - (j2 * 86400000)) - (((longValue - (86400000 * j2)) / 3600000) * 3600000)) / 60000;
            return longValue >= 7776000000L ? Boolean.valueOf(true) : Boolean.valueOf(false);
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.valueOf(true);
        }
    }

    public static void a(String str) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    public File a(String str, String str2) {
        File file;
        Exception e;
        a(str);
        try {
            file = new File(str + str2);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                return file;
            }
        } catch (Exception e3) {
            Exception exception = e3;
            file = null;
            e = exception;
            e.printStackTrace();
            return file;
        }
        return file;
    }

    public void a() {
        a(String.valueOf(System.currentTimeMillis()), "/sdcard/SDK_Demo/", "log.dll");
    }

    public void a(String str, String str2, String str3) {
        a(str2, str3);
        String str4 = str2 + str3;
        try {
            File file = new File(str4);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + str4);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            BaseA baseAVar = new BaseA();
            try {
                BaseA.methodA(str, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            Log.e("TestFile", "Error on write File:" + e2);
        }
    }

    public void b() {
        a("20000", "/sdcard/SDK_Demo/", "logtime.dll");
    }

    public void c() {
        File file = new File("/sdcard/SDK_Demo/logtime.dll");
        BaseA baseAVar = new BaseA();
        try {
            String str = new String(BaseA.methodA(file));
            if (str.equals("Error")) {
                Process.killProcess(Process.myPid());
                System.exit(0);
                return;
            }
            a(String.valueOf(Integer.valueOf(str).intValue() - 1), "/sdcard/SDK_Demo/", "logtime.dll");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
