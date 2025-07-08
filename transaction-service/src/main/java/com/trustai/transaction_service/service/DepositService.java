package com.trustai.transaction_service.service;

import com.trustai.transaction_service.dto.DepositRequest;
import com.trustai.transaction_service.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public interface DepositService {
    /**
     * Processes an automatic deposit using a specified payment gateway.
     * <p>
     * This method facilitates depositing funds through supported <b>payment gateways</b>.
     * Note that an additional gateway charge will apply.
     *
     * @param depositRequest
     * @param amount     the deposit amount (must not be null)
     * @param gateway    the payment gateway used for the transaction (must not be null)
     * @param txnFee     the transaction fee or gateway charge (must not be null)
     * @param txnRefId   the transaction reference ID from the payment gateway (can be null)
     * @param status     the current status of the transaction (e.g., "PENDING", "COMPLETED")
     * @param metaInfo   optional metadata related to the transaction (can be null or JSON string)
     * @return           a {@link Transaction} object representing the completed deposit
     */
    //Transaction deposit(long userId, @NonNull BigDecimal amount, @NonNull PaymentGateway gateway, Optional<BigDecimal> txnFee, String txnRefId, Transaction.TransactionStatus status, String metaInfo);
    Transaction deposit(@NonNull DepositRequest depositRequest);

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

    /**
     * Returns total deposited amount for a user.
     */
    BigDecimal getTotalDeposit(long userId);

    /**
     * To prevent duplicate deposits due to retries from client/gateway:
     * Optional: Checks whether a deposit with this reference already exists.
     *
     * @param txnRefId
     * @return
     */
    boolean isDepositExistsByTxnRef(String txnRefId);

    /**
     * Expose deposit history explicitly if you're using the service in multiple modules:
     *
     * @param userId
     * @param pageable
     * @return
     */
    Page<Transaction> getDepositHistory(Long userId, Pageable pageable);

    /**
     * Optional: For gateways using async confirmation callbacks.
     * Support for Async/Gateway Callback Handling (if applicable)
     * If we integrate external gateways (e.g., Razorpay, Binance Pay), you may want:
     *
     * @param txnRefId
     * @param gatewayResponseJson
     * @return
     */
    Transaction confirmGatewayDeposit(String txnRefId, String gatewayResponseJson);
}
