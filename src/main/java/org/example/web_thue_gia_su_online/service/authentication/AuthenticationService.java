package org.example.web_thue_gia_su_online.service.authentication;

import java.text.ParseException;

import org.example.web_thue_gia_su_online.dto.request.authentication.AuthenticationRequest;
import org.example.web_thue_gia_su_online.dto.request.authentication.IntrospectRequest;
import org.example.web_thue_gia_su_online.dto.request.authentication.LogoutRequest;
import org.example.web_thue_gia_su_online.dto.request.authentication.RefreshTokenRequest;
import org.example.web_thue_gia_su_online.dto.response.authentication.AuthenticationResponse;
import org.example.web_thue_gia_su_online.dto.response.authentication.IntrospectResponse;
import org.example.web_thue_gia_su_online.dto.response.authentication.RefreshTokenResponse;

import com.nimbusds.jose.JOSEException;

public interface AuthenticationService {
    public IntrospectResponse introspect(IntrospectRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);

    public void logout(LogoutRequest request);

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;
}
