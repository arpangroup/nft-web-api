package com.trustai.transaction_service;


import com.trustai.common.client.UserClient;
import com.trustai.common.enums.PaymentGateway;
import com.trustai.common.enums.TransactionType;
import com.trustai.transaction_service.dto.DepositRequest;
import com.trustai.transaction_service.entity.Transaction;
import com.trustai.transaction_service.repository.TransactionRepository;
import com.trustai.transaction_service.service.DepositService;
import com.trustai.transaction_service.service.WalletService;
import com.trustai.transaction_service.service.impl.DepositServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepositServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletService walletService;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private DepositServiceImpl depositService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deposit_shouldCreateTransactionSuccessfully() {
        // Arrange
        DepositRequest request = new DepositRequest(1L, new BigDecimal("100.00"), PaymentGateway.BINANCE, "TXN123", BigDecimal.TEN, "bonus");

        when(walletService.getUserBalance(1L)).thenReturn(new BigDecimal("50.00"));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Transaction result = depositService.deposit(request);

        // Assert
        assertNotNull(result);
        assertEquals(TransactionType.DEPOSIT, result.getTxnType());
        assertEquals("TXN123", result.getTxnRefId());
        assertEquals(new BigDecimal("100.00"), result.getAmount());
        assertEquals("bonus", result.getMetaInfo());

        verify(walletService).updateBalanceFromTransaction(eq(1L), eq(new BigDecimal("90.00")), eq(TransactionType.DEPOSIT));
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void depositManual_shouldCreateManualTransactionSuccessfully() {
        // Arrange
        when(walletService.getUserBalance(1L)).thenReturn(BigDecimal.ZERO);
        when(transactionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Transaction txn = depositService.depositManual(1L, 999L, new BigDecimal("150.00"), "Admin Topup");

        // Assert
        assertEquals(TransactionType.DEPOSIT_MANUAL, txn.getTxnType());
        assertEquals(PaymentGateway.SYSTEM, txn.getGateway());
        assertEquals("Admin Topup", txn.getRemarks());
        assertEquals(999L, txn.getSenderId());

        verify(transactionRepository).save(any(Transaction.class));
        verify(walletService).updateBalanceFromTransaction(1L, new BigDecimal("150.00"), TransactionType.DEPOSIT);
    }

    @Test
    void getTotalDeposit_shouldReturnSum() {
        when(transactionRepository.sumAmountByUserIdAndTxnType(1L, TransactionType.DEPOSIT)).thenReturn(new BigDecimal("300.00"));
        assertEquals(new BigDecimal("300.00"), depositService.getTotalDeposit(1L));
    }

    @Test
    void isDepositExistsByTxnRef_shouldReturnTrueIfExists() {
        when(transactionRepository.findByTxnRefId("TXN123")).thenReturn(Optional.of(mock(Transaction.class)));
        assertTrue(depositService.isDepositExistsByTxnRef("TXN123"));
    }

    @Test
    void getDepositHistory_shouldReturnPagedData() {
        Transaction txn = mock(Transaction.class);
        Page<Transaction> page = new PageImpl<>(List.of(txn));

        when(transactionRepository.findByUserIdAndTxnType(1L, TransactionType.DEPOSIT, PageRequest.of(0, 10))).thenReturn(page);

        Page<Transaction> result = depositService.getDepositHistory(1L, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        verify(transactionRepository).findByUserIdAndTxnType(1L, TransactionType.DEPOSIT, PageRequest.of(0, 10));
    }

    @Test
    void confirmGatewayDeposit_shouldUpdateStatusAndMetaInfo() {
        Transaction txn = new Transaction();
        txn.setTxnRefId("TXN999");
        when(transactionRepository.findByTxnRefId("TXN999")).thenReturn(Optional.of(txn));
        when(transactionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = depositService.confirmGatewayDeposit("TXN999", "success_json");

        assertEquals(Transaction.TransactionStatus.SUCCESS, result.getStatus());
        assertEquals("success_json", result.getMetaInfo());
    }

    @Test
    void confirmGatewayDeposit_shouldThrowIfNotFound() {
        when(transactionRepository.findByTxnRefId("INVALID")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> depositService.confirmGatewayDeposit("INVALID", "json"));
    }
}