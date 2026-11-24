package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.BrokeragePackage;
import org.example.web_thue_gia_su_online.enums.disputing_and_reporting.DisputeStatus;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.TransactionStatus;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.TransactionType;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.WalletType;
import org.example.web_thue_gia_su_online.repository.disputing_and_reporting.DisputeRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentTransactionRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RefundServiceImplTest {

    @Mock
    WalletRepository walletRepository;

    @Mock
    PaymentTransactionRepository paymentTransactionRepository;

    @Mock
    DisputeRepository disputeRepository;

    @InjectMocks
    RefundServiceImpl refundService;

    User student;
    User tutor;
    Booking booking;
    Dispute dispute;
    Wallet studentWallet;
    Wallet tutorWallet;
    Wallet platformWallet;

    @BeforeEach
    void setUp() {
        student = User.builder().id("student").build();
        tutor = User.builder().id("tutor").build();
        booking = Booking.builder()
                .id("booking")
                .student(student)
                .brokeragePackage(BrokeragePackage.builder().tutor(tutor).build())
                .totalPrice(100)
                .tutorIncome(80)
                .platformFee(20)
                .build();
        dispute = Dispute.builder()
                .id("dispute")
                .booking(booking)
                .refundAmount(100)
                .build();
        studentWallet = Wallet.builder()
                .id("student-wallet")
                .user(student)
                .type(WalletType.NORMAL)
                .availableBalance(10)
                .holdingBalance(100)
                .build();
        tutorWallet = Wallet.builder()
                .id("tutor-wallet")
                .user(tutor)
                .type(WalletType.NORMAL)
                .availableBalance(79)
                .holdingBalance(0)
                .build();
        platformWallet = Wallet.builder()
                .id("platform-wallet")
                .type(WalletType.PLATFORM)
                .availableBalance(100)
                .holdingBalance(0)
                .build();

        when(disputeRepository.existsByIdAndStatus("dispute", DisputeStatus.APPROVED))
                .thenReturn(false);
        when(paymentTransactionRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void refundsFromEscrowWhenTheFullAmountIsStillHeld() {
        PaymentTransaction hold = PaymentTransaction.builder()
                .sourceWallet(studentWallet)
                .amount(100L)
                .build();
        stubBookingTransactions(TransactionType.REFUND, TransactionStatus.PENDING, List.of());
        stubBookingTransactions(TransactionType.REFUND, TransactionStatus.SUCCESS, List.of());
        stubBookingTransactions(TransactionType.ESCROW_HOLD, TransactionStatus.SUCCESS, List.of(hold));
        stubBookingTransactions(TransactionType.ESCROW_RELEASE, TransactionStatus.SUCCESS, List.of());
        when(walletRepository.findByIdForUpdate("student-wallet")).thenReturn(Optional.of(studentWallet));

        assertTrue(refundService.refundDispute(dispute));
        assertEquals(110, studentWallet.getAvailableBalance());
        assertEquals(0, studentWallet.getHoldingBalance());
    }

    @Test
    void leavesRefundPendingWhenTutorCannotReturnTheirReleasedShare() {
        PaymentTransaction hold = PaymentTransaction.builder()
                .sourceWallet(studentWallet)
                .amount(100L)
                .build();
        List<PaymentTransaction> releases =
                List.of(release(studentWallet, tutorWallet, 80), release(studentWallet, platformWallet, 20));
        stubBookingTransactions(TransactionType.REFUND, TransactionStatus.PENDING, List.of());
        stubBookingTransactions(TransactionType.REFUND, TransactionStatus.SUCCESS, List.of());
        stubBookingTransactions(TransactionType.ESCROW_HOLD, TransactionStatus.SUCCESS, List.of(hold));
        stubBookingTransactions(TransactionType.ESCROW_RELEASE, TransactionStatus.SUCCESS, releases);
        when(walletRepository.findByUserIdAndTypeForUpdate("tutor", WalletType.NORMAL))
                .thenReturn(Optional.of(tutorWallet));
        when(walletRepository.findByTypeForUpdate(WalletType.PLATFORM)).thenReturn(List.of(platformWallet));
        when(walletRepository.findByIdForUpdate("student-wallet")).thenReturn(Optional.of(studentWallet));
        when(walletRepository.findByIdForUpdate("tutor-wallet")).thenReturn(Optional.of(tutorWallet));
        when(walletRepository.findByIdForUpdate("platform-wallet")).thenReturn(Optional.of(platformWallet));

        assertFalse(refundService.refundDispute(dispute));
        assertEquals(79, tutorWallet.getAvailableBalance());
        assertEquals(100, platformWallet.getAvailableBalance());
        assertEquals(10, studentWallet.getAvailableBalance());
    }

    @Test
    void retriesPendingRefundAfterBalancesAreRecovered() {
        tutorWallet.setAvailableBalance(80);
        List<PaymentTransaction> pending = List.of(
                pendingRefund(tutorWallet, studentWallet, 80), pendingRefund(platformWallet, studentWallet, 20));
        stubBookingTransactions(TransactionType.REFUND, TransactionStatus.PENDING, pending);
        when(walletRepository.findByIdForUpdate("student-wallet")).thenReturn(Optional.of(studentWallet));
        when(walletRepository.findByIdForUpdate("tutor-wallet")).thenReturn(Optional.of(tutorWallet));
        when(walletRepository.findByIdForUpdate("platform-wallet")).thenReturn(Optional.of(platformWallet));

        assertTrue(refundService.refundDispute(dispute));
        assertEquals(110, studentWallet.getAvailableBalance());
        assertEquals(0, tutorWallet.getAvailableBalance());
        assertEquals(80, platformWallet.getAvailableBalance());
        assertTrue(pending.stream().allMatch(t -> t.getStatus() == TransactionStatus.SUCCESS));
    }

    private void stubBookingTransactions(
            TransactionType type, TransactionStatus status, List<PaymentTransaction> transactions) {
        when(paymentTransactionRepository.findBookingTransactionsForUpdate("booking", type, status))
                .thenReturn(transactions);
    }

    private PaymentTransaction release(Wallet source, Wallet destination, long amount) {
        return PaymentTransaction.builder()
                .sourceWallet(source)
                .destinationWallet(destination)
                .type(TransactionType.ESCROW_RELEASE)
                .status(TransactionStatus.SUCCESS)
                .amount(amount)
                .build();
    }

    private PaymentTransaction pendingRefund(Wallet source, Wallet destination, long amount) {
        return PaymentTransaction.builder()
                .sourceWallet(source)
                .destinationWallet(destination)
                .type(TransactionType.REFUND)
                .status(TransactionStatus.PENDING)
                .amount(amount)
                .build();
    }
}
