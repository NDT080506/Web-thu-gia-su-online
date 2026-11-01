package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import java.util.Objects;
import java.util.UUID;

import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.BookingPaymentResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.enums.BookingStatus;
import org.example.web_thue_gia_su_online.enums.TransactionStatus;
import org.example.web_thue_gia_su_online.enums.TransactionType;
import org.example.web_thue_gia_su_online.enums.WalletType;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentTransactionRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BookingRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.BookingPaymentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingPaymentServiceImpl implements BookingPaymentService {

    BookingRepository bookingRepository;
    WalletRepository walletRepository;
    PaymentTransactionRepository paymentTransactionRepository;

    private String getCurrentUserId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
    }

    @Override
    @Transactional
    public BookingPaymentResponse payForBooking(String bookingId) {
        String userId = getCurrentUserId();

        // 1. Tìm và khóa booking.
        Booking booking = bookingRepository
                .findByIdForUpdate(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        // 2. Chỉ học viên sở hữu booking mới được thanh toán.
        if (booking.getStudent() == null || !userId.equals(booking.getStudent().getId())) {
            throw new IllegalArgumentException("Bạn không có quyền thanh toán booking này");
        }

        // 3. Chỉ booking đang chờ thanh toán mới được xử lý.
        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Booking không ở trạng thái chờ thanh toán");
        }

        // 4. Kiểm tra số tiền.
        long amount = booking.getTotalPrice();

        if (amount <= 0) {
            throw new IllegalStateException("Số tiền thanh toán không hợp lệ");
        }

        // 5. Kiểm tra giao dịch giữ tiền đã thành công chưa.
        boolean alreadyHeld = paymentTransactionRepository.existsByBooking_IdAndTypeAndStatus(
                bookingId, TransactionType.ESCROW_HOLD, TransactionStatus.SUCCESS);

        if (alreadyHeld) {
            throw new IllegalStateException("Booking này đã được thanh toán");
        }

        // 6. Tìm và khóa ví của học viên.
        Wallet wallet = walletRepository
                .findByUserIdAndTypeForUpdate(userId, WalletType.NORMAL)
                .orElseThrow(() -> new IllegalStateException("Bạn chưa có ví thanh toán"));

        // 7. Kiểm tra số dư khả dụng.
        if (wallet.getAvailableBalance() < amount) {
            throw new IllegalStateException("Số dư khả dụng không đủ để thanh toán");
        }

        // 8. Chuyển tiền từ khả dụng sang đang giữ (chống overflow).
        wallet.setAvailableBalance(Math.subtractExact(wallet.getAvailableBalance(), amount));

        wallet.setHoldingBalance(Math.addExact(wallet.getHoldingBalance(), amount));

        // 9. Tạo giao dịch giữ tiền.
        PaymentTransaction transaction = PaymentTransaction.builder()
                .sourceWallet(wallet)
                .destinationWallet(null)
                .booking(booking)
                .type(TransactionType.ESCROW_HOLD)
                .status(TransactionStatus.SUCCESS)
                .referenceCode(UUID.randomUUID().toString())
                .amount(amount)
                .balanceAfter(wallet.getAvailableBalance())
                .note("Giữ tiền thanh toán cho booking " + bookingId)
                .build();

        PaymentTransaction savedTransaction = paymentTransactionRepository.save(transaction);

        // 10. Chuyển booking sang PAID — tiền đã được giữ trong escrow.
        booking.setStatus(BookingStatus.PENDING_CONFIRMATION);
        bookingRepository.save(booking);

        // 11. Lưu wallet (bắt buộc để persist thay đổi balance)
        walletRepository.save(wallet);

        // 12. Trả kết quả.
        return new BookingPaymentResponse(
                savedTransaction.getId(),
                booking.getId(),
                savedTransaction.getReferenceCode(),
                amount,
                wallet.getAvailableBalance(),
                wallet.getHoldingBalance(),
                savedTransaction.getType(),
                savedTransaction.getStatus(),
                savedTransaction.getCreatedAt());
    }
}
