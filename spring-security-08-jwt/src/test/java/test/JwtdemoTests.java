package test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.Base64Codec;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtdemoTests {

    // private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode("12345678901234567890123456789012345678901234567890"));

    @Test
    public void test() {
        // 创建一个JwtBuilder对象
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("name", "eric");

        String token = Jwts.builder()
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 60 * 1000))
                .signWith(SECRET_KEY, Jwts.SIG.HS256)
                .compact();
        System.out.println(token);

        // 三部分的base64解密
        System.out.println("=========");
        String[] split = token.split("\\.");
        System.out.println(Base64Codec.BASE64.decodeToString(split[0]));
        System.out.println(Base64Codec.BASE64.decodeToString(split[1]));
        // 无法解密
        System.out.println(Base64Codec.BASE64.decodeToString(split[2]));
    }

    /**
     * 解析token
     */
    @Test
    public void testParseToken() {
        // token
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiZXJpYyIsImlkIjoxLCJleHAiOjE3MTA2MDgxOTl9.6uCJJT0HdnGpawGxWIZmxg9RnhHrGBea27BgUjhda8g";
        // 解析token获取载荷中的声明对象
        Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        System.out.println(claims);
    }

}
