package com.trustai.user_service.transaction.service;

import com.trustai.common.enums.PaymentGateway;
import com.trustai.user_service.transaction.entity.Transaction;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Optional;

public interface DepositService {
    /**
     * Processes an automatic deposit using a specified payment gateway.
     * <p>
     * This method facilitates depositing funds through supported <b>payment gateways</b>.
     * Note that an additional gateway charge will apply.
     *
     * @param userId     the ID of the user making the deposit
     * @param amount     the deposit amount (must not be null)
     * @param gateway    the payment gateway used for the transaction (must not be null)
     * @param txnFee     the transaction fee or gateway charge (must not be null)
     * @param txnRefId   the transaction reference ID from the payment gateway (can be null)
     * @param status     the current status of the transaction (e.g., "PENDING", "COMPLETED")
     * @param metaInfo   optional metadata related to the transaction (can be null or JSON string)
     * @return           a {@link Transaction} object representing the completed deposit
     */
    Transaction deposit(long userId, @NonNull BigDecimal amount, @NonNull PaymentGateway gateway, Optional<BigDecimal> txnFee, String txnRefId, Transaction.TransactionStatus status, String metaInfo);


    /**
     * Processes a manual deposit initiated by an admin or another authorized user.
     * <p>
     * This method is used for off-gateway deposits such as cash, cheque, or internal adjustments.
     *
     * @param userId     the ID of the user whose account will be credited
     * @param depositor  the ID of the admin or user performing the deposit
     * @param amount     the amount to be manually deposited (must not be null)
     * @param remarks    additional remarks or comments regarding the manual deposit (optional)
     * @return           a {@link Transaction} object representing the manual deposit
     */
    Transaction depositManual(long userId, long depositor, @NonNull BigDecimal amount, String remarks);

    BigDecimal getTotalDeposit(long userId);
}
