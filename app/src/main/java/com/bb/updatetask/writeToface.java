package com.bb.updatetask;

import android.util.Log;
import com.guo.android_extend.java.ExtOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class writeToface {
    public static String mDBPath = FaceDB.getmDBPath();

    public static boolean checkIsDataFile(String str) {
        return str.substring(str.lastIndexOf(".") + 1, str.length()).toLowerCase().equals("data");
    }

    public static List<String> getFilesAllName(String str) {
        File[] listFiles = new File(str).listFiles();
        List<String> arrayList = new ArrayList();
        for (File path : listFiles) {
            String path2 = path.getPath();
            if (checkIsDataFile(path2)) {
                arrayList.add(path2.substring(path2.lastIndexOf("/") + 1, path2.lastIndexOf(".")));
            }
        }
        return arrayList;
    }

    public void addFaceTxt(String str) {
        mDBPath = str;
        File file = new File(mDBPath);
        if (!(file.exists() || file.mkdirs())) {
            Log.e("创建目录", "创建目录失败，请检查是否有sdcard读写权限。");
        }
        Log.e("addFaceTxt", "======。");
        try {
            if (new FaceDB(str).a(mDBPath)) {
                Log.e("addFaceTxt", "a 进来了======。");
                OutputStream fileOutputStream = new FileOutputStream(mDBPath + "/face.txt", true);
                ExtOutputStream extOutputStream = new ExtOutputStream(fileOutputStream);
                for (String writeString : getFilesAllName(mDBPath)) {
                    extOutputStream.writeString(writeString);
                }
                extOutputStream.close();
                fileOutputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("addFaceTxt", "FileNotFoundException======。");
            e.printStackTrace();
        } catch (IOException e2) {
            Log.e("addFaceTxt", "IOException======。");
            e2.printStackTrace();
        }
    }
}
