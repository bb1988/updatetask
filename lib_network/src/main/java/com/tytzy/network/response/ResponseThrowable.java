package com.tytzy.network.response;

/**
 * 作者: 白勃
 * 时间: 2020/5/20 4:43 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: 自定义的异常包装类
 * @param
 */
public class ResponseThrowable extends Exception {

    public int code;
    public String message;

    public ResponseThrowable(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }
}
