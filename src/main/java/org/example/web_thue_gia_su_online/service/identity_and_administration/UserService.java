package org.example.web_thue_gia_su_online.service.identity_and_administration;

import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserUpdateRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserUpdateStatusRequest;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    public UserResponse createUser(UserCreationRequest request);

    public Page<UserResponse> getUsers(Pageable pageable);

    public Page<UserResponse> getTutors(Pageable pageable);

    public UserResponse getMyUser();

    public UserResponse getUser(String id);

    public UserResponse updateUser(UserUpdateRequest request, String id);

    public UserResponse updateUserAccountStatus(UserUpdateStatusRequest request, String id);

    public void deleteUser(String id);
}
