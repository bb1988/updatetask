package com.tytzy.network.gson;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * 作者: 白勃
 * 时间: 2020/5/20 3:56 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: 布尔值GSon转化辅助类，null值或空字符串转化为false,数字非0转化为true,0值为false
 *
 * @param
 */
public class BooleanTypeAdapter extends TypeAdapter<Boolean> {

    @Override
    public Boolean read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return false;
        }

        if (reader.peek() == JsonToken.NUMBER) {
            return reader.nextInt() != 0;
        }

        if (reader.peek() == JsonToken.STRING) {
            return queryString(reader.nextString());
        }

        return reader.nextBoolean();
    }

    @Override
    public void write(JsonWriter writer, Boolean value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value);
    }


    private boolean queryString(String data) {
        if (TextUtils.isEmpty(data)) {
            return false;
        } else {
            return Boolean.parseBoolean(data);
        }

    }

}