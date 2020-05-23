package com.tytzy.network.requestbody;


import com.tytzy.network.listener.UploadProgressListener;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 *  文件名：UploadProgressRequestBody
 *  版权：Copyright © 2016 RMD Inc. All Rights Reserved
 *  描述：上传ResponseBody类
 *  创建人：白勃
 *  创建时间：2016/9/27 16:53
 *  版本号：0.1
 */

public class UploadProgressRequestBody extends RequestBody {

    private RequestBody requestBody;
    private UploadProgressListener progressListener;
    private BufferedSink bufferedSink;

    public UploadProgressRequestBody(RequestBody requestBody,
                                     UploadProgressListener progressListener) {
        this.requestBody = requestBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return requestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            //包装
            bufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //回调上传接口
                progressListener.onProgress(bytesWritten, contentLength);
            }
        };
    }
}
