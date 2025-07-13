package com.trustai.transaction_service.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RejectDepositRequest(
        @NotNull(message = "rejectionReason is mandatory")
        @Min(value = 2, message = "rejectionReason should be minimum 2 character long")
        @Max(value = 100, message = "rejectionReason must not exceed 100 characters")
        String rejectionReason
) {}
