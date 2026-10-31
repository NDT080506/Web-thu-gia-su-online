package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentMethodCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentMethodResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentMethod;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.finance_and_wallet.PaymentMethodMapper;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentMethodRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.PaymentMethodService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentMethodServiceImpl implements PaymentMethodService {

    PaymentMethodRepository paymentMethodRepository;
    UserRepository userRepository;
    PaymentMethodMapper paymentMethodMapper;

    @Override
    @Transactional
    public PaymentMethodResponse createPaymentMethod(PaymentMethodCreationRequest request) {
        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        PaymentMethod paymentMethod = paymentMethodMapper.toEntity(request);
        paymentMethod.setUser(user);

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            unsetExistingDefaults(user.getId());
            paymentMethod.setDefault(true);
        }

        return paymentMethodMapper.toResponse(paymentMethodRepository.save(paymentMethod));
    }

    @Override
    public PaymentMethodResponse getPaymentMethod(String id) {
        PaymentMethod paymentMethod = paymentMethodRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found: " + id));
        return paymentMethodMapper.toResponse(paymentMethod);
    }

    @Override
    public List<PaymentMethodResponse> getPaymentMethodsByUserId(String userId) {
        return paymentMethodRepository.findByUserId(userId).stream()
                .map(paymentMethodMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public PaymentMethodResponse setDefaultPaymentMethod(String id) {
        PaymentMethod paymentMethod = paymentMethodRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found: " + id));

        unsetExistingDefaults(paymentMethod.getUser().getId());
        paymentMethod.setDefault(true);

        return paymentMethodMapper.toResponse(paymentMethodRepository.save(paymentMethod));
    }

    @Override
    @Transactional
    public void deletePaymentMethod(String id) {
        PaymentMethod paymentMethod = paymentMethodRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found: " + id));
        paymentMethodRepository.delete(paymentMethod);
    }

    private void unsetExistingDefaults(String userId) {
        List<PaymentMethod> userMethods = paymentMethodRepository.findByUserId(userId);
        for (PaymentMethod pm : userMethods) {
            if (pm.isDefault()) {
                pm.setDefault(false);
                paymentMethodRepository.save(pm);
            }
        }
    }
}
