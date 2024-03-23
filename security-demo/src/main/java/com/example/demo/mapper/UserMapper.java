package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * UserMapper
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-01 00:20:13
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from t_user where username = #{username}")
    @Results(id = "selectUserByUsername", value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "roles", column = "id", javaType = List.class,
                    many = @Many(select = "com.example.demo.mapper.RoleMapper.selectRoleByUserId", fetchType = FetchType.LAZY))
    })
    User selectUserByUsername(String username);

}
