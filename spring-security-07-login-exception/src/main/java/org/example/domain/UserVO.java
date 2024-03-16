package org.example.domain;

import lombok.Data;

import java.util.List;

/**
 * UserVO
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-26 15:08:29
 */
@Data
public class UserVO {
    private Long id;
    private String username;
    private String password;
    private List<RoleVO> roles;
    private boolean enabled;
}
