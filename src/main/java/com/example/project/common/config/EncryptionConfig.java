package com.example.project.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class EncryptionConfig {

    @Value("${wifi.encryption.secret}")
    private String secretKey;

    @Bean
    public SecretKeySpec wifiSecretKeySpec() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        // 256비트 키 기준 (32바이트)
        return new SecretKeySpec(keyBytes, "AES");
    }
}
