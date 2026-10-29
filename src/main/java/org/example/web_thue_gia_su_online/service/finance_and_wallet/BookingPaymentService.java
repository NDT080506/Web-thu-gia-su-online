package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.BookingPaymentResponse;

public interface BookingPaymentService {
    BookingPaymentResponse payForBooking(String bookingId);
}
