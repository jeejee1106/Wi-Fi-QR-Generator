package com.example.project.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QrImageGenerator {

    public static String generatePng(String qrContent, String outputDir, String fileNameWithoutExt) {
        int width = 300;
        int height = 300;

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, width, height);

            // 디렉토리 + 파일명
            String filePath = outputDir + "/" + fileNameWithoutExt + ".png";
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            return filePath; // DB에 이 경로 저장
        } catch (WriterException | IOException e) {
            throw new RuntimeException("QR 코드 이미지 생성 실패", e);
        }
    }

}
