package com.trustai.user_service.transaction.service;

import com.trustai.user_service.transaction.entity.Transaction;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public interface AdjustmentService {

    /**
     * Deducts a specified amount from the user's balance as a manual adjustment.
     * <p>
     * This method is typically used for administrative actions such as applying
     * penalties, correcting overpayments, or making internal balance adjustments.
     * The deduction is performed directly and should be accompanied by a clear reason
     * to ensure transparency and traceability.
     *
     * @param userId the ID of the user whose account will be debited
     * @param amount the amount to be subtracted (must be non-null and positive)
     * @param reason a descriptive explanation for the deduction (e.g., "Overdraft Fee", "Reversal of Bonus")
     * @return a {@link Transaction} object representing the manual subtraction
     * @throws IllegalArgumentException if the amount is invalid or reason is missing
     */
    Transaction subtract(long userId, @NonNull BigDecimal amount, String reason);


}
