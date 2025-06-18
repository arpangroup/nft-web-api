package com.trustai.user_service.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VerifyEmailRequest {
    public String email;
    public String otp;
}
