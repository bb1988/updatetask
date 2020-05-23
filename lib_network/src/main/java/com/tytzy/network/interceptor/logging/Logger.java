package com.tytzy.network.interceptor.logging;

import okhttp3.internal.platform.Platform;

/**
 * @Author: Baibo
 * @Date: 2020/5/20 1:52 PM
 * @param
 * @Description:
 */
@SuppressWarnings({"unused"})
public interface Logger {
    void log(int level, String tag, String msg);

    Logger DEFAULT = new Logger() {
        @Override
        public void log(int level, String tag, String message) {
            Platform.get().log(message, level, null);
        }
    };
}
