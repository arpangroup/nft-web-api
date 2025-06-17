package com.trustai.user_service.user.dto;

import com.trustai.user_service.user.entity.Kyc;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class KycDocumentRequest {
    Kyc.KycDocumentType documentType;
    private String documentLink;
}
