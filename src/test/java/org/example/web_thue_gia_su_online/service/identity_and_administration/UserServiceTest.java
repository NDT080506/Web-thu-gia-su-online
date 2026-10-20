package org.example.web_thue_gia_su_online.service.identity_and_administration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.UserResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.mapper.identity_and_administration.UserMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.authentication.EmailVerificationService;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.WalletService;
import org.example.web_thue_gia_su_online.service.identity_and_administration.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private WalletService walletService;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailVerificationService emailVerificationService;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void createUser_shouldReturnResponse() {
        User user = User.builder().id("1").build();
        UserResponse response = UserResponse.builder().id("1").build();
        when(repository.existsByEmail(any())).thenReturn(false);
        when(repository.existsByDisplayName(any())).thenReturn(false);
        when(mapper.toUser(any())).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(repository.save(any())).thenReturn(user);
        when(mapper.toUserResponse(any())).thenReturn(response);
        doNothing().when(walletService).createWallet(any(), any());
        doNothing().when(emailVerificationService).sendVerificationEmail(any());

        UserResponse result = service.createUser(any());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void getUser_shouldReturnResponse() {
        User user = User.builder().id("1").build();
        UserResponse response = UserResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(user));
        when(mapper.toUserResponse(any())).thenReturn(response);

        UserResponse result = service.getUser("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getUser_notFound_shouldThrow() {
        when(repository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getUser("1"));
    }

    @Test
    void getUsers_shouldReturnList() {
        User user = User.builder().id("1").build();
        UserResponse response = UserResponse.builder().id("1").build();
        when(repository.findAll()).thenReturn(List.of(user));
        when(mapper.toUserResponse(any())).thenReturn(response);

        List<UserResponse> result = service.getUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateUser_shouldReturnResponse() {
        User user = User.builder().id("1").build();
        UserResponse response = UserResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(user));
        when(repository.save(any())).thenReturn(user);
        when(mapper.toUserResponse(any())).thenReturn(response);

        UserResponse result = service.updateUser(any(), "1");

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void deleteUser_shouldSucceed() {
        User user = User.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(user));
        doNothing().when(repository).delete(user);

        service.deleteUser("1");

        verify(repository).delete(user);
    }
}
