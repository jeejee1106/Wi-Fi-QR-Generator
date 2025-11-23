package com.example.project.common.security.crypto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@RequiredArgsConstructor
@Component
public class WifiPasswordEncryptor {

    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH_BIT = 128;   // 16바이트
    private static final int IV_LENGTH_BYTE = 12;        // GCM 권장 12바이트

    private final SecretKeySpec wifiSecretKeySpec;
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * 평문 비밀번호 -> 암호문(Base64 문자열)
     */
    public String encrypt(String plainText) {
        if (plainText == null) {
            return null;
        }

        try {
            byte[] iv = new byte[IV_LENGTH_BYTE];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.ENCRYPT_MODE, wifiSecretKeySpec, parameterSpec);

            byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF-8"));

            // 최종 저장은 (IV + 암호문) 를 합쳐서 Base64 인코딩
            byte[] ivAndCipher = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, ivAndCipher, 0, iv.length);
            System.arraycopy(cipherText, 0, ivAndCipher, iv.length, cipherText.length);

            return Base64.getEncoder().encodeToString(ivAndCipher);
        } catch (Exception e) {
            throw new IllegalStateException("WiFi 비밀번호 암호화에 실패했습니다.", e);
        }
    }

    /**
     * 암호문(Base64 문자열) -> 평문 비밀번호
     */
    public String decrypt(String cipherTextBase64) {
        if (cipherTextBase64 == null) {
            return null;
        }

        try {
            byte[] ivAndCipher = Base64.getDecoder().decode(cipherTextBase64);

            byte[] iv = new byte[IV_LENGTH_BYTE];
            byte[] cipherText = new byte[ivAndCipher.length - IV_LENGTH_BYTE];

            System.arraycopy(ivAndCipher, 0, iv, 0, IV_LENGTH_BYTE);
            System.arraycopy(ivAndCipher, IV_LENGTH_BYTE, cipherText, 0, cipherText.length);

            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.DECRYPT_MODE, wifiSecretKeySpec, parameterSpec);

            byte[] plainBytes = cipher.doFinal(cipherText);
            return new String(plainBytes, "UTF-8");
        } catch (Exception e) {
            throw new IllegalStateException("WiFi 비밀번호 복호화에 실패했습니다.", e);
        }
    }

    /**
     * 요약:
     * encrypt()
     * 12바이트 IV 랜덤 생성
     * AES/GCM 으로 암호화
     * IV + 암호문 바이트 배열을 합쳐서 Base64 문자열로 저장

     * decrypt()
     * Base64 디코딩 → 앞 12바이트는 IV, 나머지는 암호문
     * 동일한 키로 복호화
     */
}
