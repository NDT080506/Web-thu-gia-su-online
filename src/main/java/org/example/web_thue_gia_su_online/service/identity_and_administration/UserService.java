package org.example.web_thue_gia_su_online.service.identity_and_administration;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserUpdateRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserUpdateStatusRequest;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.UserResponse;

public interface UserService {

    public UserResponse createUser(UserCreationRequest request);

    public List<UserResponse> getUsers();

    public UserResponse getUser(String id);

    public UserResponse updateUser(UserUpdateRequest request, String id);

    public UserResponse updateUserAccountStatus(UserUpdateStatusRequest request, String id);

    public void deleteUser(String id);
}
