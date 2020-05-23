package com.tytzy.network.response;

/**
 * 作者: 白勃
 * 时间: 2020/5/20 2:17 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: 网络请求返回
 * @param
 */
public class BaseResponse<E> {

    private int code;
    private String message;
    private E data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
