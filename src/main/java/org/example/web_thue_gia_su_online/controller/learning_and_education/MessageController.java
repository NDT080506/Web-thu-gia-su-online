package org.example.web_thue_gia_su_online.controller.learning_and_education;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.MessageResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.MessageService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {

    MessageService messageService;

    // Message creation chỉ qua WebSocket (ChatWebSocketController)
    // REST endpoint chỉ dùng cho update và delete

    @PatchMapping("/{id}")
    public ApiResponse<MessageResponse> update(
            @PathVariable String id, @RequestBody @Valid MessageUpdateRequest request) {
        return ApiResponse.<MessageResponse>builder()
                .result(messageService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        messageService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Message deleted successfully")
                .build();
    }
}
