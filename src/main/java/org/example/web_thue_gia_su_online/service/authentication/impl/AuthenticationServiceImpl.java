package org.example.web_thue_gia_su_online.service.authentication.impl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.example.web_thue_gia_su_online.dto.request.authentication.AuthenticationRequest;
import org.example.web_thue_gia_su_online.dto.request.authentication.IntrospectRequest;
import org.example.web_thue_gia_su_online.dto.request.authentication.LogoutRequest;
import org.example.web_thue_gia_su_online.dto.request.authentication.RefreshTokenRequest;
import org.example.web_thue_gia_su_online.dto.response.authentication.AuthenticationResponse;
import org.example.web_thue_gia_su_online.dto.response.authentication.IntrospectResponse;
import org.example.web_thue_gia_su_online.dto.response.authentication.RefreshTokenResponse;
import org.example.web_thue_gia_su_online.entity.authentication.InvalidatedToken;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.AccountStatus;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.repository.authentication.InvalidatedTokenRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signer-key}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        String token = request.getAccessToken();
        boolean isValid = true;

        try {
            SignedJWT signedJWT = verifyToken(token, "access");
            String userId = signedJWT.getJWTClaimsSet().getSubject();
            User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            isValid = user.getStatus() == AccountStatus.ACTIVE;

        } catch (AppException | JOSEException | ParseException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // log.info(request.getDisplayName());
        User user = userRepository
                .findByDisplayName(request.getDisplayName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String accessToken = generateToken(user, "access", VALID_DURATION);

        String refreshToken = generateToken(user, "refresh", REFRESHABLE_DURATION);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    @Override
    public void logout(LogoutRequest request) {
        try {
            SignedJWT accessToken = verifyToken(request.getAccessToken(), "access");

            SignedJWT refreshToken = verifyToken(request.getRefreshToken(), "refresh");

            JWTClaimsSet accessClaims = accessToken.getJWTClaimsSet();
            JWTClaimsSet refreshClaims = refreshToken.getJWTClaimsSet();

            // Hai token phải thuộc cùng một tài khoản
            if (!accessClaims.getSubject().equals(refreshClaims.getSubject())) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            String userId = accessClaims.getSubject();

            User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            // Thu hồi Access Token
            invalidatedTokenRepository.save(InvalidatedToken.builder()
                    .id(accessClaims.getJWTID())
                    .user(user)
                    .expiryTime(accessClaims.getExpirationTime())
                    .build());

            // Thu hồi Refresh Token
            invalidatedTokenRepository.save(InvalidatedToken.builder()
                    .id(refreshClaims.getJWTID())
                    .user(user)
                    .expiryTime(refreshClaims.getExpirationTime())
                    .build());

        } catch (AppException | ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        SignedJWT signedToken = verifyToken(request.getToken(), "refresh");

        String jit = signedToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();
        String userId = signedToken.getJWTClaimsSet().getSubject();
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .user(user)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        String accessToken = generateToken(user, "access", VALID_DURATION);

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .authenticated(true)
                .build();
    }

    public SignedJWT verifyToken(String token, String expectedType) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        boolean verified = signedJWT.verify(verifier);

        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

        Date expiredTime = claims.getExpirationTime();
        String tokenType = claims.getStringClaim("token_type");
        String jit = claims.getJWTID();

        if (!(verified && expiredTime.after(new Date()) && expectedType.equals(tokenType)))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(jit)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    public String generateToken(User user, String tokenType, long duration) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("my-app-backend")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(duration, ChronoUnit.DAYS)))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .claim("token_type", tokenType)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        // Ký token
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new RuntimeException(e);
        }
    }

    public String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        //                if (!CollectionUtils.isEmpty(user.getRole().value())) {
        //                    user.getRoles().forEach(role -> {
        //                        stringJoiner.add("ROLE_" + role.getName());
        //                        if (!CollectionUtils.isEmpty(role.getPermissions()))
        //                            role.getPermissions().forEach(permission ->
        // stringJoiner.add(permission.getName()));
        //                    });
        //                }

        stringJoiner.add("ROLE_" + user.getRole().toString());

        return stringJoiner.toString();
    }
}
