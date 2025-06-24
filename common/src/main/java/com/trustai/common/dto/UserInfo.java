package com.trustai.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Only this field will be excluded if null
public class UserInfo {
    private Long id;
    private String username;
    private String email;
    // Balance
    private BigDecimal walletBalance;
    private BigDecimal profitBalance;
    // Referral
    private String referralCode;
    //Status:
    private boolean isActive;
    private String accountStatus;
    private String kycStatus;
    //Date:
    private LocalDateTime createdAt;

    public UserInfo(Long id, String username) {
        this.id = id;
        this.username = username;
    }

}
