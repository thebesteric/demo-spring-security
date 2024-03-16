package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.domain.PermissionVO;
import org.example.entity.Permission;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission>  {

    @Select("select p.* from t_permission as p, r_role_permission as rp where p.id = rp.permission_id and rp.role_id = #{role_id}")
    List<PermissionVO> selectPermissionByRoleId(String roleId);

}
