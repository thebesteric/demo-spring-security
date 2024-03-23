package com.example.demo.domain;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * UserDomain
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-07 18:10:29
 */
@Data
public class UserDomain implements Serializable {
    private Long id;
    private String name;
    private String username;
    private String password;
    private boolean admin;
    private boolean enabled;
    private List<RoleDomain> roles;

    public UserDomain(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.password = user.getPassword();

        this.roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            RoleDomain roleDomain = new RoleDomain(role);
            this.roles.add(roleDomain);
        }
    }

    @JsonIgnore
    public List<GrantedAuthority> getAuthorities() {
        // 查找所有角色标签
        List<String> roleTags = this.getRoles().stream().map(role -> {
            String tag = role.getTag();
            if (!role.getTag().startsWith("ROLE_")) {
                tag = "ROLE_" + tag;
            }
            return tag;
        }).toList();

        // 查找用户所有权限标签
        List<String> permissionTags = this.getRoles().stream().flatMap(role -> role.getPermissions().stream()).map(PermissionDomain::getTag).toList();

        // 合并角色和权限数据
        List<String> roleAndPermissions = Stream.concat(roleTags.stream(), permissionTags.stream()).toList();

        // 利用 AuthorityUtils 将权限转换为 GrantedAuthority
        return AuthorityUtils.createAuthorityList(roleAndPermissions);
    }
}
