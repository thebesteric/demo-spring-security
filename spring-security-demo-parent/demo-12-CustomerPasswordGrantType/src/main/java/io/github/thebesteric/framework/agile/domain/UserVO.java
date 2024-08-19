package io.github.thebesteric.framework.agile.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * UserVO
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-26 15:08:29
 */
@Data
@JsonIgnoreProperties(value = "handler")
public class UserVO implements Serializable {
    private Long id;
    private String username;
    private String password;
    private List<RoleVO> roles = new ArrayList<>();
    private boolean enabled;
}
