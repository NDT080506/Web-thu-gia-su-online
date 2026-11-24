package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Course;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.TransactionStatus;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.TransactionType;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.WalletType;
import org.example.web_thue_gia_su_online.enums.learning_and_education.EnrollmentStatus;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentTransactionRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EscrowReleaseServiceImplTest {

    @Mock
    PaymentTransactionRepository paymentTransactionRepository;

    @Mock
    WalletRepository walletRepository;

    @InjectMocks
    EscrowReleaseServiceImpl escrowReleaseService;

    CourseEnrollment enrollment;
    Wallet studentWallet;
    Wallet tutorWallet;
    Wallet platformWallet;
    List<PaymentTransaction> savedTransactions;

    @BeforeEach
    void setUp() {
        User student = User.builder().id("student").build();
        User tutor = User.builder().id("tutor").build();
        enrollment = CourseEnrollment.builder()
                .id("enrollment")
                .student(student)
                .course(Course.builder().tutor(tutor).build())
                .status(EnrollmentStatus.ACTIVE)
                .totalPrice(100)
                .tutorIncome(80)
                .platformFee(20)
                .build();
        studentWallet = Wallet.builder()
                .id("student-wallet")
                .user(student)
                .type(WalletType.NORMAL)
                .availableBalance(0)
                .holdingBalance(100)
                .build();
        tutorWallet = Wallet.builder()
                .id("tutor-wallet")
                .user(tutor)
                .type(WalletType.NORMAL)
                .availableBalance(10)
                .build();
        platformWallet = Wallet.builder()
                .id("platform-wallet")
                .type(WalletType.PLATFORM)
                .availableBalance(20)
                .build();
        savedTransactions = new ArrayList<>();

        PaymentTransaction hold = PaymentTransaction.builder()
                .sourceWallet(studentWallet)
                .amount(100L)
                .build();
        when(paymentTransactionRepository.findEnrollmentHoldForUpdate(
                        "enrollment", TransactionType.ESCROW_HOLD, TransactionStatus.SUCCESS))
                .thenReturn(List.of(hold));
        when(walletRepository.findByUserIdAndTypeForUpdate("tutor", WalletType.NORMAL))
                .thenReturn(Optional.of(tutorWallet));
        when(walletRepository.findByTypeForUpdate(WalletType.PLATFORM)).thenReturn(List.of(platformWallet));
        when(walletRepository.findByIdForUpdate("student-wallet")).thenReturn(Optional.of(studentWallet));
    }

    @Test
    void settlesEnrollmentPaymentToTutorAndPlatformImmediately() {
        when(paymentTransactionRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<PaymentTransaction> transactions = invocation.getArgument(0);
            savedTransactions.addAll(transactions);
            return transactions;
        });

        escrowReleaseService.settleEnrollmentPayment(enrollment);

        assertEquals(0, studentWallet.getHoldingBalance());
        assertEquals(90, tutorWallet.getAvailableBalance());
        assertEquals(40, platformWallet.getAvailableBalance());
        assertEquals(2, savedTransactions.size());
        assertEquals(
                List.of(80L, 20L),
                savedTransactions.stream().map(PaymentTransaction::getAmount).toList());
        assertEquals(
                List.of(tutorWallet, platformWallet),
                savedTransactions.stream()
                        .map(PaymentTransaction::getDestinationWallet)
                        .toList());
        assertEquals(
                List.of(TransactionStatus.SUCCESS, TransactionStatus.SUCCESS),
                savedTransactions.stream().map(PaymentTransaction::getStatus).toList());
        assertEquals(
                List.of(TransactionType.ESCROW_RELEASE, TransactionType.ESCROW_RELEASE),
                savedTransactions.stream().map(PaymentTransaction::getType).toList());
        verify(paymentTransactionRepository).saveAll(anyList());
    }

    @Test
    void doesNotReleaseEnrollmentPaymentMoreThanOnce() {
        when(paymentTransactionRepository.existsByEnrollment_IdAndTypeAndStatusAndDestinationWallet_Id(
                        "enrollment", TransactionType.ESCROW_RELEASE, TransactionStatus.SUCCESS, "tutor-wallet"))
                .thenReturn(true);

        assertThrows(IllegalStateException.class, () -> escrowReleaseService.settleEnrollmentPayment(enrollment));

        assertEquals(100, studentWallet.getHoldingBalance());
        assertEquals(10, tutorWallet.getAvailableBalance());
        assertEquals(20, platformWallet.getAvailableBalance());
        assertEquals(0, savedTransactions.size());
    }
}
