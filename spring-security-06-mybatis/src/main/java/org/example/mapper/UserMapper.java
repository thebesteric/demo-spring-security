package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.example.domain.UserVO;
import org.example.entity.User;

import java.util.List;

/**
 * UserMapper
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-25 17:19:08
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 通过中间表进行一对多、多对多查询（三表）：https://blog.csdn.net/qq_33811336/article/details/125639591
    @Select("select * from t_user where id = #{id}")
    @Results(id = "selectUser", value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "roles", column = "id", javaType = List.class,
                    many = @Many(select = "org.example.mapper.RoleMapper.selectRoleByUserId", fetchType = FetchType.LAZY))
    })
    UserVO selectUserById(@Param("id") Long id);

    @Select("select * from t_user where username = #{username}")
    @ResultMap("selectUser")
    UserVO selectUserByUsername(@Param("username") String username);


}
