package com.document.module.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class GsonUtils {
    private GsonUtils() {
    }

    private static final Gson COMMON_GSON;
    private static final GsonBuilder COMMON_GSON_BUILDER;

    static {
        COMMON_GSON_BUILDER = new GsonBuilder();
//                .registerTypeAdapter(int.class, new IntegerTypeAdapter())
//                .registerTypeAdapter(double.class, new DoubleTypeAdapter())
//                .registerTypeAdapter(long.class, new LongTypeAdapter())
//                .registerTypeAdapter(float.class, new FloatTypeAdapter())
//                .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
//                .registerTypeAdapter(Long.class, new LongTypeAdapter())
//                .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
//                .registerTypeAdapter(Float.class, new FloatTypeAdapter())
//                .registerTypeAdapter(BigDecimal.class, new BigDecimalTypeAdapter());

        COMMON_GSON = COMMON_GSON_BUILDER
                .disableJdkUnsafe()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json.isEmpty()) {
            return null;
        }
        return COMMON_GSON.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        if (json.isEmpty()) {
            return null;
        }
        return COMMON_GSON.fromJson(json, typeOfT);
    }

    public static String toJson(Object jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        return COMMON_GSON.toJson(jsonObject);
    }
}
