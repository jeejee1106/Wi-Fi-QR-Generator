package com.example.project.common.security;

import com.example.project.auth.domain.Auth;
import com.example.project.auth.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthMapper authMapper;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Auth auth = authMapper.findByEmail(email);
        if (auth == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
        return new CustomUserDetails(auth);
    }
}