package org.example.web_thue_gia_su_online.service.authentication.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

import org.example.web_thue_gia_su_online.service.authentication.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final ObjectMapper objectMapper;

    private final HttpClient httpClient =
            HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.backend-url}")
    private String backendUrl;

    @Value("${app.mail.Gmail.client-id}")
    private String clientId;

    @Value("${app.mail.Gmail.client-secret}")
    private String clientSecret;

    @Value("${app.mail.Gmail.refresh-token}")
    private String refreshToken;

    private volatile String cachedAccessToken;
    private volatile Instant tokenExpiresAt = Instant.EPOCH;

    /**
     * Get a valid access token.
     * Reuse cached token until it is close to expiration.
     */
    private String getAccessToken() throws IOException, InterruptedException {

        if (isAccessTokenValid()) {
            return cachedAccessToken;
        }

        synchronized (this) {
            if (isAccessTokenValid()) {
                return cachedAccessToken;
            }

            String formData = "client_id=" + encode(clientId)
                    + "&client_secret=" + encode(clientSecret)
                    + "&refresh_token=" + encode(refreshToken)
                    + "&grant_type=refresh_token";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://oauth2.googleapis.com/token"))
                    .timeout(Duration.ofSeconds(20))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(formData, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                log.error("Google token refresh failed: status={}, body={}", response.statusCode(), response.body());

                throw new IllegalStateException("Could not refresh Gmail API access token");
            }

            JsonNode json = objectMapper.readTree(response.body());

            String accessToken = json.path("access_token").asText();
            long expiresIn = json.path("expires_in").asLong(3600);

            if (accessToken.isBlank()) {
                throw new IllegalStateException("Google did not return an access token");
            }

            cachedAccessToken = accessToken;
            tokenExpiresAt = Instant.now().plusSeconds(expiresIn);

            log.info("Gmail API access token refreshed successfully");

            return cachedAccessToken;
        }
    }

    private boolean isAccessTokenValid() {
        return cachedAccessToken != null && Instant.now().isBefore(tokenExpiresAt.minusSeconds(60));
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /**
     * Create MIME email and encode it using Base64URL.
     */
    private String createRawMessage(String to, String content) {

        validateHeader(to);
        validateHeader(fromEmail);

        String encodedContent = Base64.getMimeEncoder(76, "\r\n".getBytes(StandardCharsets.US_ASCII))
                .encodeToString(content.getBytes(StandardCharsets.UTF_8));

        String mimeMessage = "To: " + to + "\r\n"
                + "From: " + fromEmail + "\r\n"
                + "Subject: Verify your email - EduTutor\r\n"
                + "MIME-Version: 1.0\r\n"
                + "Content-Type: text/plain; charset=UTF-8\r\n"
                + "Content-Transfer-Encoding: base64\r\n"
                + "\r\n"
                + encodedContent;

        return Base64.getUrlEncoder().withoutPadding().encodeToString(mimeMessage.getBytes(StandardCharsets.UTF_8));
    }

    private void validateHeader(String value) {
        if (value == null || value.isBlank() || value.contains("\r") || value.contains("\n")) {
            throw new IllegalArgumentException("Invalid email header");
        }
    }

    private HttpResponse<String> sendRequest(String accessToken, String requestBody)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://gmail.googleapis.com/gmail/v1/users/me/messages/send"))
                .timeout(Duration.ofSeconds(30))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public void sendVerificationEmail(String email, String token) {

        String baseUrl = backendUrl.replaceAll("/+$", "");

        String link = baseUrl + "/giasu/auth/verify-email?token=" + encode(token);

        String content = """
				Chào bạn,

				Vui lòng nhấn vào đường link dưới đây để xác thực tài khoản của bạn:
				%s

				Đường link này sẽ hết hạn trong 15 phút.

				Trân trọng,
				Đội ngũ EduTutor
				""".formatted(link);

        try {
            log.info("Sending verification email from {} to {}", fromEmail, email);

            String rawMessage = createRawMessage(email, content);

            String requestBody = objectMapper.writeValueAsString(Map.of("raw", rawMessage));

            String accessToken = getAccessToken();

            HttpResponse<String> response = sendRequest(accessToken, requestBody);

            // If the access token was rejected, refresh and retry once.
            if (response.statusCode() == 401) {

                synchronized (this) {
                    cachedAccessToken = null;
                    tokenExpiresAt = Instant.EPOCH;
                }

                accessToken = getAccessToken();

                response = sendRequest(accessToken, requestBody);
            }

            if (response.statusCode() < 200 || response.statusCode() >= 300) {

                log.error("Gmail API failed: status={}, body={}", response.statusCode(), response.body());

                throw new IllegalStateException("Could not send verification email");
            }

            JsonNode result = objectMapper.readTree(response.body());

            log.info(
                    "Verification email sent successfully. Message ID: {}",
                    result.path("id").asText());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            log.error("Gmail API request was interrupted", e);

            throw new IllegalStateException("Could not send verification email", e);

        } catch (IOException e) {
            log.error("Gmail API communication failed", e);

            throw new IllegalStateException("Could not send verification email", e);
        }
    }
}
