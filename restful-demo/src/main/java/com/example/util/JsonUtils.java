package com.example.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * JsonUtils
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-06 23:10:10
 */
@Slf4j
public class JsonUtils {
    private static ObjectMapper MAPPER = new ObjectMapper();

    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {
        // 对象所有字段全部列出
        MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // 取消默认 date 转换为 timestamp
        MAPPER.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        // 忽略空 bean 转 json 的错误
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 所有日期格式都统一为以下格式：yyyy-MM-dd HH:mm:ss
        MAPPER.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));
        // 忽略在 json 字符串存在，但是 java bean 对象不存在的情况
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtils() {
    }

    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return object instanceof String ? object.toString() : MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Convert toJson error: {}", e.getMessage());
            return null;
        }
    }

    public static String toJsonEmpty(Object object) {
        if (object == null) {
            return null;
        }
        try {
            MAPPER.getSerializerProvider().setNullValueSerializer(new JsonSerializer<>() {
                @Override
                public void serialize(Object object, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                    jsonGenerator.writeString("");
                }
            });
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Convert toJsonEmpty error: {}", e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        if (!StringUtils.hasText(json) || typeReference == null) {
            return null;
        }
        try {
            return typeReference.getType().equals(String.class) ? (T) json : MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Convert toObject error: {}", e.getMessage());
        }
        return null;
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        if (!StringUtils.hasText(json) || clazz == null) {
            return null;
        }
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Convert toObject error: {}", e.getMessage());
        }
        return null;
    }

    public static <K, V> Map<K, V> toMap(String json) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        return toObject(json, new TypeReference<>() {
        });
    }

    public static <T> List<T> toList(String json) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        return toObject(json, new TypeReference<>() {
        });
    }


}
