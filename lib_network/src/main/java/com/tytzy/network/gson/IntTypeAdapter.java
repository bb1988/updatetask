package com.tytzy.network.gson;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * 作者: 白勃
 * 时间: 2020/5/20 4:14 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: int类型转化，把null值或者空值转化为0，true值为1，false值为0
 * @param
 */
public class IntTypeAdapter extends TypeAdapter<Integer> {

    @Override
    public Integer read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return 0;
        }

        if (reader.peek() == JsonToken.BOOLEAN) {
            return reader.nextBoolean() ? 1 : 0;
        }

        if (reader.peek() == JsonToken.STRING) {
            return queryString(reader.nextString());
        }

        return (int)reader.nextDouble();
    }

    @Override
    public void write(JsonWriter writer, Integer value) throws IOException {

        if (value == null) {
            writer.nullValue();
            return;
        }

        writer.value((long) value);
    }


    private int queryString(String data) {
        if (TextUtils.isEmpty(data)) {
            return 0;
        } else {
            return Integer.parseInt(data);
        }
    }

}