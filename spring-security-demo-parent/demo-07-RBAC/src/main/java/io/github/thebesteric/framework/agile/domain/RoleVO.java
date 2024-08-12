package io.github.thebesteric.framework.agile.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * RoleVO
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-26 15:10:05
 */
@Data
@JsonIgnoreProperties(value = "handler")
public class RoleVO implements Serializable {
    private Long id;
    private String name;
    private String tag;
    private List<PermissionVO> permissions = new ArrayList<>();
}
