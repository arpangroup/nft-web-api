package com.trustai.user_service.user.service;

import com.trustai.common.dto.KycInfo;
import com.trustai.user_service.user.dto.KycApproveRequest;
import com.trustai.user_service.user.dto.KycUpdateRequest;
import com.trustai.user_service.user.dto.SimpleKycInfo;
import com.trustai.user_service.user.entity.Kyc;
import org.springframework.data.domain.Page;

public interface UserKycService {
    Page<SimpleKycInfo> getAllKyc(Kyc.KycStatus status, Integer page, Integer size);

    // Accessible only to User
    Kyc updateKyc(Long userId, KycUpdateRequest request);

    // Accessible only to Admin
    Kyc verifyKyc(Long userId, KycApproveRequest request);
}
