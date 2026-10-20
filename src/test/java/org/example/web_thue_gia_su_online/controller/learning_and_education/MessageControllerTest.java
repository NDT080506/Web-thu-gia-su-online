package org.example.web_thue_gia_su_online.controller.learning_and_education;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.MessageResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController controller;

    @Test
    void create_shouldReturnResponse() {
        MessageCreationRequest request = MessageCreationRequest.builder()
                .bookingId("booking-1")
                .senderId("user-1")
                .content("Hello")
                .build();
        MessageResponse response = MessageResponse.builder().id(1L).build();
        when(messageService.create(eq(request))).thenReturn(response);
        ApiResponse<MessageResponse> apiResponse = controller.create(request);
        assertNotNull(apiResponse);
        verify(messageService).create(eq(request));
    }

    @Test
    void getByBookingId_shouldReturnList() {
        List<MessageResponse> list = List.of(MessageResponse.builder().id(1L).build());
        when(messageService.getByBookingId("booking-1")).thenReturn(list);
        ApiResponse<List<MessageResponse>> apiResponse = controller.getByBookingId("booking-1");
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }

    @Test
    void update_shouldReturnResponse() {
        MessageUpdateRequest request =
                MessageUpdateRequest.builder().content("Updated message").build();
        MessageResponse response = MessageResponse.builder().id(1L).build();
        when(messageService.update(eq(1L), eq(request))).thenReturn(response);
        ApiResponse<MessageResponse> apiResponse = controller.update(1L, request);
        assertNotNull(apiResponse);
        verify(messageService).update(eq(1L), eq(request));
    }

    @Test
    void delete_shouldReturnSuccessMessage() {
        doNothing().when(messageService).delete(1L);
        ApiResponse<Void> apiResponse = controller.delete(1L);
        assertNotNull(apiResponse);
        assertEquals("Message deleted successfully", apiResponse.getMessage());
    }
}
