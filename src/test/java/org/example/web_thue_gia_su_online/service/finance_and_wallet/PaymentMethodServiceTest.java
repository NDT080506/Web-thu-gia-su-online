package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentMethodCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentMethodResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentMethod;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.mapper.finance_and_wallet.PaymentMethodMapper;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentMethodRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.impl.PaymentMethodServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentMethodServiceTest {

    @Mock
    private PaymentMethodRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentMethodMapper mapper;

    @InjectMocks
    private PaymentMethodServiceImpl service;

    @Test
    void createPaymentMethod_shouldReturnResponse() {
        User user = User.builder().id("user-1").build();
        PaymentMethodCreationRequest request = PaymentMethodCreationRequest.builder()
                .userId("user-1")
                .bankName("Vietcombank")
                .accountNumber("1234567890")
                .accountHolderName("Nguyen Van A")
                .isDefault(false)
                .build();

        PaymentMethod pm = PaymentMethod.builder().id("1").build();
        PaymentMethodResponse response = PaymentMethodResponse.builder().id("1").build();

        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(mapper.toEntity(request)).thenReturn(pm);
        when(repository.save(any())).thenReturn(pm);
        when(mapper.toResponse(any())).thenReturn(response);

        PaymentMethodResponse result = service.createPaymentMethod(request);

        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(repository).save(any());
    }

    @Test
    void createPaymentMethod_userNotFound_shouldThrow() {
        PaymentMethodCreationRequest request = PaymentMethodCreationRequest.builder()
                .userId("user-not-found")
                .bankName("Vietcombank")
                .accountNumber("1234567890")
                .accountHolderName("Nguyen Van A")
                .build();

        when(userRepository.findById("user-not-found")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.createPaymentMethod(request));
    }

    @Test
    void getPaymentMethod_shouldReturnResponse() {
        PaymentMethod pm = PaymentMethod.builder().id("1").build();
        PaymentMethodResponse response = PaymentMethodResponse.builder().id("1").build();

        when(repository.findById("1")).thenReturn(Optional.of(pm));
        when(mapper.toResponse(pm)).thenReturn(response);

        PaymentMethodResponse result = service.getPaymentMethod("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getPaymentMethod_notFound_shouldThrow() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getPaymentMethod("1"));
    }

    @Test
    void getPaymentMethodsByUserId_shouldReturnList() {
        PaymentMethod pm = PaymentMethod.builder().id("1").build();
        PaymentMethodResponse response = PaymentMethodResponse.builder().id("1").build();

        when(repository.findByUserId("user-1")).thenReturn(List.of(pm));
        when(mapper.toResponse(pm)).thenReturn(response);

        List<PaymentMethodResponse> result = service.getPaymentMethodsByUserId("user-1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
    }

    @Test
    void setDefaultPaymentMethod_shouldReturnResponse() {
        User user = User.builder().id("user-1").build();
        PaymentMethod pm = PaymentMethod.builder().id("1").user(user).build();
        PaymentMethodResponse response =
                PaymentMethodResponse.builder().id("1").isDefault(true).build();

        when(repository.findById("1")).thenReturn(Optional.of(pm));
        when(repository.findByUserId("user-1")).thenReturn(List.of());
        when(repository.save(any())).thenReturn(pm);
        when(mapper.toResponse(any())).thenReturn(response);

        PaymentMethodResponse result = service.setDefaultPaymentMethod("1");

        assertNotNull(result);
        assertTrue(result.isDefault());
    }

    @Test
    void deletePaymentMethod_shouldSucceed() {
        PaymentMethod pm = PaymentMethod.builder().id("1").build();

        when(repository.findById("1")).thenReturn(Optional.of(pm));

        service.deletePaymentMethod("1");

        verify(repository).delete(pm);
    }

    @Test
    void deletePaymentMethod_notFound_shouldThrow() {
        when(repository.findById("not-found")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.deletePaymentMethod("not-found"));
    }
}
