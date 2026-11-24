package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.example.web_thue_gia_su_online.repository.learning_and_education.CartItemRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseEnrollmentRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.EscrowReleaseService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class EnrollmentPaymentServiceImplTest {

    @Mock
    CourseEnrollmentRepository courseEnrollmentRepository;

    @Mock
    WalletRepository walletRepository;

    @Mock
    PaymentTransactionRepository paymentTransactionRepository;

    @Mock
    CartItemRepository cartItemRepository;

    @Mock
    EscrowReleaseService escrowReleaseService;

    @InjectMocks
    EnrollmentPaymentServiceImpl enrollmentPaymentService;

    CourseEnrollment enrollment;
    Wallet studentWallet;

    @BeforeEach
    void setUp() {
        User student = User.builder().id("student").build();
        enrollment = CourseEnrollment.builder()
                .id("enrollment")
                .student(student)
                .course(Course.builder().id("course").build())
                .status(EnrollmentStatus.PENDING)
                .totalPrice(100)
                .build();
        studentWallet = Wallet.builder()
                .id("student-wallet")
                .user(student)
                .type(WalletType.NORMAL)
                .availableBalance(150)
                .holdingBalance(0)
                .build();

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken("student", "password"));
        when(courseEnrollmentRepository.findByIdForUpdate("enrollment")).thenReturn(Optional.of(enrollment));
        when(paymentTransactionRepository.existsByEnrollment_IdAndTypeAndStatus(
                        "enrollment", TransactionType.ESCROW_HOLD, TransactionStatus.SUCCESS))
                .thenReturn(false);
        when(walletRepository.findByUserIdAndTypeForUpdate("student", WalletType.NORMAL))
                .thenReturn(Optional.of(studentWallet));
        when(paymentTransactionRepository.save(any(PaymentTransaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void settlesFundsAsPartOfSuccessfulEnrollmentPayment() {
        enrollmentPaymentService.payForEnrollment("enrollment");

        assertEquals(EnrollmentStatus.ACTIVE, enrollment.getStatus());
        assertEquals(50, studentWallet.getAvailableBalance());
        assertEquals(100, studentWallet.getHoldingBalance());
        verify(escrowReleaseService).settleEnrollmentPayment(enrollment);
    }
}
