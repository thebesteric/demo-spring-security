package io.github.thebesteric.framework.agile.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * PermissionVO
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-26 15:10:14
 */
@Data
@JsonIgnoreProperties(value = "handler")
public class PermissionVO implements Serializable {
    private Long id;
    private String name;
    private String tag;
}
