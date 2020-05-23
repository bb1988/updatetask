package com.tytzy.network.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * 作者: 白勃
 * 时间: 2020/5/20 4:16 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: 字符串类型转化,null转化为空值
 * @param
 */
public class StringTypeAdapter extends TypeAdapter<String> {

    @Override
    public String read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return "";
        }
        return reader.nextString();
    }

    @Override
    public void write(JsonWriter writer, String value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value);
    }
}

