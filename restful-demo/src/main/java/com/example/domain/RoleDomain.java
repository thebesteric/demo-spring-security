package com.example.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * RoleDomain
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-07 18:11:18
 */
@Data
public class RoleDomain implements Serializable {
    private Long id;
    private String name;
    private String tag;
    private List<PermissionDomain> permissions;
}
