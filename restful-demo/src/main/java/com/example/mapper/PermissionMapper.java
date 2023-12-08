package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("select p.* from t_permission as p, r_role_permission as rp " +
            "where p.id = rp.permission_id and rp.role_id = #{role_id}")
    List<Permission> selectPermissionByRoleId(String roleId);

    @Select("select distinct p.* from t_permission as p, r_role_permission as rp, r_user_role ur " +
            "where p.id = rp.permission_id and rp.role_id = ur.role_id and ur.user_id = #{userId}")
    List<Permission> selectPermissionByUserId(Long userId);

}
