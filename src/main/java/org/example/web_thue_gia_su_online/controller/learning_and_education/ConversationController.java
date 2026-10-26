package org.example.web_thue_gia_su_online.controller.learning_and_education;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ConversationCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.ConversationResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.ConversationService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conversations")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationController {
    ConversationService conversationService;

    @PostMapping("/direct")
    public ApiResponse<ConversationResponse> create(@RequestBody @Valid ConversationCreationRequest request) {

        return ApiResponse.<ConversationResponse>builder()
                .result(conversationService.create(request))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<List<ConversationResponse>> getMyConversations() {
        return ApiResponse.<List<ConversationResponse>>builder()
                .result(conversationService.getMyConversations())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ConversationResponse> getById(@PathVariable String id) {

        return ApiResponse.<ConversationResponse>builder()
                .result(conversationService.getById(id))
                .build();
    }

    //    @PatchMapping("/{id}/agreement/tutor")
    //    public ApiResponse<ConversationResponse> agreeAsTutor(
    //            @PathVariable String id) {
    //
    //        return ApiResponse.<ConversationResponse>builder()
    //                .result(conversationService.agreeAsTutor(id))
    //                .build();
    //    }

    @PatchMapping("/{id}/agreement/student")
    public ApiResponse<ConversationResponse> agreeAsStudent(@PathVariable String id) {

        return ApiResponse.<ConversationResponse>builder()
                .result(conversationService.agreeAsStudent(id))
                .build();
    }

    @PatchMapping("/{id}/decline/student")
    public ApiResponse<ConversationResponse> decline(@PathVariable String id) {

        return ApiResponse.<ConversationResponse>builder()
                .result(conversationService.declineAsStudent(id))
                .build();
    }
}
