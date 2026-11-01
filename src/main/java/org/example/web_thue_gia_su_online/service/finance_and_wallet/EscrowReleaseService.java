package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;

public interface EscrowReleaseService {
    void releaseEnrollmentEscrow(CourseEnrollment enrollment);

    void releaseBookingEscrow(Booking booking);
}
