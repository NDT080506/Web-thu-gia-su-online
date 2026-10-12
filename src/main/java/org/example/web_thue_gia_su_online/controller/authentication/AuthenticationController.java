package org.example.web_thue_gia_su_online.controller.authentication;

import java.text.ParseException;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.authentication.AuthenticationRequest;
import org.example.web_thue_gia_su_online.dto.request.authentication.IntrospectRequest;
import org.example.web_thue_gia_su_online.dto.request.authentication.LogoutRequest;
import org.example.web_thue_gia_su_online.dto.request.authentication.RefreshTokenRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.authentication.AuthenticationResponse;
import org.example.web_thue_gia_su_online.dto.response.authentication.IntrospectResponse;
import org.example.web_thue_gia_su_online.dto.response.authentication.RefreshTokenResponse;
import org.example.web_thue_gia_su_online.service.authentication.AuthenticationService;
import org.springframework.web.bind.annotation.*;

import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody @Valid IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(request))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody @Valid LogoutRequest request) {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().message("Token has been log out").build();
    }

    @PostMapping("/refresh")
    ApiResponse<RefreshTokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request)
            throws ParseException, JOSEException {
        return ApiResponse.<RefreshTokenResponse>builder()
                .result(authenticationService.refreshToken(request))
                .build();
    }
}
