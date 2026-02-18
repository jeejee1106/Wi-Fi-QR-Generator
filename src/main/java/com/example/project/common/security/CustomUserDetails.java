package com.example.project.common.security;

import com.example.project.auth.domain.Auth;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails{

    private final Auth auth;

    public CustomUserDetails(Auth auth) { this.auth = auth; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(auth.getRole()));
    }

    @Override
    public String getPassword() { return auth.getPassword(); }

    @Override
    public String getUsername() { return auth.getEmail(); }

    public Long getUserSeq() { return auth.getUserSeq(); }


    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return "N".equals(auth.getDelYn()); }

}
