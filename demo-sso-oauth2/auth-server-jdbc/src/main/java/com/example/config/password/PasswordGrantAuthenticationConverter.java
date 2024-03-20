package com.example.config.password;

import com.example.config.password.constant.OAuth2Constant;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * PasswordGrantAuthenticationConverter
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-03-20 09:46:14
 */
@Component
public class PasswordGrantAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        // 只处理 authorization_password 类型的认证
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!OAuth2Constant.GRANT_TYPE_PASSWORD.equals(grantType)) {
            return null;
        }

        // 从上下文取出认证信息
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        // 从请求中提取请求参数
        MultiValueMap<String, String> parameters = getParameters(request);

        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) || parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            throw new OAuth2AuthenticationException("无效请求，用户名不能为空");
        }
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) || parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            throw new OAuth2AuthenticationException("无效请求，密码不能为空");
        }

        // 收集要传入 PasswordGrantAuthenticationToken 构造方法的参数，
        // 该参数接下来在 PasswordGrantAuthenticationProvider 中使用
        Map<String, Object> additionalParameters = new HashMap<>();
        // 遍历从 request 中提取的参数，排除掉 grant_type、client_id、code 等字段参数，其他参数收集到 additionalParameters 中
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) && !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.CODE)) {
                additionalParameters.put(key, value.get(0));
            }
        });

        // 返回自定义的 PasswordGrantAuthenticationToken 对象
        return new PasswordGrantAuthenticationToken(clientPrincipal, additionalParameters);
    }

    private static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }
        });
        return parameters;
    }
}
