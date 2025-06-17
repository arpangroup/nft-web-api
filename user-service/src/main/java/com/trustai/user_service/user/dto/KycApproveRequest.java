package com.trustai.user_service.user.dto;

import com.trustai.user_service.user.entity.Kyc;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class KycApproveRequest {
    private boolean isEmailVerified;
    private boolean isPhoneVerified;
    private boolean isAddressVerified;
    private String kycRejectionReason;

}
