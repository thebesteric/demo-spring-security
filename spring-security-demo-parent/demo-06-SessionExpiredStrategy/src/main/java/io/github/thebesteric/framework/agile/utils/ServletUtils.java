package io.github.thebesteric.framework.agile.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * ServletUtils
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-06 23:06:01
 */
@Slf4j
public class ServletUtils {

    public static void renderString(HttpServletResponse response, Object object) {
        try {
            String json = JsonUtils.toJson(object);
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(json);
            response.getWriter().flush();
        } catch (IOException e) {
            log.error("响应数据发生异常：" + e.getMessage());
        }
    }

}
