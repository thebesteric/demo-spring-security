package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Role
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-25 17:54:14
 */
@Data
@TableName("t_role")
@JsonIgnoreProperties("handler")
public class Role implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField
    private String name;

    @TableField
    private String tag;

    @TableField(exist = false)
    private List<Permission> permissions;
}
