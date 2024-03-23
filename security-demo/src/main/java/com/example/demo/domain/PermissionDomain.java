package com.example.demo.domain;

import com.example.demo.entity.Permission;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * PermissionDomain
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-07 18:11:41
 */
@Data
@NoArgsConstructor
public class PermissionDomain implements Serializable {
    private Long id;
    private String type;
    private String name;
    private String url;
    private String parentId;
    private String tag;

    public PermissionDomain(Permission permission) {
        this.id = permission.getId();
        this.type = permission.getType();
        this.name = permission.getName();
        this.url = permission.getUrl();
        this.parentId = permission.getParentId();
        this.tag = permission.getTag();
    }
}
