package com.trustai.income_service.rank.dto;

public record RankEvaluationResultDTO(
        Long userId,
        String oldRankCode,
        String newRankCode,
        boolean upgraded,
        String reason
) {}