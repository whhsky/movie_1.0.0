package com.movie.secutity.domain;

import com.user.domain.entity.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.*;


public class RedisUser implements UserDetails {

    private SysUser sysUser;

    private  Set<GrantedAuthority> authorities;

    private  boolean accountNonExpired;

    private  boolean accountNonLocked;

    private  boolean credentialsNonExpired;

    private  boolean enabled;

    private String username;
    private String password;

    public RedisUser() {
    }

    public RedisUser(SysUser sysUser, Collection<? extends GrantedAuthority> authorities) {
        this(sysUser, true, true, true, true, authorities);
    }

    public RedisUser(SysUser sysUser, boolean enabled, boolean accountNonExpired,
                boolean credentialsNonExpired, boolean accountNonLocked,
                Collection<? extends GrantedAuthority> authorities) {
        Assert.isTrue(sysUser != null,
                "Cannot pass null or empty values to constructor");
        this.sysUser = sysUser;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = Collections.unmodifiableSet((Set<? extends GrantedAuthority>) authorities);
    }

    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUser.getUserName();
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
