package com.example.demo.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * MyUserDetails
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-07 02:18:15
 */
@Data
public class MyUserDetails implements UserDetails {

    private UserDomain userDomain;
    private List<GrantedAuthority> authorities;

    public MyUserDetails(UserDomain userDomain) {
        this.userDomain = userDomain;
        this.authorities = userDomain.getAuthorities();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.userDomain.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userDomain.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}