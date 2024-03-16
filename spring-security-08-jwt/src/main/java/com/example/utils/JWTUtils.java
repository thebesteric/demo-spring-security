package com.example.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;

import java.util.Map;

public class JWTUtils {

    /**
     * 创建JWT
     *
     * @param secret
     * @param claims 创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
     *
     * @return
     */
    public static String getAccessToken(String secret, Map<String, Object> claims) {
        // 指定签名的时候使用的签名算法。
        MacSigner rsaSigner = new MacSigner(secret);
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(claims), rsaSigner);
        return jwt.getEncoded();
    }

    public static Map<String, Object> parseToken(String token) {
        Jwt jwt = JwtHelper.decode(token);
        return JSON.parseObject(jwt.getClaims());
    }

    /**
     * 根据传入的token过期时间判断token是否已过期
     *
     * @param expiresIn
     *
     * @return true-已过期，false-没有过期
     */
    public static boolean isExpiresIn(long expiresIn) {
        long now = System.currentTimeMillis();
        return now > expiresIn;
    }
}
