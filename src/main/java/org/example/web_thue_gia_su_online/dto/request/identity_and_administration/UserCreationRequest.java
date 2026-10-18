package org.example.web_thue_gia_su_online.dto.request.identity_and_administration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.example.web_thue_gia_su_online.enums.UserRole;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    String password;

    @NotBlank(message = "Display name is required")
    String displayName;

    String avatarUrl;

    @Builder.Default
    UserRole role = UserRole.STUDENT;

    String phone;
    String zaloLink;
}
