package com.trustai.user_service.user.dto;

import lombok.*;

@Getter
@Builder
public class SimpleKycInfo {
    private Long kycId;
    private String fullname;
    private String documentType;
    private String createdAt;
    private String status;
}
