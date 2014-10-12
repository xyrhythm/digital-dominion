package com.dominion.utils;

import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtils {

    public static final ObjectMapper jsonMapper = new ObjectMapper();

    public static <T> T readJsonObject(InputStream inputStream, Class<T> objectType) throws IOException {
        return jsonMapper.readValue(inputStream, objectType);
    }

    public static <T> String writeJsonObjectToString(T javaObject) throws IOException {
        return jsonMapper.writeValueAsString(javaObject);
    }

}
