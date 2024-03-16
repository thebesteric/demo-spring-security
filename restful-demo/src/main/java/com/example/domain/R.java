package com.example.domain;

import lombok.Data;
import lombok.Getter;

/**
 * R
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-07 00:03:13
 */
@Data
public class R<T> {
    private Integer code;
    private String message;
    private T data;

    private R(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> R<T> success() {
        return success(DefaultCode.SUCCESS.code, DefaultCode.SUCCESS.message);
    }

    public static <T> R<T> success(T data) {
        return success(DefaultCode.SUCCESS.code, DefaultCode.SUCCESS.message, data);
    }

    public static <T> R<T> success(int code, String message) {
        return success(code, message, null);
    }

    public static <T> R<T> success(int code, String message, T data) {
        return new R<>(code, message, data);
    }

    public static <T> R<T> error() {
        return error(DefaultCode.ERROR.code, DefaultCode.ERROR.message);
    }

    public static <T> R<T> error(T data) {
        return error(DefaultCode.ERROR.code, DefaultCode.ERROR.message, data);
    }

    public static <T> R<T> error(int code, String message) {
        return error(code, message, null);
    }

    public static <T> R<T> error(int code, String message, T data) {
        return new R<>(code, message, data);
    }


    @Getter
    public enum DefaultCode {
        SUCCESS(200, "success"), ERROR(201, "error");

        private final Integer code;
        private final String message;

        DefaultCode(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
