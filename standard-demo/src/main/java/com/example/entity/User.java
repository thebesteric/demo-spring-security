package com.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

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
public class User {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField
    private String name;

    @TableField
    private String username;

    @TableField
    private String password;

    @TableLogic
    private boolean enabled;

    @TableField(exist = false)
    private List<Role> roles;
}
