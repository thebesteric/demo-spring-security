package io.github.thebesteric.framework.agile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.thebesteric.framework.agile.domain.RoleVO;
import io.github.thebesteric.framework.agile.entity.Role;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select r.* from t_role as r, r_user_role as ur where r.id = ur.role_id and ur.user_id = #{userId}")
    @Results(id = "selectRoleByUserId", value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "permissions", column = "id", javaType = List.class,
                    many = @Many(select = "io.github.thebesteric.framework.agile.mapper.PermissionMapper.selectPermissionByRoleId", fetchType = FetchType.LAZY))
    })
    List<RoleVO> selectRoleByUserId(@Param("userId") Long userId);

}
