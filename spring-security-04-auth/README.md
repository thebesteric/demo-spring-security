# 认证和授权
## 授权
1. 角色
2. 权限
角色和权限在 SpringSecurity 中都是一样的，只是在业务中做了区分，他们都是我们指定的标志 \

/admin/user/save
- 角色：admin，即拥有 admin 角色的可以访问
- 权限：user:save，即拥有 user:save 权限的用户可以操作

/admin/user/query
- 角色：admin，即拥有 user 角色的可以访问
- 权限：user:query，即拥有 user:query 权限的用户可以操作

在实际的应用中通常，菜单通过角色控制，通过权限通过接口的访问

# 案例
/admin/api：需要 admin 角色访问
/user/api：需要 user 角色访问
/app/api：匿名可以访问
1. 配置角色、权限
2. 将用户存储到内存
现在内容中配置用户和权限，实际开发中是配置到数据库中的
```java
@Bean
public InMemoryUserDetailsManager inMemoryUserDetailsManager(SecurityProperties properties, ObjectProvider<PasswordEncoder> passwordEncoder) {
    SecurityProperties.User user = properties.getUser();
    List<String> roles = user.getRoles();
    return new InMemoryUserDetailsManager(new UserDetails[]{User.withUsername(user.getName()).password(this.getOrDeducePassword(user, (PasswordEncoder) passwordEncoder.getIfAvailable())).roles(StringUtils.toStringArray(roles)).build()});
}
```
# 分析角色和权限到区别
```java
public static <T> AuthorityAuthorizationManager<T> hasAnyRole(String... roles) {
        return hasAnyRole("ROLE_", roles);
    }

public static <T> AuthorityAuthorizationManager<T> hasAnyRole(String rolePrefix, String[] roles) {
    Assert.notNull(rolePrefix, "rolePrefix cannot be null");
    Assert.notEmpty(roles, "roles cannot be empty");
    Assert.noNullElements(roles, "roles cannot contain null values");
    return hasAnyAuthority(toNamedRolesArray(rolePrefix, roles));
}

public static <T> AuthorityAuthorizationManager<T> hasAnyAuthority(String... authorities) {
    Assert.notEmpty(authorities, "authorities cannot be empty");
    Assert.noNullElements(authorities, "authorities cannot contain null values");
    return new AuthorityAuthorizationManager(authorities);
}
```
角色判断实际上就是权限判断，只是角色加了一个 **ROLE_** 的前缀，