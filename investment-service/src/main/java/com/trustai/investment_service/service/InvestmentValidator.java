package com.trustai.investment_service.service;

import com.trustai.common.dto.RankConfigDto;
import com.trustai.common.dto.UserDetailsInfo;
import com.trustai.investment_service.entity.InvestmentSchema;
import com.trustai.investment_service.provider.RankConfigClient;
import com.trustai.investment_service.provider.WalletClient;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.trustai.investment_service.enums.SchemaType.FIXED;
import static com.trustai.investment_service.enums.SchemaType.RANGE;


@Component
@RequiredArgsConstructor
public class InvestmentValidator {
    private final RankConfigClient rankConfigClient;
    private final WalletClient walletClient;
    //private final RankService rankService;

    public void validateEligibility(UserDetailsInfo user, InvestmentSchema schema, BigDecimal amount) {
        validateAmountAgainstSchema(schema, amount);
        validateUserEligibility(user, amount);
    }

    private void validateAmountAgainstSchema(InvestmentSchema schema, BigDecimal amount) {
        if (!schema.isActive()) throw new ValidationException("Schema is inactive");
        if (amount == null) throw new ValidationException("Invalid amount, should not be null");

        // Fixed
        if (schema.getSchemaType() == FIXED) {
            if (amount.compareTo(schema.getMinimumInvestmentAmount()) != 0) {
                throw new IllegalArgumentException("Amount must be exactly " + schema.getMinimumInvestmentAmount());
            }
        }

        // Range
        if (schema.getSchemaType() == RANGE) {
            if (amount.compareTo(schema.getMinimumInvestmentAmount()) < 0 ||
                    amount.compareTo(schema.getMaximumInvestmentAmount()) > 0) {
                throw new ValidationException("Amount must be between " + schema.getMinimumInvestmentAmount() + " and " + schema.getMaximumInvestmentAmount() );
            }
        }
    }

    private void validateUserEligibility(UserDetailsInfo user, BigDecimal amount) {
        // Wallet Check
        //BigDecimal walletBalance = walletClient.getWalletBalance(user.getId());
        BigDecimal walletBalance = user.getWalletBalance();
        if (walletBalance.compareTo(amount) < 0) {
            throw new ValidationException("Insufficient wallet balance");
        }

        // Rank config check
        //RankConfig userRank = rankService.getUserRank(user.getId());
        RankConfigDto rankConfig = rankConfigClient.getRankConfigByRankCode(user.getRankCode());
        if (amount.compareTo(rankConfig.getMinInvestmentAmount()) < 0) {
            throw new ValidationException("Doesn't meet min investment for current rank");
        }
    }
}
