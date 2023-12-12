package com.unitral.microdoc.config;

import com.unitral.microdoc.entity.UserAuthentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUser implements UserDetails {

    UserAuthentication userAuthentication;

    public CustomUser(UserAuthentication user) {
        userAuthentication=user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(userAuthentication.getRole()));
    }

    @Override
    public String getPassword() {
        return userAuthentication.getPassword();
    }

    @Override
    public String getUsername() {
        return userAuthentication.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userAuthentication.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userAuthentication.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userAuthentication.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return userAuthentication.isEnabled();
    }
}
