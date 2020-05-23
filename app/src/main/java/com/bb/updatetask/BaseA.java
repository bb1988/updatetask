package com.bb.updatetask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BaseA {
    private static int a = 0;

    public static String methodA(File file) throws IOException {
        if (!file.exists()) {
            return "Error";
        }

        InputStream fileInputStream = new FileInputStream(file);
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            int read = fileInputStream.read();
            a = read;
            if (read > -1) {
                stringBuffer.append((char) (a ^ 153));
            } else {
                fileInputStream.close();
                return new String(stringBuffer);
            }
        }
    }

    public static void methodA(String str, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        OutputStream fileOutputStream = new FileOutputStream(file);
        for (int i = 0; i < str.length(); i++) {
            fileOutputStream.write((char) (str.charAt(i) ^ 153));
        }
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
