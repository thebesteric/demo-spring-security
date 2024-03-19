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
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField
    private String type;

    @TableField
    private String name;

    @TableField
    private String tag;
}
