package com.trustai.user_service.user.service;

import com.trustai.user_service.user.dto.KycApproveRequest;
import com.trustai.user_service.user.dto.KycUpdateRequest;
import com.trustai.user_service.user.entity.Kyc;
import org.w3c.dom.DocumentType;

public interface KycService {
    // Accessible only to User
    Kyc updateKyc(Long userId, KycUpdateRequest request);

    // Accessible only to Admin
    Kyc verifyKyc(Long userId, KycApproveRequest request);
}
