package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BookingResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.BrokeragePackage;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Conversation;
import org.example.web_thue_gia_su_online.enums.BookingStatus;
import org.example.web_thue_gia_su_online.enums.ConversationStatus;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.BookingMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BookingRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BrokeragePackageRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.ConversationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    BrokeragePackageRepository packageRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ConversationRepository conversationRepository;

    @Mock
    BookingMapper bookingMapper;

    @InjectMocks
    BookingServiceImpl bookingService;

    private String studentId;
    private String tutorId;
    private String packageId;
    private String conversationId;

    @BeforeEach
    void setUp() {
        studentId = "student-uuid-123";
        tutorId = "tutor-uuid-456";
        packageId = "package-uuid-789";
        conversationId = "conversation-uuid-000";
    }

    private void setAuthenticatedUser(String userId) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(userId);
        SecurityContextHolder.setContext(new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                return authentication;
            }

            @Override
            public void setAuthentication(Authentication authentication) {}
        });
    }

    private User createStudent() {
        User user = User.builder().id(studentId).build();
        return user;
    }

    private User createTutor() {
        User user = User.builder().id(tutorId).build();
        return user;
    }

    private BrokeragePackage createActivePackage() {
        return BrokeragePackage.builder()
                .id(packageId)
                .name("Basic Package")
                .price(100000)
                .platformFeePercent(10)
                .active(true)
                .tutor(createTutor())
                .build();
    }

    private Conversation createAgreedConversation() {
        return Conversation.builder()
                .id(conversationId)
                .student(createStudent())
                .tutor(createTutor())
                .status(ConversationStatus.AGREED)
                .build();
    }

    private Booking createBooking() {
        return Booking.builder()
                .id("booking-uuid-111")
                .student(createStudent())
                .tutor(createTutor())
                .brokeragePackage(createActivePackage())
                .conversation(createAgreedConversation())
                .status(BookingStatus.PENDING_PAYMENT)
                .totalPrice(100000)
                .platformFee(10000)
                .tutorIncome(90000)
                .build();
    }

    @Test
    void create_bookingSuccess_whenConversationAgreed() {
        setAuthenticatedUser(studentId);

        BookingCreationRequest request = new BookingCreationRequest();
        request.setPackageId(packageId);

        BrokeragePackage pkg = createActivePackage();
        User student = createStudent();
        Conversation conversation = createAgreedConversation();
        Booking savedBooking = createBooking();

        when(packageRepository.findById(packageId)).thenReturn(Optional.of(pkg));
        when(userRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(conversationRepository.findByStudent_IdAndTutor_IdOrderByUpdatedAtDesc(studentId, tutorId))
                .thenReturn(Optional.of(conversation));
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);
        when(bookingMapper.toResponse(savedBooking)).thenReturn(new BookingResponse());

        BookingResponse response = bookingService.create(request);

        assertThat(response).isNotNull();
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void create_bookingFails_whenConversationNotAgreed() {
        setAuthenticatedUser(studentId);

        BookingCreationRequest request = new BookingCreationRequest();
        request.setPackageId(packageId);

        Conversation negotiatingConversation = Conversation.builder()
                .id(conversationId)
                .student(createStudent())
                .tutor(createTutor())
                .status(ConversationStatus.NEGOTIATING)
                .build();

        when(packageRepository.findById(packageId)).thenReturn(Optional.of(createActivePackage()));
        when(userRepository.findById(studentId)).thenReturn(Optional.of(createStudent()));
        when(conversationRepository.findByStudent_IdAndTutor_IdOrderByUpdatedAtDesc(studentId, tutorId))
                .thenReturn(Optional.of(negotiatingConversation));

        assertThatThrownBy(() -> bookingService.create(request)).isInstanceOf(AppException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void create_bookingFails_whenPackageNotFound() {
        setAuthenticatedUser(studentId);

        BookingCreationRequest request = new BookingCreationRequest();
        request.setPackageId("non-existent");

        lenient()
                .when(packageRepository.findById("non-existent"))
                .thenThrow(new AppException(org.example.web_thue_gia_su_online.exception.ErrorCode.BOOKING_NOT_FOUND));

        assertThatThrownBy(() -> bookingService.create(request)).isInstanceOf(AppException.class);
    }

    @Test
    void create_bookingFails_whenPackageInactive() {
        setAuthenticatedUser(studentId);

        BookingCreationRequest request = new BookingCreationRequest();
        request.setPackageId(packageId);

        BrokeragePackage inactivePkg = BrokeragePackage.builder()
                .id(packageId)
                .active(false)
                .tutor(createTutor())
                .build();

        when(packageRepository.findById(packageId)).thenReturn(Optional.of(inactivePkg));
        when(userRepository.findById(studentId)).thenReturn(Optional.of(createStudent()));

        assertThatThrownBy(() -> bookingService.create(request)).isInstanceOf(AppException.class);
    }

    @Test
    void create_bookingUsesPackagePrice_notClientPrice() {
        setAuthenticatedUser(studentId);

        BookingCreationRequest request = new BookingCreationRequest();
        request.setPackageId(packageId);

        BrokeragePackage pkg = BrokeragePackage.builder()
                .id(packageId)
                .price(200000)
                .platformFeePercent(15)
                .active(true)
                .tutor(createTutor())
                .build();

        Conversation conversation = createAgreedConversation();
        Booking savedBooking = Booking.builder()
                .id("booking-uuid-222")
                .totalPrice(200000)
                .platformFee(30000)
                .tutorIncome(170000)
                .build();

        when(packageRepository.findById(packageId)).thenReturn(Optional.of(pkg));
        when(userRepository.findById(studentId)).thenReturn(Optional.of(createStudent()));
        when(conversationRepository.findByStudent_IdAndTutor_IdOrderByUpdatedAtDesc(studentId, tutorId))
                .thenReturn(Optional.of(conversation));
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);
        when(bookingMapper.toResponse(savedBooking)).thenReturn(new BookingResponse());

        bookingService.create(request);

        verify(bookingRepository)
                .save(argThat(booking -> booking.getTotalPrice() == 200000
                        && booking.getPlatformFee() == 30000
                        && booking.getTutorIncome() == 170000));
    }

    @Test
    void getById_success_whenStudentOwnsBooking() {
        setAuthenticatedUser(studentId);

        Booking booking = createBooking();
        when(bookingRepository.findById("booking-1")).thenReturn(Optional.of(booking));
        when(bookingMapper.toResponse(booking)).thenReturn(new BookingResponse());

        BookingResponse response = bookingService.getById("booking-1");

        assertThat(response).isNotNull();
    }

    @Test
    void getById_fails_whenNotOwner() {
        setAuthenticatedUser("other-user-id");

        Booking booking = createBooking();
        when(bookingRepository.findById("booking-1")).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.getById("booking-1")).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void update_cancelSuccess_whenStudentOwnsBooking() {
        setAuthenticatedUser(studentId);

        Booking booking = createBooking();
        when(bookingRepository.findById("booking-1")).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toResponse(booking)).thenReturn(new BookingResponse());

        BookingResponse response = bookingService.update("booking-1");

        assertThat(response).isNotNull();
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELLED);
    }

    @Test
    void update_cancelFails_whenNotOwner() {
        setAuthenticatedUser("other-user-id");

        Booking booking = createBooking();
        when(bookingRepository.findById("booking-1")).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.update("booking-1")).isInstanceOf(AccessDeniedException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void create_bookingLinksConversation() {
        setAuthenticatedUser(studentId);

        BookingCreationRequest request = new BookingCreationRequest();
        request.setPackageId(packageId);

        Conversation conversation = createAgreedConversation();
        Booking savedBooking = createBooking();

        when(packageRepository.findById(packageId)).thenReturn(Optional.of(createActivePackage()));
        when(userRepository.findById(studentId)).thenReturn(Optional.of(createStudent()));
        when(conversationRepository.findByStudent_IdAndTutor_IdOrderByUpdatedAtDesc(studentId, tutorId))
                .thenReturn(Optional.of(conversation));
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);
        when(bookingMapper.toResponse(savedBooking)).thenReturn(new BookingResponse());

        bookingService.create(request);

        verify(bookingRepository)
                .save(argThat(booking -> booking.getConversation() != null
                        && booking.getConversation().getId().equals(conversationId)));
    }
}
