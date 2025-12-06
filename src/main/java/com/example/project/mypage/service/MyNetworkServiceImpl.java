package com.example.project.mypage.service;

import com.example.project.common.exception.BusinessException;
import com.example.project.common.exception.ErrorCode;
import com.example.project.common.security.crypto.WifiPasswordEncryptor;
import com.example.project.mypage.dto.request.MyNetworkSearchCond;
import com.example.project.mypage.dto.request.UpdateMyNetworkReq;
import com.example.project.mypage.dto.response.MyNetworkDetailRes;
import com.example.project.mypage.dto.response.MyNetworkListRes;
import com.example.project.mypage.dto.response.MyNetworkRes;
import com.example.project.mypage.mapper.MyNetworkMapper;
import com.example.project.qrcode.dto.request.DeactivateQrCodeReq;
import com.example.project.qrcode.dto.request.QrCodeSearchCond;
import com.example.project.qrcode.mapper.QrCodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyNetworkServiceImpl implements MyNetworkService {

    private final MyNetworkMapper myNetworkMapper;
    private final QrCodeMapper qrCodeMapper;
    private final WifiPasswordEncryptor wifiPasswordEncryptor;

    @Override
    public MyNetworkListRes getMyNetworkList(MyNetworkSearchCond cond, Long userSeq){
        if (userSeq == null) {
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        List<MyNetworkRes> resultList = myNetworkMapper.getMyNetworkList(cond, userSeq);

        return MyNetworkListRes.builder()
                .totalCount(resultList.size())
                .list(resultList)
                .build();
    }

    @Override
    public MyNetworkDetailRes getMyNetwork(Long networkSeq, Long  userSeq) {
        if (userSeq == null) {
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        MyNetworkDetailRes result = myNetworkMapper.getMyNetwork(networkSeq, userSeq);

        if (result == null) {
            // 네트워크가 없거나, 이 사용자의 네트워크가 아닌 경우
            throw new BusinessException(ErrorCode.NETWORK_NOT_FOUND);
        }

        return result;
    }

    @Override
    @Transactional
    public void updateMyNetwork(Long networkSeq, UpdateMyNetworkReq req, Long userSeq) {

        if (userSeq == null) {
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        // 1. 기존 네트워크 조회 (권한 포함)
        MyNetworkDetailRes network = myNetworkMapper.getMyNetwork(networkSeq, userSeq);
        if (network == null) {
            throw new BusinessException(ErrorCode.NETWORK_NO_PERMISSION);
        }

        // 2. SSID / 비밀번호 변경 여부 체크
        boolean ssidChanged = req.getSsid() != null && !req.getSsid().isEmpty();
        boolean passwordChanged = req.getPassword() != null && !req.getPassword().isEmpty();

        if (passwordChanged) {
            // 새 비밀번호 암호화
            String encryptedPassword = wifiPasswordEncryptor.encrypt(req.getPassword());
            req.setPassword(encryptedPassword);
        }

        boolean needDeactivateQrs = ssidChanged || passwordChanged;

        // 3. SSID 또는 비밀번호가 변경되면 -> 기존 QR 전부 비활성화
        QrCodeSearchCond qrCodeSearchCond = new QrCodeSearchCond();
        qrCodeSearchCond.setNetworkSeq(networkSeq);

        DeactivateQrCodeReq deactivateQrCodeReq = new DeactivateQrCodeReq();
        deactivateQrCodeReq.setNetworkSeq(networkSeq);
        deactivateQrCodeReq.setDeactivatedReason("Auto deactivated: Network SSID/Password changed");

        //기존 QR 전부 비활성화
        if (needDeactivateQrs) {
            qrCodeMapper.deactivateQrCode(deactivateQrCodeReq);
        }

        // 4. 네트워크 정보 업데이트
        int updated = myNetworkMapper.updateMyNetwork(networkSeq, req, userSeq);

        if (updated == 0) {
            throw new BusinessException(ErrorCode.FAILED_UPDATE);
        }
    }

    @Override
    @Transactional
    public void deleteMyNetwork(Long networkSeq, Long userSeq) {

        if (userSeq == null) {
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        //1. 해당 네트워크에 등록된 QR 전부 비활성화
        DeactivateQrCodeReq deactivateQrCodeReq = new DeactivateQrCodeReq();
        deactivateQrCodeReq.setNetworkSeq(networkSeq);
        deactivateQrCodeReq.setDeactivatedReason("Auto deactivated: The associated network has been deleted.");
        qrCodeMapper.deactivateQrCode(deactivateQrCodeReq);

        //2. 네트워크 정보 삭제 (update쿼리)
        int updated = myNetworkMapper.deleteMyNetwork(networkSeq, userSeq);

        if (updated == 0) {
            throw new BusinessException(ErrorCode.FAILED_UPDATE);
        }

    }

}
