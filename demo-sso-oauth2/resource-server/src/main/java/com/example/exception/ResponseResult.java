package com.example.exception;

import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Data
public class ResponseResult<T> {

    private Integer code;
    private String message;
    private T data;

    private ResponseResult() {
    }

    public static <T> ResponseResult<T> build(T body, ResultCodeEnum resultCodeEnum) {
        ResponseResult<T> result = new ResponseResult<>();
        // 封装数据
        if (body != null) {
            result.setData(body);
        }
        // 状态码
        result.setCode(resultCodeEnum.getCode());
        // 返回信息
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }


    public static <T> ResponseResult<T> ok() {
        return build(null, ResultCodeEnum.SUCCESS);
    }


    public static <T> ResponseResult<T> ok(T data) {
        return build(data, ResultCodeEnum.SUCCESS);
    }


    public static <T> ResponseResult<T> fail() {
        return build(null, ResultCodeEnum.FAIL);
    }


    public static <T> ResponseResult<T> fail(T data) {
        return build(data, ResultCodeEnum.FAIL);
    }

    public ResponseResult<T> message(String msg) {
        this.setMessage(msg);
        return this;
    }

    public ResponseResult<T> code(Integer code) {
        this.setCode(code);
        return this;
    }


    public static void exceptionResponse(HttpServletResponse response, Exception e) throws AccessDeniedException, AuthenticationException, IOException {
        String message = null;
        if (e instanceof OAuth2AuthenticationException o) {
            message = o.getError().getDescription();
        } else {
            message = e.getMessage();
        }
        exceptionResponse(response, message);
    }


    public static void exceptionResponse(HttpServletResponse response, String message) throws AccessDeniedException, AuthenticationException, IOException {

        ResponseResult responseResult = ResponseResult.fail(message);
        Gson gson = new Gson();
        String jsonResult = gson.toJson(responseResult);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print(jsonResult);
    }
}