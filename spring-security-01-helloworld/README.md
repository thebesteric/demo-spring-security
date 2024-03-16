# 登陆
SpringSecurity 已经自带了一个过滤器，拦截是否登陆，没有的话会跳转到登陆 login 页面
- /index => /login => /index
# 自定义登陆账号和密码
```yaml
spring:
  security:
    user:
      name: admin
      password: 123456
```