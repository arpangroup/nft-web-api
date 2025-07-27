package com.trustai.investment_service.service;

import com.trustai.common.api.RankConfigApi;
import com.trustai.common.api.WalletApi;
import com.trustai.common.dto.RankConfigDto;
import com.trustai.common.dto.UserInfo;
import com.trustai.investment_service.entity.InvestmentSchema;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.trustai.investment_service.enums.SchemaType.FIXED;
import static com.trustai.investment_service.enums.SchemaType.RANGE;


@Component
@RequiredArgsConstructor
@Slf4j
public class InvestmentValidator {
    private final RankConfigApi rankConfigClient;
    private final WalletApi walletClient;

    public void validateEligibility(UserInfo user, InvestmentSchema schema, BigDecimal amount) {
        log.debug("Validating eligibility for userId={}, schemaId={}, amount={}", user.getId(), schema.getId(), amount);
        validateAmountAgainstSchema(schema, amount);
        validateUserEligibility(user, amount);
        validateUserEligibilityAgainstSchema(user, schema);
        log.debug("User {} passed all eligibility validations", user.getId());
    }

    private void validateAmountAgainstSchema(InvestmentSchema schema, BigDecimal amount) {
        log.debug("Validating amount against schema: schemaId={}, schemaType={}, amount={}", schema.getId(), schema.getSchemaType(), amount);
        if (!schema.isActive()) {
            log.warn("Schema {} is inactive", schema.getId());
            throw new ValidationException("Schema is inactive");
        }

        if (amount == null) {
            log.warn("Amount is null for schema {}", schema.getId());
            throw new ValidationException("Invalid amount, should not be null");
        }

        switch (schema.getSchemaType()) {
            case FIXED:
                if (amount.compareTo(schema.getMinimumInvestmentAmount()) != 0) {
                    log.warn("Amount {} does not match fixed minimum {}", amount, schema.getMinimumInvestmentAmount());
                    throw new IllegalArgumentException("Amount must be exactly " + schema.getMinimumInvestmentAmount());
                }
                break;
            case RANGE:
                if (amount.compareTo(schema.getMinimumInvestmentAmount()) < 0 || amount.compareTo(schema.getMaximumInvestmentAmount()) > 0) {
                    log.warn("Amount {} not within range [{}, {}]", amount, schema.getMinimumInvestmentAmount(), schema.getMaximumInvestmentAmount());
                    throw new ValidationException("Amount must be between " + schema.getMinimumInvestmentAmount() + " and " + schema.getMaximumInvestmentAmount());
                }
                break;
            default:
                log.warn("Unknown schema type for schema {}", schema.getId());
                throw new ValidationException("Unknown schema type");
        }

        log.debug("Amount {} is valid for schema {}", amount, schema.getId());
    }

    private void validateUserEligibility(UserInfo user, BigDecimal amount) {
        log.debug("Validating wallet and rank eligibility for userId={}, rankCode={}, amount={}", user.getId(), user.getRankCode(), amount);

        // Wallet Check
        //BigDecimal walletBalance = walletClient.getWalletBalance(user.getId());
        BigDecimal walletBalance = user.getWalletBalance();
        if (walletBalance.compareTo(amount) < 0) {
            log.warn("User {} has insufficient balance: required={}, actual={}", user.getId(), amount, walletBalance);
            throw new ValidationException("Insufficient wallet balance");
        }

        // Rank config check
        //RankConfig userRank = rankService.getUserRank(user.getId());
        RankConfigDto rankConfig = rankConfigClient.getRankConfigByRankCode(user.getRankCode());
        if (rankConfig == null) {
            log.error("RankConfig not found for rankCode={}", user.getRankCode());
            throw new ValidationException("Invalid rank configuration");
        }

        if (amount.compareTo(rankConfig.getMinInvestmentAmount()) < 0) {
            log.warn("Amount {} is below min required {} for user rank {}", amount, rankConfig.getMinInvestmentAmount(), user.getRankCode());
            throw new ValidationException("Doesn't meet min investment for current rank");
        }
        log.debug("User {} passed wallet and rank eligibility", user.getId());
    }

    private void validateUserEligibilityAgainstSchema(UserInfo user, InvestmentSchema schema) {
        log.debug("Validating user level against schema: userId={}, rankCode={}, schemaId={}", user.getId(), user.getRankCode(), schema.getId());

        if (!schema.getParticipationLevels().isEmpty() && !schema.getParticipationLevels().contains(user.getRankCode())) {
            log.warn("User {} with rankCode={} not in schema's participation levels", user.getId(), user.getRankCode());
            throw new IllegalStateException("User level not eligible for this stake");
        }

        if (schema.getLinkedRank() != null && !schema.getLinkedRank().equals(user.getRankCode())) {
            log.warn("User {} rankCode={} does not match schema's linked rank {}", user.getId(), user.getRankCode(), schema.getLinkedRank());
            throw new IllegalStateException("User rank not eligible for this stake");
        }
        log.debug("User {} passed schema-specific eligibility checks", user.getId());
    }
}
