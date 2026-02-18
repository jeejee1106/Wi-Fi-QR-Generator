package com.example.project.auth.service;

import com.example.project.common.exception.BusinessException;
import com.example.project.common.exception.ErrorCode;
import com.example.project.auth.dto.request.SignInReq;
import com.example.project.auth.dto.response.SignInRes;
import com.example.project.auth.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SignInRes userJoin(SignInReq req) {

        //1. 이메일 중복 체크
        int count = authMapper.existsByEmail(req.getEmail());
        if (count > 0) {
            throw new BusinessException(ErrorCode.USER_EMAIL_DUPLICATED); //409
        }

        //2. 비밀번호 암호화
        String encodedPw = passwordEncoder.encode(req.getPassword());
        req.setPassword(encodedPw);

        //3. db 저장
        try {
            authMapper.insertUser(req);
        } catch (DuplicateKeyException e) {
            // // race condition 등으로 인해 동시에 insert 시도된 경우
            log.warn("Duplicate email detected on insert. email={}", req.getEmail(), e);
            throw new BusinessException(ErrorCode.USER_EMAIL_DUPLICATED); //409
        }

        return SignInRes.of(req);
    }
}
