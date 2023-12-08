package com.example.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * PermissionDomain
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-07 18:11:41
 */
@Data
public class PermissionDomain implements Serializable {
    private Long id;
    private String type;
    private String name;
    private String url;
    private String parentId;
    private String tag;
}
