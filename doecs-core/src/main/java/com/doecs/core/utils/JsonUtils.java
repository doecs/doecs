package com.doecs.core.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String bean2Json(T bean) {
        try {
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            return objectMapper.writeValueAsString(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String map2Json(Map map) {
        try {
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String list2Json(List list) {
        try {
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static <T> T json2Bean(String json, Class<T> beanClass) {
        try {
            return objectMapper.readValue(json, beanClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> json2List(String json, Class<T> beanClass) {
        try {
            JavaType javaType = getCollectionType(List.class, beanClass);
            return (List<T>)objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            ExceptionUtils.handle(e);
        }
        return null;
    }

    public static Map json2Map(String json) {
        try {

            return (Map)objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
