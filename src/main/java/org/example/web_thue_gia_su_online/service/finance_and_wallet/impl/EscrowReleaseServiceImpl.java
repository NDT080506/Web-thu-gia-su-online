package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import java.util.List;
import java.util.UUID;

import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.example.web_thue_gia_su_online.enums.*;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentTransactionRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.EscrowReleaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EscrowReleaseServiceImpl implements EscrowReleaseService {

    PaymentTransactionRepository paymentTransactionRepository;
    WalletRepository walletRepository;

    @Override
    @Transactional
    public void releaseEnrollmentEscrow(CourseEnrollment enrollment) {
        String enrollmentId = enrollment.getId();

        // 1. Chỉ giải ngân khi enrollment đã hoàn thành
        if (enrollment.getStatus() != EnrollmentStatus.COMPLETED) {
            throw new IllegalStateException("Chỉ được giải ngân khi khóa học đã hoàn thành");
        }

        // 2. Kiểm tra số tiền cần phân chia (chống overflow)
        long tutorIncome = enrollment.getTutorIncome();
        long platformFee = enrollment.getPlatformFee();
        long totalPrice = enrollment.getTotalPrice();

        if (tutorIncome < 0 || platformFee < 0 || totalPrice <= 0) {
            throw new IllegalStateException("Số tiền phân chia không hợp lệ");
        }

        if (tutorIncome + platformFee != totalPrice) {
            throw new IllegalStateException("Số tiền phân chia không khớp với tổng giá");
        }

        // 3. Lấy giao dịch giữ tiền thành công và khóa bản ghi
        List<PaymentTransaction> holdTransactions = paymentTransactionRepository.findEnrollmentHoldForUpdate(
                enrollmentId, TransactionType.ESCROW_HOLD, TransactionStatus.SUCCESS);

        if (holdTransactions.size() != 1) {
            throw new IllegalStateException("Enrollment phải có đúng một giao dịch ESCROW_HOLD thành công");
        }

        PaymentTransaction holdTransaction = holdTransactions.getFirst();

        Wallet studentWallet = holdTransaction.getSourceWallet();

        if (studentWallet == null) {
            throw new IllegalStateException("Không tìm thấy ví đã giữ tiền của học sinh");
        }

        // 4. Tìm và khóa ví gia sư
        String tutorId = enrollment.getCourse().getTutor().getId();

        Wallet tutorWallet = walletRepository
                .findByUserIdAndTypeForUpdate(tutorId, WalletType.NORMAL)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy ví của gia sư"));

        // 5. Tìm và khóa ví nền tảng```
        List<Wallet> platformWallets = walletRepository.findByTypeForUpdate(WalletType.PLATFORM);

        if (platformWallets.size() != 1) {
            throw new IllegalStateException("Hệ thống phải có đúng một ví nền tảng");
        }

        Wallet platformWallet = platformWallets.getFirst();

        // 6. Khóa ví học sinh
        studentWallet = walletRepository
                .findByIdForUpdate(studentWallet.getId())
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy ví học sinh"));

        // 7. Chống giải ngân trùng
        boolean tutorAlreadyReleased =
                paymentTransactionRepository.existsByEnrollment_IdAndTypeAndStatusAndDestinationWallet_Id(
                        enrollmentId, TransactionType.ESCROW_RELEASE, TransactionStatus.SUCCESS, tutorWallet.getId());

        boolean platformAlreadyReleased =
                paymentTransactionRepository.existsByEnrollment_IdAndTypeAndStatusAndDestinationWallet_Id(
                        enrollmentId,
                        TransactionType.ESCROW_RELEASE,
                        TransactionStatus.SUCCESS,
                        platformWallet.getId());

        if (tutorAlreadyReleased || platformAlreadyReleased) {
            throw new IllegalStateException("Enrollment đã có giao dịch giải ngân; cần kiểm tra trạng thái giao dịch");
        }

        // 8. Kiểm tra hold amount khớp với total price
        if (holdTransaction.getAmount() != totalPrice) {
            throw new IllegalStateException("Hold amount không khớp với tổng giá của enrollment");
        }

        // 9. Kiểm tra tiền đang giữ
        if (studentWallet.getHoldingBalance() < totalPrice) {
            throw new IllegalStateException("Số dư đang giữ không đủ để giải ngân");
        }

        // 10. Chuyển tiền từ số dư đang giữ (chống overflow)
        studentWallet.setHoldingBalance(Math.subtractExact(studentWallet.getHoldingBalance(), totalPrice));

        tutorWallet.setAvailableBalance(Math.addExact(tutorWallet.getAvailableBalance(), tutorIncome));

        platformWallet.setAvailableBalance(Math.addExact(platformWallet.getAvailableBalance(), platformFee));

        walletRepository.save(studentWallet);
        walletRepository.save(tutorWallet);
        walletRepository.save(platformWallet);

        // 11. Tạo giao dịch giải ngân cho gia sư
        PaymentTransaction tutorRelease = PaymentTransaction.builder()
                .sourceWallet(studentWallet)
                .destinationWallet(tutorWallet)
                .enrollment(enrollment)
                .type(TransactionType.ESCROW_RELEASE)
                .status(TransactionStatus.SUCCESS)
                .provider(PaymentGatewayType.INTERNAL)
                .referenceCode(UUID.randomUUID().toString())
                .amount(tutorIncome)
                .balanceAfter(tutorWallet.getAvailableBalance())
                .note("Giải ngân thu nhập gia sư cho enrollment " + enrollmentId)
                .build();

        // 12. Tạo giao dịch phí nền tảng
        PaymentTransaction platformRelease = PaymentTransaction.builder()
                .sourceWallet(studentWallet)
                .destinationWallet(platformWallet)
                .enrollment(enrollment)
                .type(TransactionType.ESCROW_RELEASE)
                .status(TransactionStatus.SUCCESS)
                .provider(PaymentGatewayType.INTERNAL)
                .referenceCode(UUID.randomUUID().toString())
                .amount(platformFee)
                .balanceAfter(platformWallet.getAvailableBalance())
                .note("Thu phí nền tảng cho enrollment " + enrollmentId)
                .build();

        paymentTransactionRepository.saveAll(List.of(tutorRelease, platformRelease));
    }

    @Override
    @Transactional
    public void releaseBookingEscrow(Booking booking) {

        String bookingId = booking.getId();

        // 1. Chỉ giải ngân khi booking đã hoàn thành
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new IllegalStateException("Booking chưa hoàn thành, không thể giải ngân");
        }

        // 2. Kiểm tra số tiền phân chia (chống overflow)
        long totalPrice = booking.getTotalPrice();
        long tutorIncome = booking.getTutorIncome();
        long platformFee = booking.getPlatformFee();

        if (tutorIncome < 0 || platformFee < 0 || totalPrice <= 0) {
            throw new IllegalStateException("Số tiền phân chia của booking không hợp lệ");
        }

        if (tutorIncome + platformFee != totalPrice) {
            throw new IllegalStateException("Số tiền phân chia không khớp với tổng giá của booking");
        }

        // 3. Tìm giao dịch giữ tiền thành công
        List<PaymentTransaction> holdTransactions = paymentTransactionRepository.findBookingHoldForUpdate(
                bookingId, TransactionType.ESCROW_HOLD, TransactionStatus.SUCCESS);

        if (holdTransactions.size() != 1) {
            throw new IllegalStateException("Booking phải có đúng một giao dịch ESCROW_HOLD thành công");
        }

        PaymentTransaction holdTransaction = holdTransactions.getFirst();

        Wallet studentWallet = holdTransaction.getSourceWallet();

        if (studentWallet == null) {
            throw new IllegalStateException("Không tìm thấy ví học sinh đã giữ tiền");
        }

        // 4. Tìm và khóa ví gia sư
        String tutorId = booking.getTutor().getId();

        Wallet tutorWallet = walletRepository
                .findByUserIdAndTypeForUpdate(tutorId, WalletType.NORMAL)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy ví gia sư"));

        // 5. Tìm ví nền tảng
        // Giả định hệ thống chỉ có một ví PLATFORM.
        List<Wallet> platformWallets = walletRepository.findByTypeForUpdate(WalletType.PLATFORM);

        if (platformWallets.size() != 1) {
            throw new IllegalStateException("Hệ thống phải có đúng một ví nền tảng");
        }

        Wallet platformWallet = platformWallets.getFirst();

        // 6. Khóa ví học sinh trước khi cập nhật số dư
        studentWallet = walletRepository
                .findByIdForUpdate(studentWallet.getId())
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy ví học sinh"));

        // 7. Chống giải ngân trùng
        boolean tutorReleased = paymentTransactionRepository.existsByBooking_IdAndTypeAndStatusAndDestinationWallet_Id(
                bookingId, TransactionType.ESCROW_RELEASE, TransactionStatus.SUCCESS, tutorWallet.getId());

        boolean platformReleased =
                paymentTransactionRepository.existsByBooking_IdAndTypeAndStatusAndDestinationWallet_Id(
                        bookingId, TransactionType.ESCROW_RELEASE, TransactionStatus.SUCCESS, platformWallet.getId());

        if (tutorReleased || platformReleased) {
            throw new IllegalStateException("Booking đã có giao dịch giải ngân, cần kiểm tra dữ liệu");
        }

        // 8. Kiểm tra hold amount khớp với total price
        if (holdTransaction.getAmount() != totalPrice) {
            throw new IllegalStateException("Hold amount không khớp với tổng giá của booking");
        }

        // 9. Kiểm tra số dư đang giữ
        if (studentWallet.getHoldingBalance() < totalPrice) {
            throw new IllegalStateException("Số dư đang giữ không đủ để giải ngân");
        }

        // 10. Cập nhật số dư (chống overflow)
        studentWallet.setHoldingBalance(Math.subtractExact(studentWallet.getHoldingBalance(), totalPrice));

        tutorWallet.setAvailableBalance(Math.addExact(tutorWallet.getAvailableBalance(), tutorIncome));

        platformWallet.setAvailableBalance(Math.addExact(platformWallet.getAvailableBalance(), platformFee));

        walletRepository.save(studentWallet);
        walletRepository.save(tutorWallet);
        walletRepository.save(platformWallet);

        // 11. Tạo giao dịch giải ngân cho gia sư
        PaymentTransaction tutorRelease = PaymentTransaction.builder()
                .sourceWallet(studentWallet)
                .destinationWallet(tutorWallet)
                .booking(booking)
                .type(TransactionType.ESCROW_RELEASE)
                .status(TransactionStatus.SUCCESS)
                .referenceCode(UUID.randomUUID().toString())
                .amount(tutorIncome)
                .balanceAfter(tutorWallet.getAvailableBalance())
                .note("Giải ngân thu nhập gia sư cho booking " + bookingId)
                .build();

        // 12. Tạo giao dịch phí nền tảng
        PaymentTransaction platformRelease = PaymentTransaction.builder()
                .sourceWallet(studentWallet)
                .destinationWallet(platformWallet)
                .booking(booking)
                .type(TransactionType.ESCROW_RELEASE)
                .status(TransactionStatus.SUCCESS)
                .referenceCode(UUID.randomUUID().toString())
                .amount(platformFee)
                .balanceAfter(platformWallet.getAvailableBalance())
                .note("Thu phí nền tảng cho booking " + bookingId)
                .build();

        paymentTransactionRepository.saveAll(List.of(tutorRelease, platformRelease));
    }
}
