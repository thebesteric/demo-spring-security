package org.example.domain;

import lombok.Data;

import java.util.List;

/**
 * RoleVO
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-26 15:10:05
 */
@Data
public class RoleVO {
    private Long id;
    private String name;
    private String tag;
    private List<PermissionVO> permissions;
}
