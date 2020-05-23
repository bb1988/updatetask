package com.tytzy.network.gson;

import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * 作者: 白勃
 * 时间: 2020/5/20 4:18 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: long类型转化，null值空值转化为0l,false转化为0l，true转化为1l
 * @param
 */
public class LongTypeAdapter extends TypeAdapter<Long> {

    @Override
    public Long read(JsonReader reader) throws IOException {

        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return 0L;
        }

        if (reader.peek() == JsonToken.STRING) {
            return queryString(reader.nextString());
        }

        try {
            return (long)reader.nextDouble();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }

    @Override
    public void write(JsonWriter writer, Long value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }

        writer.value(value.toString());
    }

    private long queryString(String data) {
        if (TextUtils.isEmpty(data)) {
            return 0L;
        } else {
            return getLong(data);
        }
    }

    public  long getLong(Object object) {
        try {
            if (object == null) {
                return 0L;
            } else if (object instanceof String) {
                return Long.parseLong(((String) object));
            } else if (object instanceof Boolean) {
                if ((boolean) object) {
                    return 1L;
                } else {
                    return 0L;
                }
            } else if (object instanceof Integer) {
                return (long) object;
            } else if (object instanceof Double) {
                return (long) object;
            } else if (object instanceof Float) {
                return (long) object;
            } else {
                return 0L;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

}

