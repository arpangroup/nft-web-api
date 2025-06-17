package com.trustai.user_service.transaction.service;

import com.trustai.common.enums.PaymentGateway;
import com.trustai.user_service.transaction.entity.Transaction;
import com.trustai.user_service.transaction.exception.InsufficientBalanceException;
import com.trustai.user_service.transaction.exception.InvalidPaymentGatewayException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for handling user transactions such as deposits.
 */
public interface TransactionService {
    Page<Transaction> getTransactions(Pageable pageable);
    Page<Transaction> getTransactionsByUserId(Long userId, Pageable pageable);
    Boolean hasDepositTransaction(Long userId);

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

    /**
     * Processes a withdrawal request made by the user.
     * <p>
     * This method deducts the specified amount from the user's balance and initiates
     * a transfer to the provided destination account, which could be a bank account,
     * wallet, or any external financial address.
     *
     * @param userId             the ID of the user requesting the withdrawal.
     *                           This identifies whose balance will be deducted.
     *
     * @param amount             the amount to be withdrawn (must be positive and non-null).
     *                           Ensure that the user has sufficient balance before calling this method.
     *
     * @param destinationAccount the account or destination to which the funds should be transferred.
     *                           This could be a bank account number, mobile wallet ID, crypto address, etc.
     *
     * @param remarks            optional remarks or notes related to the withdrawal.
     *                           This may be displayed in transaction history or audit logs.
     *
     * @return a {@link Transaction} object representing the withdrawal transaction,
     *         including status and reference details.
     *
     * @throws InsufficientBalanceException if the user does not have enough funds.
     * @throws InvalidPaymentGatewayException if the destination account or channel is not supported.
     */
    Transaction withdraw(long userId, @NonNull BigDecimal amount, String destinationAccount, String remarks);


    /**
     * Issues a refund to a user for a previously completed transaction.
     * <p>
     * This method is typically used when a transaction (such as a purchase,
     * deposit, or failed transfer) needs to be reversed, and the amount
     * should be returned to the user's balance.
     *
     * @param userId          the ID of the user receiving the refund.
     *                        This identifies the account to which the refund will be credited.
     *
     * @param amount          the amount to be refunded (must be non-null and positive).
     *                        Ensure it does not exceed the original transaction amount.
     *
     * @param originalTxnRef  the reference ID of the original transaction that is being refunded.
     *                        This is used for traceability and audit purposes.
     *
     * @param reason          a short description or reason for issuing the refund.
     *                        This can help in reporting and customer support follow-ups.
     *
     * @return a {@link Transaction} object representing the refund transaction,
     *         including status, timestamps, and references.
     *
     * @throws IllegalArgumentException if the original transaction is not found or not refundable.
     */
    Transaction refund(long userId, @NonNull BigDecimal amount, String originalTxnRef, String reason);

    /**
     * Subtracts an amount from the user's balance manually (e.g. penalties, adjustments).
     */
    Transaction subtract(long userId, @NonNull BigDecimal amount, String reason);

    /**
     * Transfers money from one user to another (peer-to-peer transfer).
     * <p>
     * This method debits the sender's account and credits the receiver's account
     * in a single atomic transaction.
     *
     * @param senderId    the ID of the user who is sending the money.
     * @param receiverId  the ID of the user who will receive the money.
     * @param amount      the amount to be transferred (must be positive and non-null).
     * @param message     optional message or note attached to the transaction (e.g., "Thanks!", "For lunch").
     *
     * @return a {@link Transaction} object representing the sender's side of the transaction.
     *
     * @throws InsufficientBalanceException if the sender does not have enough funds.
     * @throws IllegalArgumentException if sender and receiver are the same.
     */
    Transaction transferMoney(long senderId, long receiverId, @NonNull BigDecimal amount, String message); // sendMoney / receiveMoney


    /**
     * Records an investment transaction made by the user.
     * <p>
     * This method is used when a user invests a certain amount into a financial product,
     * plan, or asset. The invested amount is deducted from the user's balance and recorded
     * along with metadata about the investment.
     *
     * @param userId         the ID of the user making the investment.
     *                       This identifies whose account will be debited.
     *
     * @param amount         the amount being invested (must be non-null and positive).
     *                       Ensure the user has sufficient balance before proceeding.
     *
     * @param investmentType the type or category of the investment (e.g., "Mutual Fund", "Fixed Deposit", "Crypto").
     *                       Used to classify the investment for reporting or processing purposes.
     *
     * @param metaInfo       optional additional information related to the investment.
     *                       This could be a JSON string or metadata including duration, expected return, etc.
     *
     * @return a {@link Transaction} object representing the investment transaction,
     *         including status and reference details.
     *
     * @throws InsufficientBalanceException if the user does not have enough funds to invest.
     * @throws IllegalArgumentException if investmentType is invalid or not supported.
     */
    Transaction invest(long userId, @NonNull BigDecimal amount, String investmentType, String metaInfo);


    /**
     * Records a currency or asset exchange transaction for a user.
     * <p>
     * This method is used when a user converts one currency or asset into another.
     * It logs both the amount deducted in the original currency and the amount credited
     * in the new currency. Common use cases include fiat-to-crypto, crypto-to-crypto,
     * or foreign currency exchange.
     *
     * @param userId       the ID of the user performing the exchange.
     *                     The original amount will be deducted from this user's balance.
     *
     * @param fromAmount   the amount to be deducted from the original currency (must be non-null and positive).
     *                     This represents the value the user is giving up in the exchange.
     *
     * @param fromCurrency the code of the currency or asset being exchanged from (e.g., "USD", "BTC", "ETH").
     *                     Must follow standard currency or asset symbol conventions.
     *
     * @param toAmount     the amount to be credited in the target currency (must be non-null and positive).
     *                     This is the result of the exchange rate applied to the original amount.
     *
     * @param toCurrency   the code of the currency or asset being exchanged to (e.g., "EUR", "USDT", "ETH").
     *                     Should be different from `fromCurrency` in a valid exchange.
     *
     * @param metaInfo     optional metadata related to the exchange, such as the exchange rate,
     *                     transaction fee, or exchange platform details. This can be a JSON string or descriptive text.
     *
     * @return a {@link Transaction} object representing the exchange transaction,
     *         typically categorized as a single logical operation containing both sides of the exchange.
     *
     * @throws InsufficientBalanceException if the user does not have enough of the `fromCurrency` to perform the exchange.
     * @throws IllegalArgumentException if currency codes are invalid or the amounts are inconsistent.
     */
    Transaction exchange(long userId, @NonNull BigDecimal fromAmount, @NonNull String fromCurrency, @NonNull BigDecimal toAmount, @NonNull String toCurrency, String metaInfo);


    /**
     * Applies a one-time signup bonus to a new user's account.
     * <p>
     * This method credits a predefined bonus amount to the user's wallet or account
     * as part of a promotional or onboarding campaign. It is typically called
     * immediately after account creation or upon meeting certain signup criteria.
     *
     * @param userId       the ID of the newly registered user receiving the bonus.
     *                     This identifies the account to which the bonus will be credited.
     *
     * @param bonusAmount  the bonus amount to be credited (must be non-null and positive).
     *                     This value may be fixed or configured externally as part of business rules.
     *
     * @return a {@link Transaction} object representing the bonus credit,
     *         including amount, timestamp, and transaction type.
     *
     * @throws IllegalArgumentException if the user is not eligible for a signup bonus
     *         or if the bonus has already been applied.
     */
    Transaction applySignupBonus(long userId, @NonNull BigDecimal bonusAmount);


    /**
     * Credits a referral bonus to the referrer when a referred user successfully registers or qualifies.
     * <p>
     * This method awards a bonus amount to the referrer as part of a referral program,
     * incentivizing users to invite others to the platform.
     *
     * @param referrerUserId  the ID of the user who referred another user and will receive the bonus.
     *                        This user’s account will be credited with the referral bonus.
     *
     * @param referredUserId  the ID of the new user who was referred.
     *                        This is used to track and validate the referral relationship.
     *
     * @param bonusAmount     the bonus amount to be credited to the referrer (must be non-null and positive).
     *                        The value is typically configured as part of the referral program rules.
     *
     * @return a {@link Transaction} object representing the referral bonus transaction,
     *         including the credited amount and transaction metadata.
     *
     * @throws IllegalArgumentException if the referral relationship is invalid or the bonus has already been granted.
     */
    Transaction applyReferralBonus(long referrerUserId, long referredUserId, @NonNull BigDecimal bonusAmount);


    /**
     * Applies a general bonus to a user's account.
     * <p>
     * This method credits a specified bonus amount to the user's balance for various purposes,
     * such as promotions, campaigns, adjustments, or rewards.
     *
     * @param userId       the ID of the user receiving the bonus.
     *                     Identifies the account to be credited.
     *
     * @param bonusAmount  the bonus amount to be credited (must be non-null and positive).
     *                     Represents the value of the bonus being applied.
     *
     * @param reason       a descriptive reason or note explaining why the bonus is being applied.
     *                     Useful for audit trails and reporting.
     *
     * @return a {@link Transaction} object representing the bonus credit transaction,
     *         including details such as amount, timestamp, and reason.
     *
     * @throws IllegalArgumentException if the bonusAmount is invalid or the reason is missing.
     */
    Transaction applyBonus(long userId, @NonNull BigDecimal bonusAmount, String reason);


    /**
     * Applies interest earnings to a user's account.
     * <p>
     * This method credits the specified interest amount to the user's balance,
     * typically calculated based on the user's holdings over a defined period.
     *
     * @param userId            the ID of the user receiving the interest.
     *                          Identifies the account to be credited.
     *
     * @param interestAmount    the amount of interest to be credited (must be non-null and positive).
     *                          Represents the interest earned for the specified period.
     *
     * @param periodDescription a textual description of the interest period (e.g., "Monthly June 2025", "Q1 2025").
     *                          Helps provide context for reporting and audit purposes.
     *
     * @return a {@link Transaction} object representing the interest credit transaction,
     *         including details such as amount, period, and timestamp.
     *
     * @throws IllegalArgumentException if interestAmount is invalid or periodDescription is null/empty.
     */
    Transaction applyInterest(long userId, @NonNull BigDecimal interestAmount, String periodDescription);

}
