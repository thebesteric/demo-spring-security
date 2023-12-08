package com.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * User
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-26 16:01:07
 */
@Data
@TableName("t_user")
@JsonIgnoreProperties("handler")
public class User implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField
    private String name;

    @TableField
    private String username;

    @TableField
    private String password;

    @TableField
    private boolean admin;

    @TableLogic
    private boolean enabled;

    @TableField(exist = false)
    private List<Role> roles;
}
