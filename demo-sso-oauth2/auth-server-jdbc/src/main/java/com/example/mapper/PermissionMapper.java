package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.PermissionVO;
import com.example.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("select p.* from t_permission as p, r_role_permission as rp where p.id = rp.permission_id and rp.role_id = #{roleId}")
    List<PermissionVO> selectPermissionByRoleId(@Param("roleId") Long roleId);

}
