package com.example.security.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码解析器
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-01 13:11:59
 */
public class MyPasswordEncoder implements PasswordEncoder {

    /**
     * 加密
     *
     * @param rawPassword 密码明文
     * @return String
     * @author wangweijun
     * @since 2023/12/1 13:13
     */
    @Override
    public String encode(CharSequence rawPassword) {
        System.out.println("【自定义密码解析器】 - encode 执行加密");
        return rawPassword.toString();
    }

    /**
     * 密码明文是否与密码密文相同
     *
     * @param rawPassword     密码明文，是客户端传递过来的参数
     * @param encodedPassword 密码密文，是服务器存储的，一般保存在数据库中
     * @return boolean
     * @author wangweijun
     * @since 2023/12/1 13:14
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        System.out.println("【自定义密码解析器】 - matches 执行匹配");
        // 先加密，再对比
        boolean result = encode(rawPassword).equals(encodedPassword);
        if (result) {
            System.out.println("【自定义密码解析器】 - matches 匹配成功");
        }  else {
            System.out.println("【自定义密码解析器】 - matches 匹配失败");
        }
        return result;
    }

    /**
     * 是否需要升级密码解析策略，默认返回 false
     */
    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return PasswordEncoder.super.upgradeEncoding(encodedPassword);
    }
}
