package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Permission
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-25 17:55:04
 */
@Data
@TableName("t_permission")
public class Permission {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 权限类型：M-菜单、A-子菜单，U-请求 */
    @TableField
    private String type;

    @TableField
    private String name;

    @TableField
    private String url;

    @TableField
    private String parentId;

    /** 权限描述符 */
    @TableField
    private String tag;
}
