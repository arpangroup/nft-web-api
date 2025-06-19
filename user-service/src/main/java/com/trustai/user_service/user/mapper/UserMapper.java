package com.trustai.user_service.user.mapper;

import com.trustai.common.dto.*;
import com.trustai.user_service.user.entity.Kyc;
import com.trustai.user_service.user.entity.User;
import com.trustai.user_service.user.ustil.PhoneMaskingUtil;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    /*public UserInfoOld mapTo(User user) {
        return UserInfoOld.builder()
                .id(user.getId())
                .referralCode(user.getReferralCode())
                .walletBalance(user.getWalletBalance())
                .username(user.getUsername())
                .level(user.getRank())
//                .firstname(user.getFirstname())
//                .lastname(user.getLastname())
//                .email(user.getEmail())
//                .mobile(user.getMobile())
//                .referBy(user.getReferBy())
                .build();
    }*/

    public UserInfo mapTo(User user) {
        return UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                // Balance:
                .walletBalance(user.getWalletBalance())
                .profitBalance(user.getProfitBalance())
                // Referral:
                .referralCode(user.getReferralCode())
                // Status:
                .accountStatus(user.getAccountStatus().name())
                .kycStatus(user.getKycInfo().getStatus().name())
                // AuditLog
                .createdAt(user.getCreatedAt())
                .build();
    }

    public UserDetailsInfo mapToDetails(User user) {
        final User referrer = user.getReferrer();
        return UserDetailsInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .phone(PhoneMaskingUtil.maskPhoneNumber(user.getMobile()))
                // Balance:
                .walletBalance(user.getWalletBalance())
                .profitBalance(user.getProfitBalance())
                // Referral:
                .referralCode(user.getReferralCode())
                .referrer(new UserInfo(referrer.getId(), referrer.getUsername()))
                .rank(user.getRank())
                // KYC:
                .kyc(convert(user.getKycInfo()))
                // Status:
                .accountStatus(convert(user))
                // AuditLog
                .createdAt(user.getCreatedAt())
                .build();
    }

    private AccountStatus convert(User user) {
        return AccountStatus.builder()
                .isAccountActive(user.getAccountStatus() == User.AccountStatus.ACTIVE)
                .isKycVerified(user.getKycInfo().status == Kyc.KycStatus.VERIFIED)
                .isDepositEnabled(user.depositStatus == User.TransactionStatus.DISABLED)
                .isWithdrawEnabled(user.withdrawStatus == User.TransactionStatus.DISABLED)
                .isSendMoneyEnabled(user.sendMoneyStatus == User.TransactionStatus.DISABLED)
                .accountStatus(user.accountStatus.name())
                .kycStatus(user.getKycInfo().status.name())
                .emailVerifyStatus(user.getKycInfo().getEmailVerifyStatus().name())
                .phoneVerifyStatus(user.getKycInfo().getPhoneVerifyStatus().name())
                .kycRejectionReason(user.getKycInfo().getKycRejectionReason())
                .build();
    }

    private KycInfo convert(Kyc kyc) {
        return KycInfo.builder()
                .email(kyc.getEmail())
                .phone(kyc.getPhone())
                .address(kyc.getAddress())
                .address(kyc.getAddress())
                .documentType(kyc.getDocumentType().name())
                .emailVerifyStatus(kyc.getEmailVerifyStatus().name())
                .phoneVerifyStatus(kyc.getPhoneVerifyStatus().name())
                .status(kyc.status.name())
                .kycRejectionReason(kyc.getKycRejectionReason())
                .build();
    }

    public User mapTo(UserInfoOld info) {
        return null;
    }
}
