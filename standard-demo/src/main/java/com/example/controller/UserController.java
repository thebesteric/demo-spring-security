package com.example.controller;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-05 23:44:28
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 要求：必须有超级管理员角色，才可以访问
     * 注解 @Secured，基于角色的权限管理，要求当前登录用户权限列表中，必须有注解参数角色
     * 可以写多个角色，类似 hanAnyRole 的权限判断
     * 权限管理力度比较粗，一般很少时使用
     */
    // @Secured({"ROLE_admin", "ROLE_user"}) == @RolesAllowed({"admin", "user"})
    @RolesAllowed({"admin", "user"})
    @GetMapping("/list")
    public String list() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        System.out.println("名称 = " + authentication.getName());
        System.out.println("权限 = " + authentication.getAuthorities());
        System.out.println("身份 = " + authentication.getPrincipal());
        System.out.println("凭证 = " + authentication.getCredentials());
        System.out.println("明细 = " + authentication.getDetails());
        return "用户查询列表";
    }

    /**
     * 要求：必须有权限：user:add
     * 注解 @PreAuthorize，在当前方法执行前，判断登录用户在权限列表中是否符合注解参数表达式的要求
     * 注解参数表达式的可选结构：hasAuthority、hasAnyAuthority、hasRole、hasAnyRole
     */
    // @PreAuthorize("hasAuthority('user:add')")
    @PreAuthorize("hasAuthority('user:add')")
    @GetMapping("/add")
    public String add() {
        System.out.println("add 方法执行");
        return "用户添加";
    }

    /**
     * 要求：必须有权限：user:update
     * 注解 @PostAuthorize，在当前方法执行后，判断登录用户在权限列表中是否符合注解参数表达式的要求
     * 注解参数表达式的可选结构：hasAuthority、hasAnyAuthority、hasRole、hasAnyRole
     * 由于在方法执行后判断，几乎很少时使用
     */
    @PostAuthorize("hasAuthority('user:update')")
    @GetMapping("/update")
    public String update() {
        System.out.println("update 方法执行");
        return "用户修改";
    }

    // @PreAuthorize("hasRole('admin')") == @Secured({"ROLE_admin"}) == @RolesAllowed({"admin"})
    @PreAuthorize("hasRole('user') || hasAuthority('user:delete')")
    @GetMapping("/delete")
    public String delete() {
        System.out.println("delete 方法执行");
        return "用户删除";
    }

    @PermitAll
    @GetMapping("/allow")
    public String allow() {
        System.out.println("allow 方法执行");
        return "PermitAll";
    }

    @DenyAll
    @GetMapping("/deny")
    public String deny() {
        System.out.println("deny 方法执行");
        return "DenyAll";
    }

}
