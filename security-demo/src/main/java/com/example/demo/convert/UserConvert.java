package com.example.demo.convert;

import com.example.demo.domain.PermissionDomain;
import com.example.demo.domain.RoleDomain;
import com.example.demo.domain.UserDomain;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserConvert {

    public static final UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    @Mappings({@Mapping(target = "authorities", ignore = true)})
    public abstract UserDomain toUserDomain(User user);

    public abstract RoleDomain toRoleDomain(Role role);

    public abstract List<RoleDomain> toRoleDomains(List<Role> roles);

    public abstract PermissionDomain toPermissionDomain(Permission permission);

    public abstract List<PermissionDomain> toPermissionDomains(List<Permission> permissions);

}
