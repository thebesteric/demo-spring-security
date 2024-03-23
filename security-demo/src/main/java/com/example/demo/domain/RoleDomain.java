package com.example.demo.domain;

import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * RoleDomain
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-07 18:11:18
 */
@Data
@NoArgsConstructor
public class RoleDomain implements Serializable {
    private Long id;
    private String name;
    private String tag;
    private List<PermissionDomain> permissions;

    public RoleDomain(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.tag = role.getTag();
        this.permissions = new ArrayList<>();
        for (Permission permission : role.getPermissions()) {
            PermissionDomain permissionDomain = new PermissionDomain(permission);
            this.permissions.add(permissionDomain);
        }
    }
}
