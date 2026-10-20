package org.example.web_thue_gia_su_online.controller.identity_and_administration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.ActionLogCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse;
import org.example.web_thue_gia_su_online.service.identity_and_administration.ActionLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ActionLogControllerTest {

    @Mock
    private ActionLogService actionLogService;

    @InjectMocks
    private ActionLogController actionLogController;

    @Test
    void logAction_shouldReturnResponse() {
        ActionLogCreationRequest request = ActionLogCreationRequest.builder()
                .adminId("admin-1")
                .targetUserId("user-1")
                .actionCode("BAN")
                .reason("Test reason")
                .build();
        ActionLogResponse response = ActionLogResponse.builder().id("1").build();
        when(actionLogService.logAction(eq(request))).thenReturn(response);
        ApiResponse<ActionLogResponse> apiResponse = actionLogController.logAction(request);
        assertNotNull(apiResponse);
        verify(actionLogService).logAction(eq(request));
    }

    @Test
    void getLog_shouldReturnResponse() {
        ActionLogResponse response = ActionLogResponse.builder().id("1").build();
        when(actionLogService.getLog("1")).thenReturn(response);
        ApiResponse<ActionLogResponse> apiResponse = actionLogController.getLog("1");
        assertNotNull(apiResponse);
        verify(actionLogService).getLog("1");
    }

    @Test
    void getLogsByTargetUser_shouldReturnList() {
        List<ActionLogResponse> list =
                List.of(ActionLogResponse.builder().id("1").build());
        when(actionLogService.getLogsByTargetUser("user-1")).thenReturn(list);
        ApiResponse<List<ActionLogResponse>> apiResponse = actionLogController.getLogsByTargetUser("user-1");
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }

    @Test
    void getLogsByAdmin_shouldReturnList() {
        List<ActionLogResponse> list =
                List.of(ActionLogResponse.builder().id("1").build());
        when(actionLogService.getLogsByAdmin("admin-1")).thenReturn(list);
        ApiResponse<List<ActionLogResponse>> apiResponse = actionLogController.getLogsByAdmin("admin-1");
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }

    @Test
    void getAllLogs_shouldReturnList() {
        List<ActionLogResponse> list =
                List.of(ActionLogResponse.builder().id("1").build());
        when(actionLogService.getAllLogs()).thenReturn(list);
        ApiResponse<List<ActionLogResponse>> apiResponse = actionLogController.getAllLogs();
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }
}
