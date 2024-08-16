package io.github.thebesteric.framework.agile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.thebesteric.framework.agile.domain.PermissionVO;
import io.github.thebesteric.framework.agile.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("select p.* from t_permission as p, r_role_permission as rp where p.id = rp.permission_id and rp.role_id = #{roleId}")
    List<PermissionVO> selectPermissionByRoleId(@Param("roleId") Long roleId);

}
