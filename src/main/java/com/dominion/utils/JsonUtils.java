package com.dominion.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtils {

    public static final ObjectMapper jsonMapper = new ObjectMapper();

    public static <T> T readJsonObject(InputStream inputStream, Class<T> objectType) throws IOException {
        return jsonMapper.readValue(inputStream, objectType);
    }

    public static <T> String writeJsonObjectToString(T javaObject) throws IOException {
        return jsonMapper.writeValueAsString(javaObject);
    }

    public static ObjectMapper getMapper() {
        return jsonMapper;
    }

    public static <T> String writeJsonArray(List<T> javaArray) {
        final StringBuilder s = new StringBuilder(100);
        s.append("[");
        for (T element : javaArray) {
            try {
                s.append(writeJsonObjectToString(element));
                s.append(",");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (s.length() > 1) {
            s.deleteCharAt(s.length() - 1);
        }
        s.append("]");
        return s.toString();
    }

}
