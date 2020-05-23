package com.tytzy.network.gson;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * 作者: 白勃
 * 时间: 2020/5/20 4:00 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: 双精度浮点数类型转换，null值或者空值转化为0.0,布尔值 true是1.0 false是0.0
 * @param
 */
public class DoubleTypeAdapter extends TypeAdapter<Double> {

    @Override
    public Double read(JsonReader reader) throws IOException {

        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return 0.0;
        }

        if (reader.peek() == JsonToken.BOOLEAN) {
            return reader.nextBoolean() ? 1.0 : 0.0;
        }

        if (reader.peek() == JsonToken.STRING) {
            return queryString(reader.nextString());
        }

        return reader.nextDouble();
    }

    @Override
    public void write(JsonWriter writer, Double value) throws IOException {

        if (value == null) {
            writer.nullValue();
            return;
        }

        writer.value(value);
    }


    private double queryString(String data) {
        if (TextUtils.isEmpty(data)) {
            return 0.0;
        } else {
            return Double.parseDouble(data);
        }

    }

}