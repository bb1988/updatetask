package com.tytzy.network.gson;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 作者: 白勃
 * 时间: 2020/5/20 4:12 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: Gson转化类拓展，注册自定义的转化type，对一些异常值进行转化
 * @param
 */
public class GsonSerializer {

    public static volatile GsonSerializer mInstance;

    public static Gson mGSon;

    public static GsonSerializer get() {
        if (mInstance == null) {
            synchronized (GsonSerializer.class) {
                if (mInstance == null)
                    mInstance = new GsonSerializer();
            }
        }
        return mInstance;
    }

    public GsonSerializer() {
        mGSon = new GsonBuilder()
                .registerTypeAdapterFactory(new GsonAdapterFactory())
                .create();
    }

    public Gson getGson() {
        return mGSon;
    }


    public class GsonAdapterFactory<T> implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType == String.class) {
                return (TypeAdapter<T>) new StringTypeAdapter();
            }
            if (rawType == Long.class) {
                return (TypeAdapter<T>) new LongTypeAdapter();
            }
            if (rawType == long.class) {
                return (TypeAdapter<T>) new LongTypeAdapter();
            }
            if (rawType == Integer.class) {
                return (TypeAdapter<T>) new IntTypeAdapter();
            }
            if (rawType == int.class) {
                return (TypeAdapter<T>) new IntTypeAdapter();
            }
            if (rawType == Double.class) {
                return (TypeAdapter<T>) new DoubleTypeAdapter();
            }
            if (rawType == double.class) {
                return (TypeAdapter<T>) new DoubleTypeAdapter();
            }
            if (rawType == Boolean.class) {
                return (TypeAdapter<T>) new BooleanTypeAdapter();
            }
            if (rawType == boolean.class) {
                return (TypeAdapter<T>) new BooleanTypeAdapter();
            }

            return null;
        }
    }


    public String toJson(Map object) {
        return mGSon.toJson(object);
    }

    public String toJson(Object object) {
        if (object == null) {
            return "";
        } else if (object instanceof String && TextUtils.isEmpty((String) object)) {
            return "";
        }
        return mGSon.toJson(object);
    }

    public <T> Object fromJsonT(String json, Class<T> cls) {
        return mGSon.fromJson(json, cls);
    }

    public Object fromJson(String json, Class<?> cls) {
        return mGSon.fromJson(json, cls);
    }

    public <T> T fromJsonSafe(String json, Type typeOfT) {
        try {
            return mGSon.fromJson(json, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T fromJson(String json, Type type) {
        return mGSon.fromJson(json, type);
    }


}
