package test;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;

import java.util.HashMap;
import java.util.Map;

public class SpringSecurityJwtTests {

    private static final String SECRET_KEY = "123456";

    @Test
    public void test() {
        // 创建 token
        Map<String, Object> claums = new HashMap<>();
        claums.put("username", "eric");
        claums.put("mobile", "139xxxxxxxx");
        claums.put("expires_in", 60 * 1000); // 设置过期时间，可用于过期校验
        MacSigner rsaSigner = new MacSigner(SECRET_KEY);
        Jwt encode = JwtHelper.encode(JSON.toJSONString(claums), rsaSigner);
        String token = encode.getEncoded();
        System.out.println(token);

        // 解析 token
        Jwt decode = JwtHelper.decode(token);
        System.out.println(decode.getClaims());
    }

}
